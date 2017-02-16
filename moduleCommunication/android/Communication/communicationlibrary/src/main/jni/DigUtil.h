//
// Created by kenny on 2017/2/16.
//

#ifndef COMMUNICATION_DIGUTIL_H
#define COMMUNICATION_DIGUTIL_H


#include <jni.h>
#include <string>
#include <vector>

using std::string;
using std::vector;

class SkDigestUtil
{
public:
    static const char * Base64Decode(JNIEnv * env, const char * input, string & output);
    static const char * Base64Encode(JNIEnv * env, unsigned char * input, int inSize, string & output);
    static const char * Digest(JNIEnv * env, const char * input, string & output);
    static unsigned char * Digest(JNIEnv * env, unsigned char * input, int inSize, vector<unsigned char> & output);
};

#endif //COMMUNICATION_DIGUTIL_H
