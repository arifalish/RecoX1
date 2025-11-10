RecoX - Advanced Caller Recorder (Starter)
=========================================

What's included:
- App name: RecoX
- Settings screen to store Telegram Bot Token & Chat ID securely (EncryptedSharedPreferences)
- WorkManager-based uploader with retry
- Lottie integration placeholders for animations
- Termux-friendly accept_licenses.sh helper
- Gradle wrapper scripts (gradlew), but you may need to let Android Studio download Gradle distribution

How to use:
1. Unzip and open this folder in Android Studio.
2. Let Gradle sync. Install SDK 34 if prompted.
3. Run on a real device. Grant RECORD_AUDIO and READ_PHONE_STATE.
4. Open Settings (gear icon) and enter your Bot Token & Chat ID.
5. Use Start button and make a test call. Uploaded segments will appear in your bot chat.

Termux SDK license:
- Use the included accept_licenses.sh script; ensure ANDROID_SDK_ROOT is set.
