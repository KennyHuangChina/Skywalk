package com.kjs.skywalk.communicationlibrary;

class ServerURL {
    //public static final String mServerUri= "http://ub1604-skywalk-:8080";
//    public static final String mServerUri= "http://192.168.31.239:8080";
    public static final String mProtocol = "http";
//    public static final String mHost = "skywalk-dev";
    public static final String mHost = "ks100-hq.eicp.net";
    public static final String mPort = "9099";
    public static final String mServerUri = mProtocol + "://" + mHost + ":" + mPort;
}
