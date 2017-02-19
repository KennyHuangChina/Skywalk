//
// Created by kenny on 2017/2/16.
//

#ifndef COMMUNICATION_SKMD5IMPL_H
#define COMMUNICATION_SKMD5IMPL_H

#include "SkMD5Abstract.h"

class SkMD5Impl : public SkMD5Abstract
{
public:
    SkMD5Impl(void * ctx);
    virtual ~SkMD5Impl();

    // return MD5 digest result buffer, length should be MD5_BYTES bytes
    virtual unsigned char * Digest(unsigned char * in, int inSize); // = 0;

private:
    SkMD5Impl();

private:
    void * mEnv;
    unsigned char mMD5Buf[MD5_BYTES];
    unsigned char mMD5String[2 * MD5_BYTES + 1];
};
#endif //COMMUNICATION_SKMD5IMPL_H
