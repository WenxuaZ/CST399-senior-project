package com.util;

import com.google.common.base.Splitter;

import java.util.List;

public class MsgUtil {

    private static final Splitter spliter = Splitter.on('/');

    public static String extractFileName(String absPath){
        List<String> parts = spliter.splitToList(absPath);
        return parts.get(parts.size()-1);
    }

    public static void main(String[] args) {
        System.out.println(extractFileName("/Users/fanshuquan/p2p/upload/1.txt"));
    }
}
