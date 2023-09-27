package com.run;

import com.entity.Const;
import com.entity.Msg;
import com.entity.PeerId;
import com.google.common.base.Stopwatch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.util.MsgUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Peer {

    private void processMsg(String message) throws IOException {
        if (StringUtils.isEmpty(message)){
            return;
        }
        Msg msg = Msg.decode(message);
        String msgBody = msg.getMsgBody();
        if (StringUtils.equalsIgnoreCase(msg.getMsgType(), Const.MsgTypeSearchResult)) {
            List<String> list = new Gson().fromJson(msgBody, new TypeToken<List<String>>() {
            }.getType());
            if (CollectionUtils.isNotEmpty(list)) {
                String peer = list.get(0);
                PeerId peerId = PeerId.decode(peer);
                startFileClient(peerId);
            }
        }
    }

    public void startMsgClient(Msg msg) throws IOException {
        Socket socket = new Socket(Const.centerIp, Const.msgServerPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        out.println(msg.toString());

        String response = in.readLine();
        System.out.println("response from server：" + response);
        processMsg(response);
        socket.close();
    }

    public void startFileServer(String filePath) throws IOException {
        ServerSocket serverSocket = new ServerSocket(Const.fileServerPort);

        System.out.println("wait client connection...");
        Socket clientSocket = serverSocket.accept();

        System.out.println("client connected");

        FileInputStream fileInputStream = new FileInputStream(filePath);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        OutputStream outputStream = clientSocket.getOutputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            outputStream.flush();
        }

        System.out.println("file transfer finished");

        bufferedInputStream.close();
        outputStream.close();
        clientSocket.close();
        serverSocket.close();
    }

    public void startFileClient(PeerId peerId) throws IOException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Socket clientSocket = new Socket(peerId.getIp(), peerId.getPort());
        FileOutputStream fileOutputStream = new FileOutputStream(Const.downloadPath + MsgUtil.extractFileName(peerId.getPath()));
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        InputStream inputStream = clientSocket.getInputStream();

        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            bufferedOutputStream.write(buffer, 0, bytesRead);
            bufferedOutputStream.flush();
        }

        System.out.println("file download finished");

        bufferedOutputStream.close();
        inputStream.close();
        clientSocket.close();
        stopwatch.stop();
        System.out.println("file download duration(MILLISECONDS):"+ stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
