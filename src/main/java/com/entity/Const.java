package com.entity;

public class Const {

    //Center ip address, this should be change to your real server ip address
    public static final String centerIp = "119.45.168.33";

    //Download file path, folder should be created
    public static final String downloadPath = "/home/ubuntu/p2p/download/";

    //Upload file path, folder should be created
    public static final String uploadPath = "/home/ubuntu/p2p/upload/";

    //Used to open socket port and transfer message
    public static final int msgServerPort = 11111;

    //Used to open socket port and transfer file
    public static final int fileServerPort = 11112;

    //[Don't change] Used to define register message
    public static final String MsgTypeRegister = "register";

    //[Don't change] Used to define search message
    public static final String MsgTypeSearch = "search";

    //[Don't change] Used to define search result message
    public static final String MsgTypeSearchResult = "searchResult";
}
