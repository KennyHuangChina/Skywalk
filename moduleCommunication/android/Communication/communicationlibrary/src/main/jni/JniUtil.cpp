//
// Created by kenny on 2017/2/16.
//

#include "JniUtil.h"
#include <stdlib.h>

// return true for exception cleared and false for no exception occurred.
bool ClearException(JNIEnv * env)
{
    jthrowable exception = env->ExceptionOccurred();
    if (NULL != exception)
    {
        env->ExceptionDescribe();
        env->ExceptionClear();
        return true;
    }
    return false;
}
