package com.entity;

import com.google.gson.Gson;

public class PeerId {
    private String ip;
    private int port;
    private String path;

    public PeerId(String ip, int port, String path) {
        this.ip = ip;
        this.port = port;
        this.path = path;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static PeerId decode(String peer){
        return new Gson().fromJson(peer, PeerId.class);
    }
}
