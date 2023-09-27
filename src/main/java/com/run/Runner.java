package com.run;

import com.entity.Const;
import com.entity.Msg;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.concurrent.Executors;

public class Runner {
    public static void main(String[] args) {
        String function = args.length > 0 ? args[0] : "";
        String fileName = args.length > 1 ? args[1] : "";
        if (StringUtils.equals("center", function)) {
            Center center = new Center();
            center.startServer();
        }
        if (StringUtils.equals("uploader", function) && StringUtils.isNotEmpty(fileName)) {
            Peer peer = new Peer();
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        peer.startFileServer(Const.uploadPath + fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        peer.startMsgClient(new Msg(Const.MsgTypeRegister, Const.uploadPath + fileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        if (StringUtils.equals("downloader", function) && StringUtils.isNotEmpty(fileName)) {
            Peer peer = new Peer();
            Executors.newSingleThreadExecutor().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        peer.startMsgClient(new Msg(Const.MsgTypeSearch, fileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
