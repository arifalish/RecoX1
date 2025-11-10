package com.bcs.RecoX

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val etBot = findViewById<EditText>(R.id.etBotToken)
        val etChat = findViewById<EditText>(R.id.etChatId)
        val btnSave = findViewById<Button>(R.id.btnSave)

        etBot.setText(Prefs.getBotToken(this))
        etChat.setText(Prefs.getChatId(this))

        btnSave.setOnClickListener {
            Prefs.saveBotToken(this, etBot.text.toString())
            Prefs.saveChatId(this, etChat.text.toString())
            finish()
        }
    }
}
