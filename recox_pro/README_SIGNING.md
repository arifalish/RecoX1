RecoX - Signed Release Ready Project
===================================

This version of RecoX is prepared for building a signed release APK for Google Play.
It includes advanced feature scaffolding: transcription, noise suppression, waveform visualization,
WorkManager uploads, Lottie animations and EncryptedSharedPreferences for secure token storage.

IMPORTANT: I cannot build the signed APK in this environment (Gradle/Android SDK not available here).
But this project is fully configured so you can build it locally or on your CI/Jules AI environment.

Steps to generate a signed APK (local machine or Jules AI):
-----------------------------------------------------------
1. Generate a keystore (one-time):
   ./generate_keystore.sh <keystore_password> <key_alias> <key_password> "Your Name" "Your Org" "IN"

   Example:
   ./generate_keystore.sh S3curePass recox_key S3curePass "ARIF ALI" "Kadrik" "IN"

   This creates keystore/recox.keystore

2. Export environment variables (recommended) or set in gradle.properties:
   export RECOX_KEYSTORE_PASSWORD=S3curePass
   export RECOX_KEY_ALIAS=recox_key
   export RECOX_KEY_PASSWORD=S3curePass

3. Build signed release APK:
   ./gradlew clean assembleRelease

   Or use the helper:
   ./build_release.sh

   The signed APK will be at:
   app/build/outputs/apk/release/app-release.apk

4. Test & Upload:
   - Install the APK on a test device: adb install -r app/build/outputs/apk/release/app-release.apk
   - Test Telegram upload workflow, check EncryptedSharedPreferences stored tokens in Settings.
   - When ready, upload to Google Play Console (follow Play Store checklist below).

Google Play readiness checklist:
- Keystore securely stored and backed up.
- TargetSdk 34 and compileSdk 34 set.
- Privacy policy URL if app records calls (required by Google Play).
- Proper runtime permissions handling with user-facing consent.
- Ads/Analytics opt-in/out if applicable.
- Remove any hard-coded tokens before publishing.

Advanced Features included (scaffolded / ready to implement):
- TranscriptionWorker.kt -> hook to Google Cloud Speech or on-device STT for transcripts
- NoiseSuppressor.kt -> stub for RNNoise or WebRTC noise suppression pipeline
- WaveformView.kt -> placeholder for waveform visualization during recording
- UploadWorker.kt -> WorkManager-based reliable uploader with retry (already in project)
- SettingsActivity.kt -> Secure token storage with EncryptedSharedPreferences
- Lottie support (add your .json to res/raw and set in MainActivity)

If you want, I can now:
- Produce a step-by-step script to run on Jules AI to build the APK automatically (CI script).
- Pre-fill keystore using a keystore you provide (NOT recommended over chat).
- Add Google Speech transcription integration (requires cloud credentials).
- Build the APK in your Jules AI environment if you provide build access or run these commands there.
