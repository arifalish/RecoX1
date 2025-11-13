package com.bcs.recox

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.MediaRecorder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.core.app.NotificationCompat
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class RecordingService : Service() {

    private var recorder: MediaRecorder? = null
    private val handler = Handler()
    private val executor = Executors.newSingleThreadExecutor()
    private var isRecording = false
    private lateinit var telephonyManager: TelephonyManager
    private val segmentDurationMs = 25_000L

    override fun onCreate() {
        super.onCreate()
        startForegroundIfNeeded()
        telephonyManager = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun startForegroundIfNeeded() {
        val channelId = "recox_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(NotificationManager::class.java)
            val chan = NotificationChannel(channelId, "RecoX Recorder", NotificationManager.IMPORTANCE_LOW)
            manager.createNotificationChannel(chan)
        }
        val notif = NotificationCompat.Builder(this, channelId)
            .setContentTitle("RecoX Recorder")
            .setContentText("Listening for calls")
            .setSmallIcon(android.R.drawable.ic_btn_speak_now)
            .build()
        startForeground(201, notif)
    }

    private val phoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, phoneNumber: String?) {
            when (state) {
                TelephonyManager.CALL_STATE_OFFHOOK -> if (!isRecording) startRecordingChunks()
                TelephonyManager.CALL_STATE_IDLE -> stopRecordingChunks()
                else -> {}
            }
        }
    }

    private fun startRecordingChunks() {
        isRecording = true
        scheduleNextChunk()
    }

    private fun scheduleNextChunk() {
        startChunkRecording { if (isRecording) scheduleNextChunk() }
    }

    private fun startChunkRecording(onChunkFinished: () -> Unit) {
        val ts = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val f = File(cacheDir, "rec_$ts.3gp")

        try {
            recorder = MediaRecorder().apply {
                try { setAudioSource(MediaRecorder.AudioSource.VOICE_CALL) }
                catch (e: Exception) { try { setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION) } catch (e2: Exception) { setAudioSource(MediaRecorder.AudioSource.MIC) } }
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(f.absolutePath)
                prepare()
                start()
            }
        } catch (ex: Exception) { ex.printStackTrace(); recorder?.release(); recorder = null; onChunkFinished(); return }

        handler.postDelayed({
            try { recorder?.apply { stop(); release() } } catch (_: Exception) {}
            recorder = null
            // Enqueue upload work
            val input = Data.Builder().putString("path", f.absolutePath).putString("ts", ts).build()
            val work = OneTimeWorkRequestBuilder<UploadWorker>().setInputData(input).build()
            WorkManager.getInstance(applicationContext).enqueue(work)
            onChunkFinished()
        }, segmentDurationMs)
    }

    private fun stopRecordingChunks() {
        isRecording = false
        try { recorder?.stop(); recorder?.release() } catch (_: Exception) {}
        recorder = null
    }

    override fun onDestroy() {
        super.onDestroy()
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        executor.shutdownNow()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
