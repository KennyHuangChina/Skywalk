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

char* MakeAesKey(const char* szSalt, const char* szRand, int ver, char* szKey)
{
    szKey[0] = 0x00;
    if (NULL == szSalt || 0 == szSalt[0] || NULL == szRand || 0 == szRand[0] || ver <= 0)
    {
        return NULL;
    }

    switch (ver)
    {
    case 1:
        strcpy(szKey, szSalt);
        strcat(szKey, szRand);
        break;
    }

    DP_LOG("szKey:%s", szKey);
    return szKey;
}

static int aes_encrypt(unsigned char* out, unsigned char* in, int size, const unsigned char* key)
{
    for (int i = 0; i < size / 16; i++) {
        AES128_CBC_encrypt_buffer(out + 16 * i, in + 16 * i, 16, i == 0 ? key : NULL, i == 0 ? key : NULL);
//        DP_LOG("[aes_encrypt] szKey:%s", szKey);
    }

    return size;
}

static int aes_decrypt(unsigned char* out, unsigned char* in, int size, const unsigned char* key)
{
    for (int i = 0; i < size / 16; i++) {
        AES128_CBC_decrypt_buffer(out + 16 * i, in + 16 * i, 16, i == 0 ? key : NULL, i == 0 ? key : NULL);
    }

    return out[size - 1]; // PKCS5 Padding
}

const char* AesEncrypt(const unsigned char* input, int size, const unsigned char* key, /*unsigned char* output) //*/ string& output)
{
//    if (NULL == output)
//    {
//        return NULL;
//    }
//    output[0] = 0x00;
    DP_LOG("[AesEncrypt] input: %s, key: %s", input, key);
    output.clear();

    int new_size = 0;

    if (size % 16 == 0) {
        new_size = size + 16;
    }
    else {
        new_size = (size / 16) * 16 + 16;
    }
    DP_LOG("[AesEncrypt] size:%d, new_size:%d", size, new_size);

    unsigned char* new_input = new unsigned char[new_size];
    unsigned char* buffer = new unsigned char[new_size];

    memcpy(new_input, input, size);
    memset(new_input + size, (unsigned char)(new_size - size), new_size - size); // PKCS5 Padding

    aes_encrypt(buffer, new_input, new_size, key);

    output = string((const char*)buffer, new_size); // The encrypted buffer MAY contains one or more '\0' in the middle
//    memcpy(output, (const char*)buffer, new_size);

    unsigned char szBcd[100];
    SkDigestUtil::Digest2Bcd((const char*)output.c_str(), new_size, szBcd, sizeof(szBcd));
    DP_LOG("[AesEncrypt] output(BCD): %s", szBcd);

    delete[] buffer;
    return output.c_str();
//    return (const char*)output;
}

const char* AesDecrypt(const unsigned char* input, int size, const unsigned char* key, string& output)
{
    output.clear();

    if(size % 16 != 0) {
        return NULL;
    }

    unsigned char* buffer = new unsigned char[size];
    int ret = aes_decrypt(buffer, (unsigned char*)input, size, (unsigned char*)key);

    if(ret > 16) {
        delete[] buffer;
        return NULL;
    } else {
        buffer[size - ret] = '\0';  // The decrypted MUST be a null-terminated char array
        output = string((const char*)buffer, size - ret + 1);
    }

    delete[] buffer;
    return output.c_str();
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

    // make key for AES encrypting
    char szKey[100];
    if (NULL == MakeAesKey(szSalt, szRand, ver, szKey))
    {
        DP_ERR("Fail to make AES key");
        return NULL;
    }
    SkMD5Abstract* md5 = GetMD5Instance(env);
    if (NULL == md5)
    {
        return NULL;
    }
    unsigned char * md5Result = md5->Digest((unsigned char*)szKey, strlen(szKey));
    if (NULL == md5Result) {
        return NULL;
    }
    DP_LOG("Final md5Result: %s", md5Result);

    int nKeySize = sizeof(szKey);
    SkDigestUtil::Bcd2Digest((const char*)md5Result, (unsigned char*)szKey, (unsigned int&)nKeySize);


//    strcpy((char*)md5Result, "f6d59aa26b582907");
//    DP_LOG("Final md5Result:%s", md5Result);

//    char szCryptText[100];
    memcpy(szKey, md5Result, 16);
    szKey[16] = 0x00;
//    strcpy(szKey, "1234567890123456");
    DP_LOG("szKey: %s", szKey);
    string szCryptText;
    AesEncrypt((unsigned char*)szPswd, strlen(szPswd), (unsigned char*)szKey, szCryptText);
    DP_LOG("szCryptText: %s, len:%d", szCryptText.c_str(), szCryptText.length());

    string plaintxt;
    AesDecrypt((unsigned char*)szCryptText.c_str(), szCryptText.length(), (unsigned char*)szKey, plaintxt);
    DP_LOG("plaintxt: %s", plaintxt.c_str());

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
