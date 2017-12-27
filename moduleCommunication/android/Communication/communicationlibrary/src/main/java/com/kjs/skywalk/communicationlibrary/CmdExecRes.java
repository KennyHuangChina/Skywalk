package com.kjs.skywalk.communicationlibrary;

/**
 * Created by kenny on 2017/12/27.
 */

public class CmdExecRes {
    public int                  mError;     // err or not, ref to CommunicationError.CE_ERROR_NO_XXXX
    public int                  mCmdSeq;    // command sequence
    public IApiArgs.IArgsBase   mArgs;      // command arguments

    public CmdExecRes(int err, int seq, IApiArgs.IArgsBase args) {
        mError  = err;
        mCmdSeq = seq;
        mArgs   = args;
    }

    @Override
    public String toString() {
        String str = super.toString() + "\n";
        str += String.format("\tmError: 0x%x (%d)\n", mError, mError);
        str += String.format("\tCmdSeq: 0x%x (%d)\n", mCmdSeq, mCmdSeq);
        str += String.format("\tArgs  : %s", mArgs.toString());
        return str;
    }
}
