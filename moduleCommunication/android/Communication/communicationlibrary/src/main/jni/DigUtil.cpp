//
// Created by kenny on 2017/2/16.
//

#include "DigUtil.h"
#include "JniUtil.h"
#include "DP.h"

const char * SkDigestUtil::Base64Decode(JNIEnv * env, const char * input, string & output)
{
    return NULL;
}

const char * SkDigestUtil::Base64Encode(JNIEnv * env, unsigned char * input, int inSize, string & output)
{
    jclass classBase64 = env->FindClass("android/util/Base64");
    if (classBase64 == NULL) {
        ClearException(env);
        return NULL;
    }
    jmethodID midDecode = env->GetStaticMethodID(classBase64, "encode", "([BI)[B");
    if (midDecode == NULL) {
        ClearException(env);
    return NULL;
}
    jfieldID fidDefault = env->GetStaticFieldID(classBase64, "DEFAULT", "I");
    if (fidDefault == NULL) {
        ClearException(env);
        return NULL;
    }

    jint flagDefault = env->GetStaticIntField(classBase64, fidDefault);

    jbyteArray jbArrayInput = env->NewByteArray(inSize);
    env->SetByteArrayRegion(jbArrayInput, 0, inSize, (jbyte*)input);
    jbyteArray jbArrayOutput = (jbyteArray)env->CallStaticObjectMethod(classBase64, midDecode, jbArrayInput, flagDefault);
    env->DeleteLocalRef(jbArrayInput);

    int length = env->GetArrayLength(jbArrayOutput);
    if (length == 0)
    {
        env->DeleteLocalRef(jbArrayOutput);
        return NULL;
    }

    output.resize(length);
    env->GetByteArrayRegion(jbArrayOutput, 0, length, (jbyte *)&output[0]);
    env->DeleteLocalRef(jbArrayOutput);
    return output.c_str();
}

const char * SkDigestUtil::Digest(JNIEnv * env, const char * input, string & output)
{
    return NULL;
}

unsigned char * SkDigestUtil::Digest(JNIEnv * env, unsigned char * input, int inSize, vector<unsigned char> &output)
{
    jclass classMessageDigest = env->FindClass("java/security/MessageDigest");
    if (classMessageDigest == NULL) {
        ClearException(env);
        return NULL;
    }

    jmethodID MessageDigest_getInstance = env->GetStaticMethodID(classMessageDigest, "getInstance", "(Ljava/lang/String;)Ljava/security/MessageDigest;");
    if (MessageDigest_getInstance == NULL) {
        ClearException(env);
        return NULL;
    }

    jmethodID MessageDigest_update = env->GetMethodID(classMessageDigest, "update", "([B)V");
    if (MessageDigest_update == NULL) {
        ClearException(env);
        return NULL;
    }

    jmethodID MessageDigest_digest = env->GetMethodID(classMessageDigest, "digest", "()[B");
    if (MessageDigest_digest == NULL) {
        ClearException(env);
        return NULL;
    }

    jstring digestName = env->NewStringUTF("MD5");
    jobject digestInstance = env->CallStaticObjectMethod(classMessageDigest,
                                                         MessageDigest_getInstance,
                                                         digestName);
    env->DeleteLocalRef(digestName);
    env->DeleteLocalRef(classMessageDigest);

    jbyteArray jbArray = env->NewByteArray(inSize);
    env->SetByteArrayRegion(jbArray, 0, inSize, (jbyte*)input);
    env->CallVoidMethod(digestInstance, MessageDigest_update, jbArray);
    env->DeleteLocalRef(jbArray);
    jbyteArray jbArrayOutput = (jbyteArray)env->CallObjectMethod(digestInstance, MessageDigest_digest);
    env->DeleteLocalRef(digestInstance);

    int length = env->GetArrayLength(jbArrayOutput);
    if (length == 0)
    {
        env->DeleteLocalRef(jbArrayOutput);
        return NULL;
    }

    output.clear();
    output.resize(length);
    env->GetByteArrayRegion(jbArrayOutput, 0, length, (jbyte *)&output[0]);
    env->DeleteLocalRef(jbArrayOutput);

    return &output[0];
}

unsigned  char* SkDigestUtil::Digest2Bcd(const char* pDigest, unsigned int nDigLen, unsigned char* szBcd, unsigned int nBcdSize)  // static
{
    if (NULL == pDigest || 0 == nDigLen || NULL == szBcd || nBcdSize < 2 * nDigLen)
    {
        DP_ERR("error arguments: pDigest:%#x, nDigLen:%d, szBcd:%#x, nBcdSize:%d", pDigest, nDigLen, szBcd, nBcdSize);
        return NULL;
    }
    szBcd[0] = 0x00;

    char szDig[10] = {0};
    for (int n = 0; n <nDigLen; n++) {
        sprintf(szDig, "%.2x", pDigest[n]);
        strcat((char*)szBcd, szDig);
    }

    return szBcd;
}

int ahextoi( const char* hex_str)
{
    int r =0;
    if(hex_str)
        sscanf(hex_str,"%x",&r);
    return r;
}

unsigned char* SkDigestUtil::Bcd2Digest(const char* szBCD, unsigned char* szDigest, unsigned int& nDigestSize)
{
    if (NULL == szBCD || 0 == strlen(szBCD) || NULL == szDigest || nDigestSize * 2 < strlen(szBCD))
    {
        DP_ERR("error arguments: szBCD:%#x, szDigest:%#x, nDigestSize:%d", szBCD, szDigest, nDigestSize);
        return NULL;
    }
//    DP_LOG("szBCD:%#x, nDigLen:%d, szDigest:%#x, nDigestSize:%d", szBCD, strlen(szBCD), szDigest, nDigestSize);
    szDigest[0] = 0x00;
    nDigestSize = 0;

    int nBcdLen = strlen(szBCD);
    int nV = 0;
    char szV[5];
    for (int i = 0; i < nBcdLen; i += 2)
    {
        strcpy(szV, "0x");
        memcpy(szV+2, szBCD + i, 2);
        szV[4] = 0;
        nV = ahextoi(szV);
        szDigest[i] = nV;
        nDigestSize ++;
        DP_LOG("%d:, szV:%s, %.2x, %d %c", i/2, szV, szDigest[i], szDigest[i], szDigest[i]);
    }

    return szDigest;
}
