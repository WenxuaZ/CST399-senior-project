package com.run;

import com.entity.Const;
import com.entity.Msg;
import com.entity.PeerId;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.util.MsgUtil;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Center {

    private Map<String, List<String>> fileMap = Maps.newHashMap();

    public void register(String peerId, String fileName) {
        System.out.println("register, peerId:" + peerId + ",fileName:" + fileName);
        if (!fileMap.containsKey(fileName)) {
            fileMap.put(fileName, new ArrayList<String>());
        }
        List<String> peers = fileMap.get(fileName);
        peers.add(peerId);
    }

    public List<String> search(String fileName) {
        System.out.println("register, fileName" + fileName);
        return fileMap.getOrDefault(fileName, Collections.emptyList());
    }

    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(Const.msgServerPort)) {
            System.out.println("wait client connection...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("client connected：" + clientSocket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                String message = in.readLine();
                System.out.println("client msg：" + message);
                processMsg(clientSocket.getInetAddress().toString(), message, out);

                clientSocket.close();
                System.out.println("client close");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void processMsg(String ip, String message, PrintWriter out) {
        Msg msg = Msg.decode(message);
        String msgBody = msg.getMsgBody();
        if (StringUtils.equalsIgnoreCase(msg.getMsgType(), Const.MsgTypeRegister)) {
            PeerId peerId = new PeerId(ip.substring(1), Const.fileServerPort, msgBody);
            register(peerId.toString(), MsgUtil.extractFileName(msgBody));
        }
        if (StringUtils.equalsIgnoreCase(msg.getMsgType(), Const.MsgTypeSearch)) {
            List<String> list = search(msgBody);
            Gson gson = new Gson();
            out.println(gson.toJson(new Msg(Const.MsgTypeSearchResult, gson.toJson(list))));
        }
    }
}
