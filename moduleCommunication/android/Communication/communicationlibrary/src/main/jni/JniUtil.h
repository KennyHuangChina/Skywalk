//
// Created by kenny on 2017/2/16.
//

#ifndef COMMUNICATION_JNIUTIL_H
#define COMMUNICATION_JNIUTIL_H

#include <jni.h>

// return true for exception cleared and false for no exception occurred.
extern bool ClearException(JNIEnv * env);

#endif //COMMUNICATION_JNIUTIL_H
