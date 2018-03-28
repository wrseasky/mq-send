package com.baidu.rabbitTest.rpcmy;

public class Main {
    public static void main(String[] args) throws Exception {
        Client c = new Client();
        String shit = c.call("shit");
        System.out.println(shit);
    }
}
