#!/bin/bash
# Termux-friendly SDK license acceptance helper
# Set ANDROID_SDK_ROOT to your SDK path before running (example: export ANDROID_SDK_ROOT=$HOME/Android/Sdk)
if [ -z "$ANDROID_SDK_ROOT" ]; then
  echo "Set ANDROID_SDK_ROOT, e.g.: export ANDROID_SDK_ROOT=~/Android/Sdk"
  exit 1
fi
SDKMANAGER="$ANDROID_SDK_ROOT/cmdline-tools/latest/bin/sdkmanager"
if [ ! -x "$SDKMANAGER" ]; then
  SDKMANAGER="$ANDROID_SDK_ROOT/tools/bin/sdkmanager"
fi
if [ ! -x "$SDKMANAGER" ]; then
  echo "sdkmanager not found. Install Android cmdline-tools or use Android Studio."
  exit 2
fi
echo "Accepting licenses..."
yes | "$SDKMANAGER" --licenses
echo "Done."
