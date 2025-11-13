package com.bcs.recox

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object Prefs {
    private const val NAME = "recox_prefs"
    private const val KEY_SERVICE = "service_running"
    private const val KEY_BOT = "bot_token"
    private const val KEY_CHAT = "chat_id"

    private fun encryptedPrefs(ctx: Context) =
        EncryptedSharedPreferences.create(
            ctx,
            NAME,
            MasterKey.Builder(ctx).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

    fun setServiceRunning(ctx: Context, v: Boolean) {
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit().putBoolean(KEY_SERVICE, v).apply()
    }
    fun isServiceRunning(ctx: Context) =
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).getBoolean(KEY_SERVICE, false)

    fun saveBotToken(ctx: Context, token: String) {
        encryptedPrefs(ctx).edit().putString(KEY_BOT, token).apply()
    }
    fun saveChatId(ctx: Context, id: String) {
        encryptedPrefs(ctx).edit().putString(KEY_CHAT, id).apply()
    }
    fun getBotToken(ctx: Context) = encryptedPrefs(ctx).getString(KEY_BOT, "") ?: ""
    fun getChatId(ctx: Context) = encryptedPrefs(ctx).getString(KEY_CHAT, "") ?: ""
}
