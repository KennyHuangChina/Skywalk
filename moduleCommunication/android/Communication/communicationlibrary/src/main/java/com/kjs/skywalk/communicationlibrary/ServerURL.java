package com.kjs.skywalk.communicationlibrary;

class ServerURL {
    //public static final String mServerUri= "http://ub1604-skywalk-:8080";
    //public static final String mServerUri= "http://192.168.31.239:8080";
    public static final String mPrefix = "http";
    public static final String mServer = "192.168.0.75";
    public static final String mPort = "8080";
    public static final String mServerUri= mPrefix + "://" + mServer + ":" + mPort;
}
