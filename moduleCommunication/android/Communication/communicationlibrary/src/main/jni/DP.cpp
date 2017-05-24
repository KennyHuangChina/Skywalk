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

void PRINT_BUFF(const char* szBuffName, const char* pBuff, unsigned int nLen)
{
    if (NULL == pBuff || 0 == nLen)
    {
        return;
    }

    const int PRINT_LEN = 100;
    char szTmp[10];
    char szPrint[PRINT_LEN] = {0};
    for (int i = 0; i < nLen; ++i) {
        char tmp = *(pBuff + i);
        sprintf(szTmp, "%.2x ", tmp);
        strcat(szPrint, szTmp);
        if (strlen(szPrint) >= PRINT_LEN - 4)
        {
            strcat(szPrint, " ...");
            break;
        }
    }
    DP_LOG("%s: %s", szBuffName, szPrint);
}

#endif  // ANDROID
