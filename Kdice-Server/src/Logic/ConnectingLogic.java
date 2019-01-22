package Logic;

import Models.PlayerModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectingLogic
{
    private static List<PlayerModel> playerList = new ArrayList<>();

    public static void startConnect(ServerSocket serverSocket) throws IOException
    {
        int counter = 1;
        int id = 1;
        do {
            Socket clientSocket = null;
            try
            {
                clientSocket = serverSocket.accept();

                //Obtaining input and out streams
                DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

                //Create a new thread object
                PlayerModel newPlayer = new PlayerModel(id, clientSocket, inputStream, outputStream);
                playerList.add(newPlayer);

                //Invoking the start() method
                newPlayer.start();
                counter++;
                id++;
            }
            catch(Exception e)
            {
                clientSocket.close();
                e.printStackTrace();
            }

        }while(counter < 7);
    }
}
