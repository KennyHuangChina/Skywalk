//
// Created by kenny on 2017/2/16.
//
#include "com_kjs_skywalk_communicationlibrary_NativeCall.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <string>
#include "SkMD5Abstract.h"
#include "DP.h"
//#include "aes_encryptor.h"
#include "AES.h"
#include "DigUtil.h"
#include <sstream>
#include <unistd.h>

using std::string;
using std::stringstream;
using std::pair;

/*
 * Class:     com_kjs_skywalk_communicationlibrary_NativeCall
 * Method:    GeneratePassword
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)[B
 */
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
//    PRINT_BUFF("md5Result", (const char*)md5Result, MD5_BYTES);

    strcpy(szSrc, (const char*)md5Result);
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

/*
 * Class:     com_kjs_skywalk_communicationlibrary_NativeCall
 * Method:    GenerateReloginSession
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_kjs_skywalk_communicationlibrary_NativeCall_GenerateReloginSession
        (JNIEnv* env, jobject thiz, jstring user, jstring rand, jstring sid, jint ver)
{
    if (1 != ver ) {
        return NULL;
    }
    const char* szUser = env->GetStringUTFChars(user, false);
    if (NULL == szUser || 0 == szUser[0]) {
        return NULL;
    }
    DP_LOG("user:%s", szUser);
    const char* szRand = env->GetStringUTFChars(rand, false);
    if (NULL == szRand || 0 == szRand[0]) {
        return NULL;
    }
    DP_LOG("rand:%s", szRand);
    const char* szSession = env->GetStringUTFChars(sid, false);
    if (NULL == szSession || 0 == szSession[0]) {
        return NULL;
    }
    DP_LOG("sid:%s", szSession);

    SkMD5Abstract* md5 = GetMD5Instance(env);
    if (NULL == md5)
    {
        return NULL;
    }

    // v1: session = md5(user + rand + sid), session = md5(session) for another 22 times
    char szSrc[100];
    strcpy(szSrc, szUser);
    strcat(szSrc, szRand);
    strcat(szSrc, szSession);
    DP_LOG("szSrc:%s", szSrc);

    unsigned char * md5Result = NULL;
    for (int i = 0; i < 23; ++i) {
//        DP_LOG("md5Result(%d):%s", i + 1, md5Result);
        md5Result = md5->Digest((unsigned char*)szSrc, strlen(szSrc));
        if (NULL == md5Result) {
            return NULL;
        }
//        DP_LOG("md5Result(%d):%s", i + 1, md5Result);
        strcpy(szSrc, (const char*)md5Result);
    }
    DP_LOG("Final md5Result:%s", md5Result);
//    PRINT_BUFF("md5Result", (const char*)md5Result, MD5_BYTES);

    // Export
    int nLen = strlen((const char*)md5Result);
    jbyteArray jbArray = env->NewByteArray(nLen);
    env->SetByteArrayRegion(jbArray, 0, nLen, (jbyte*)md5Result);
//    sigGen.ReleaseSignature(md5Result);

    return jbArray;
}

/*
 * Class:     com_kjs_skywalk_communicationlibrary_NativeCall
 * Method:    GeneratePassword
 * Signature: (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)[B
 */
#pragma clang diagnostic push
#pragma ide diagnostic ignored "NotAssignable"
JNIEXPORT jbyteArray JNICALL Java_com_kjs_skywalk_communicationlibrary_NativeCall_GenerateNewPassword
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
//    DP_LOG("pswd:%s", szPswd);
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

    // make key for AES encrypting
    char szKey[100];
    if (NULL == MakeAesKey(env, szSalt, szRand, ver, szKey))
    {
        DP_ERR("Fail to make AES key");
        return NULL;
    }

    string szCryptText;
    AesEncrypt((unsigned char*)szPswd, strlen(szPswd), (unsigned char*)szKey, szCryptText);
    DP_LOG("szCryptText: %s, len:%d", szCryptText.c_str(), szCryptText.length());

    // self-test
//    string plaintxt;
//    AesDecrypt((unsigned char*)szCryptText.c_str(), szCryptText.length(), (unsigned char*)szKey, plaintxt);
//    DP_LOG("plaintxt: %s", plaintxt.c_str());

    string base64;
    SkDigestUtil::Base64Encode(env, (unsigned char*)szCryptText.c_str(), szCryptText.length(), base64);
    DP_LOG("base64: %s", base64.c_str());

    // Export
    int nLen = base64.length();
    jbyteArray jbArray = env->NewByteArray(nLen);
    env->SetByteArrayRegion(jbArray, 0, nLen, (jbyte*)base64.c_str());

//    AesEncryptor *pAesEncryptor = new AesEncryptor((unsigned char*)md5Result);
//    if (NULL == pAesEncryptor)
//    {
//        DP_ERR("Fail to create AES encryptor");
//        return NULL;
//    }
//
////    std::string strPass = szPswd;
//    std::string strPass = pAesEncryptor->EncryptString(szPswd);
//    DP_LOG("strPass:%s", strPass.data());
//    PRINT_BUFF("strPass", (const char*)strPass.data(), strPass.length());
//
//    // Export
//    int nLen = strPass.length();
//    jbyteArray jbArray = env->NewByteArray(nLen);
//    env->SetByteArrayRegion(jbArray, 0, nLen, (jbyte*)strPass.data());
////    sigGen.ReleaseSignature(md5Result);

    return jbArray;
}
#pragma clang diagnostic pop
