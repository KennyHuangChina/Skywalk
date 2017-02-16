//
// Created by kenny on 2017/2/16.
//
#include "dp.h"
#include <stdio.h>
#include <string.h>


#ifdef ANDROID

#define LOG_TAG "SK_SIG"
#include <android/log.h>

void SK_DP(const char * szFormat, ...)
{
    char szBuffer[256];
    va_list vl;
    va_start(vl, szFormat);

    memset(szBuffer, 0, sizeof(szBuffer));

    vsnprintf(szBuffer,255,szFormat,vl);
    szBuffer[255]=0;

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, "%s", szBuffer);
    va_end(vl);
}
#endif  // ANDROID
