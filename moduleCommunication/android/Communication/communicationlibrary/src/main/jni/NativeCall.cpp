//
// Created by kenny on 2017/2/16.
//
#include "com_kjs_skywalk_communicationlibrary_NativeCall.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "SkMD5Abstract.h"
#include "DP.h"

//using std::string;

JNIEXPORT jbyteArray JNICALL Java_com_kjs_skywalk_communicationlibrary_NativeCall_GeneratePassword
  (JNIEnv* env, jobject thiz, jstring pswd, jstring salt, jstring rand, jint ver)
{
    if (1 != ver )
    {
        return NULL;
    }
    const char* szPswd = env->GetStringUTFChars(pswd, false);
    if (NULL == szPswd || 0 == szPswd[0])
    {
        return NULL;
    }
    DP_LOG("pswd:%s", szPswd);
    const char* szSalt = env->GetStringUTFChars(salt, false);
    if (NULL == szSalt || 0 == szSalt[0])
    {
        return NULL;
    }
    DP_LOG("salt:%s", szSalt);
    const char* szRand = env->GetStringUTFChars(rand, false);
    if (NULL == szRand || 0 == szRand[0])
    {
        return NULL;
    }
    DP_LOG("random:%s", szRand);

    SkMD5Abstract* md5 = GetMD5Instance(env);
    if (NULL == md5)
    {
        return NULL;
    }

    // ver1: md5(md5(pass+salt)+rand)
    char szSrc[100];
    strcpy(szSrc, szPswd);
    strcat(szSrc, szSalt);

    DP_LOG("szSrc:%s", szSrc);
    unsigned char * md5Result = md5->Digest((unsigned char*)szSrc, strlen(szSrc));
    if (NULL == md5Result)
    {
        return NULL;
    }
    DP_LOG("md5Result:%s", md5Result);
    PRINT_BUFF("md5Result", (const char*)md5Result, MD5_BYTES);

    memcpy(szSrc, md5Result, MD5_BYTES);
    strcat(szSrc, szRand);
    DP_LOG("szSrc:%s", szSrc);
    PRINT_BUFF("szSrc", (const char*)md5Result, MD5_BYTES + strlen(szRand));
    md5Result = md5->Digest((unsigned char*)szSrc, strlen(szSrc));
    if (NULL == md5Result)
    {
        return NULL;
    }
    DP_LOG("md5Result:%s", md5Result);
    PRINT_BUFF("md5Result", (const char*)md5Result, MD5_BYTES);

    // Export
    int nLen = strlen((const char*)md5Result);
    jbyteArray jbArray = env->NewByteArray(nLen);
    env->SetByteArrayRegion(jbArray, 0, nLen, (jbyte*)md5Result);
//    sigGen.ReleaseSignature(md5Result);

    return jbArray;
}