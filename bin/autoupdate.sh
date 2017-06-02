#!/bin/bash 
PACKAGE_NAME=com.example.showyuv
APK_NAME=myyuvviewer.apk
PATCH=$PWD
cd $ANDROID_SDK_HOME
rm -rf out/target/common/obj/APPS/myyuvviewer_intermediates
echo $PWD
find $PATCH -name "R.java" |xargs rm 
mmm $PATCH
adb uninstall $PACKAGE_NAME 
adb install out/target/product/wing-k70/system/app/$APK_NAME
adb shell am start -n $PACKAGE_NAME/$PACKAGE_NAME.MainActivity
cd $PATCH
