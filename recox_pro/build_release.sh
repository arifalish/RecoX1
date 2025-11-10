#!/bin/bash
# Build signed release APK using gradle wrapper
# Expects environment variables or gradle.properties for RECOX_KEYSTORE_PASSWORD, RECOX_KEY_ALIAS, RECOX_KEY_PASSWORD
set -e
./gradlew clean assembleRelease -Pandroid.injected.signing.store.file=keystore/recox.keystore -Pandroid.injected.signing.store.password=$RECOX_KEYSTORE_PASSWORD -Pandroid.injected.signing.key.alias=$RECOX_KEY_ALIAS -Pandroid.injected.signing.key.password=$RECOX_KEY_PASSWORD
echo "Release build finished. APK located in app/build/outputs/apk/release/"
