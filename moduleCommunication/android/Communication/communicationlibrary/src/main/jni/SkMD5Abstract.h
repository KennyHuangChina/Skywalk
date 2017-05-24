//
// Created by kenny on 2017/2/16.
//

#ifndef COMMUNICATION_SKMD5ABSTRACT_H
#define COMMUNICATION_SKMD5ABSTRACT_H


#define MD5_BYTES 16

class SkMD5Abstract {
public:
    SkMD5Abstract ();
    SkMD5Abstract (void * ctx);
    virtual ~SkMD5Abstract () = 0;

    // return MD5 digest result buffer, length should be MD5_BYTES bytes
    virtual unsigned char * Digest(unsigned char * in, int inSize) = 0;
};

// ctx is platform/implementation related parameter.
// To Android JNI implementation, ctx is a point to JNIEnv
SkMD5Abstract* GetMD5Instance(void * ctx);

#endif //COMMUNICATION_SKMD5ABSTRACT_H
