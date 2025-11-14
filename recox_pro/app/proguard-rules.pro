# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/user/Library/Android/sdk/tools/proguard/proguard-android-optimize.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Firebase and Google Services
-keep class com.google.android.gms.common.** { *; }
-keep class com.google.android.gms.tasks.** { *; }
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**
-keepnames class com.google.android.gms.measurement.AppMeasurement

# Keep model classes (data classes) used by Firestore
-keep class com.bcs.recox.** { *; }
-keep public class * extends com.google.firebase.firestore.IgnoreExtraProperties

# Keep custom Application class
-keep class com.bcs.recox.RecoXApp

# Keep activities, services, etc.
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.view.View

# OkHttp and related libraries
-dontwarn okio.**
-dontwarn okhttp3.**

# Lottie
-keep class com.airbnb.lottie.** { *; }
