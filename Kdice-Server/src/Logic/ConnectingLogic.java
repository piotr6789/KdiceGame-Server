package Logic;

import Models.BoardModel;
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
    public static List<PlayerModel> playerList = new ArrayList<>();
    public static int counter = 0;

    public static void startConnect(ServerSocket serverSocket) throws IOException
    {
        int id = 1;

        while(counter < 1) {
            counter++;
            if (playerList.size() < 5) {
                while (playerList.size() < 5) {
                    Socket clientSocket = null;
                    clientSocket = serverSocket.accept();

                    //Obtaining input and out streams
                    DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

                    //Create a new thread object
                    PlayerModel newPlayer = new PlayerModel(id, clientSocket, inputStream, outputStream);
                    playerList.add(newPlayer);

                    //Invoking the start() method
                    newPlayer.start();
                    id++;
                }
            }

            GameLogic gameLogic = new GameLogic();
            if(playerList.size() == 5 && playerList.stream().allMatch(PlayerModel::is_isReady)){
                try
                {
                    GameLogic.Start(playerList);
                    Thread.sleep(2000);
                }
                catch(Exception io)
                {
                    io.printStackTrace();
                }
            }


            BoardModel board = new BoardModel(playerList);
            board.printBoard();
            GameLogic.cleanBuffor(playerList);

            playerList.get(0).get_outClient().writeUTF("TWOJ RUCH");
            playerList.get(0).set_isReady(true);
            System.out.println(playerList.get(0).get_inClient().readUTF());
        }
    }
}
