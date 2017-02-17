//
// Created by kenny on 2017/2/16.
//

#ifndef COMMUNICATION_DP_H
#define COMMUNICATION_DP_H

extern void SK_DP(const char * szFormat, ...);
extern void PRINT_BUFF(const char* szBuffName, const char* pBuff, unsigned int nLen);

#ifdef __linux__

#ifdef DEBUG_INFO
    #define DP_LOG(x...) SK_DP(" [LOG] "x)
#endif

#define DP_WARN(x...) SK_DP(" [WARNING] "x)
#define DP_ERR(x...) SK_DP(" [ERROR] "x)

#endif


#endif //COMMUNICATION_DP_H
