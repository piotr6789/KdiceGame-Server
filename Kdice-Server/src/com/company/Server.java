package com.company;

import Logic.MainLogic;
import java.io.IOException;
import java.net.ServerSocket;

public class Server
{
    public static void main(String[] args) throws IOException, InterruptedException {
        int PORT = 5056;
        ServerSocket serverSocket = new ServerSocket(PORT);
        MainLogic.startConnect(serverSocket);
    }
}
