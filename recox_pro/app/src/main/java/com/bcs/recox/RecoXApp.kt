package com.bcs.recox

import android.app.Application
import com.google.firebase.FirebaseApp

class RecoXApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
