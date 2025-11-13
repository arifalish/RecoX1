package com.bcs.recox

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.airbnb.lottie.LottieAnimationView

class MainActivity : AppCompatActivity() {
    private lateinit var btnToggle: Button
    private lateinit var tvStatus: TextView
    private lateinit var rvHistory: RecyclerView
    private lateinit var fabSettings: FloatingActionButton
    private lateinit var lottieRecord: LottieAnimationView

    private val requestPermLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[Manifest.permission.RECORD_AUDIO] == true &&
                      perms[Manifest.permission.READ_PHONE_STATE] == true
        if (granted) tvStatus.text = "Permissions granted"
        else tvStatus.text = "Please grant permissions"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnToggle = findViewById(R.id.btnToggle)
        tvStatus = findViewById(R.id.tvStatus)
        rvHistory = findViewById(R.id.rvHistory)
        fabSettings = findViewById(R.id.fabSettings)
        lottieRecord = findViewById(R.id.lottieRecord)

        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = HistoryAdapter(listOf())

        btnToggle.setOnClickListener {
            val running = Prefs.isServiceRunning(this)
            if (!running) {
                startService(Intent(this, RecordingService::class.java))
                btnToggle.text = "Stop"
                tvStatus.text = "Service running"
                lottieRecord.playAnimation()
                Prefs.setServiceRunning(this, true)
            } else {
                stopService(Intent(this, RecordingService::class.java))
                btnToggle.text = "Start"
                tvStatus.text = "Service stopped"
                lottieRecord.pauseAnimation()
                Prefs.setServiceRunning(this, false)
            }
        }

        // request permissions
        val needed = mutableListOf<String>()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            needed.add(Manifest.permission.RECORD_AUDIO)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
            needed.add(Manifest.permission.READ_PHONE_STATE)

        if (needed.isNotEmpty()) {
            requestPermLauncher.launch(needed.toTypedArray())
        }

        fabSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }
}
