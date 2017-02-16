//
// Created by kenny on 2017/2/16.
//

#include "DigUtil.h"
#include "JniUtil.h"

const char * SkDigestUtil::Base64Decode(JNIEnv * env, const char * input, string & output)
{
    return NULL;
}

const char * SkDigestUtil::Base64Encode(JNIEnv * env, unsigned char * input, int inSize, string & output)
{
    return NULL;
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

