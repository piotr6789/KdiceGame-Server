package Logic;

import Models.BoardModel;
import Models.PlayerModel;
import org.omg.PortableServer.THREAD_POLICY_ID;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class ConnectingLogic
{
    public static List<PlayerModel> playerList = new ArrayList<>();
    public static int counter = 0;
    public static BoardModel board;
    public static int roundCounter = 0;
    public static int gameCounter = 0;
    public static String answerClient = "";
    public static int randomStarter;

    public static void startConnect(ServerSocket serverSocket) throws IOException, InterruptedException {
        int id = 1;

        try {
            while (counter < 1) {
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

                if (playerList.size() == 5) {
                    try {
                        GameLogic.Start(playerList);
                        Thread.sleep(1000);
                    } catch (Exception io) {
                        io.printStackTrace();
                    }
                }
                List<PlayerModel> randomList = new ArrayList<>();
                randomList = playerList;

                while (gameCounter < 10) {
                    gameCounter++;
                    roundCounter = 0;
                    board = new BoardModel(playerList);
                    board.createBoard();
                    board.setUpPlayersAndCubes();

                    Collections.shuffle(randomList);

                    Thread.sleep(2000);
                    while (roundCounter < 100) {

                        for (PlayerModel player : randomList) {
                            player.get_outClient().writeUTF("START GRY");
                        }
                        board.printBoard();
                        GameLogic.cleanBuffor(playerList);


                        for (PlayerModel player : playerList) {
                            player.get_outClient().writeUTF("TWOJ RUCH");
                            board.printBoard();
                            answerClient = player.get_inClient().readUTF();
                            while (answerClient.substring(0, 4).equals("ATAK")) {
                                player.get_outClient().writeUTF("OK");
                                for (PlayerModel players : playerList) {
                                    if (players.get_id() != player.get_id()) {
                                        players.get_outClient().writeUTF(answerClient);
                                    }
                                }
                                board.updateBoardAfterAttack(answerClient, player.get_id());
                                board.printBoard();
                                GameLogic.cleanBuffor(playerList);
                                for(PlayerModel playerModel : playerList){
                                    playerModel.get_outClient().writeUTF(board.attackResult());
                                }
                                answerClient = player.get_inClient().readUTF();
                            }
                            if (answerClient.equals("PASS")) {
                                player.get_outClient().writeUTF("PASS");
                                continue;
                            }
                        }

                            for (PlayerModel player : playerList) {
                                player.get_outClient().writeUTF("KONIEC RUNDY");
                            }

                        board.addCubesAfterRound();
//                    Thread.sleep(1000);
                        roundCounter++;
                    }
                }

            }
            for (PlayerModel player : playerList) {
                player.get_outClient().writeUTF("KONIEC GRY");
            }
        }catch(Exception io){
            io.printStackTrace();
        }
    }
}
