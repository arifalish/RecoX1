#!/bin/bash
# Generate a release keystore for RecoX
# Usage: ./generate_keystore.sh <keystore_password> <key_alias> <key_password> <your_name> <your_org> <your_countryCode>
set -e
if [ $# -lt 6 ]; then
  echo "Usage: $0 <keystore_password> <key_alias> <key_password> <your_name> <your_org> <countryCode>"
  exit 1
fi
KS_PASS=$1
KEY_ALIAS=$2
KEY_PASS=$3
NAME=$4
ORG=$5
COUNTRY=$6
mkdir -p keystore
keytool -genkeypair -v -keystore keystore/recox.keystore -storepass "$KS_PASS" -alias "$KEY_ALIAS" -keypass "$KEY_PASS" -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=$NAME, OU=$ORG, O=$ORG, L=City, ST=State, C=$COUNTRY"
echo "Keystore generated at keystore/recox.keystore with alias $KEY_ALIAS"
