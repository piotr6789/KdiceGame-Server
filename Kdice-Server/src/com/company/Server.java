package com.company;

import Logic.ConnectingLogic;
import java.io.IOException;
import java.net.ServerSocket;

public class Server
{

    public static void main(String[] args) throws IOException
    {
        int PORT = 5056;
        ServerSocket serverSocket = new ServerSocket(PORT);
        ConnectingLogic.startConnect(serverSocket);
    }
}
