package com.company;

import Logic.ConnectingLogic;
import Models.PlayerModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class Server
{
    public static List<PlayerModel> playerList = new ArrayList<>();

    public static void main(String[] args) throws IOException, InterruptedException {
        int PORT = 5056;
        ServerSocket serverSocket = new ServerSocket(PORT);
        ConnectingLogic.startConnect(serverSocket);
    }
}
