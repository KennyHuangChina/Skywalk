//
// Created by kenny on 2017/2/16.
//
#include <stdlib.h>
#include "SkMD5Impl.h"
#include "DigUtil.h"

SkMD5Abstract* GetMD5Instance(void * ctx)
{
    SkMD5Impl* pMd5 = new SkMD5Impl(ctx);
    return pMd5;
}

SkMD5Impl::SkMD5Impl()
{
}

SkMD5Impl::SkMD5Impl(void * ctx) : mEnv(ctx)
{
}

SkMD5Impl::~SkMD5Impl()     // virtual
{
}

unsigned char* SkMD5Impl::Digest(unsigned char * in, int inSize)  // virtual = 0;
{
    vector<unsigned char> vtOut;
    if (NULL == SkDigestUtil::Digest((JNIEnv *)mEnv, in, inSize, vtOut))
    {
        return NULL;
    }

    if (vtOut.size() != MD5_BYTES)
    {
        return NULL;
    }

    memcpy(mMD5Buf, &vtOut[0], MD5_BYTES);
    return mMD5Buf;
}
