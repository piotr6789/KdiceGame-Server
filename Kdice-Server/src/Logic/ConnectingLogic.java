package Logic;

import Models.BoardModel;
import Models.PlayerModel;

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

                while (gameCounter < 10) {
                    gameCounter++;
                    roundCounter = 0;
                    board = new BoardModel(playerList);
                    board.clearPoints(playerList);
                    board.createBoard();
                    board.setUpPlayersAndCubes();


                    Thread.sleep(2000);
                    while (roundCounter < 100) {


                        if(!board.checkPlayers(roundCounter)){
                            break;
                        }
                        board.clearEliminates();
                        for(PlayerModel playerModel : playerList){
                            board.checkEliminates(playerModel);
                        }
                        for (PlayerModel player : playerList) {
                            if(player.is_isEliminated()){
                                player.get_outClient().writeUTF("ELIMINACJA");
                            }
                        }
                        for (PlayerModel player : playerList) {
                            if(!player.is_isEliminated()){
                                player.get_outClient().writeUTF("START GRY");
                            }
                        }

                        board.printBoard();
                        GameLogic.cleanBuffor(playerList);


                        for (PlayerModel player : playerList) {
                            if(!player.is_isEliminated()){
                                player.get_outClient().writeUTF("TWOJ RUCH");
                                board.printBoard();
                                answerClient = player.get_inClient().readUTF();
                                while (answerClient.substring(0, 4).equals("ATAK")) {
                                    player.get_outClient().writeUTF("OK");
                                    for (PlayerModel players : playerList) {
                                        if (players.get_id() != player.get_id() && !players.is_isEliminated()) {
                                            players.get_outClient().writeUTF(answerClient);
                                        }
                                    }
                                    board.updateBoardAfterAttack(answerClient, player.get_id());
                                    board.printBoard();
                                    GameLogic.cleanBuffor(playerList);
                                    for(PlayerModel playerModel : playerList){
                                        if(!playerModel.is_isEliminated()){
                                            playerModel.get_outClient().writeUTF(board.attackResult());
                                        }
                                    }
                                    answerClient = player.get_inClient().readUTF();
                                }
                                if (answerClient.equals("PASS")) {
                                    player.get_outClient().writeUTF("PASS");
                                }
                            }
                        }
                        for (PlayerModel player : playerList) {
                            board.roundResults(player, playerList, roundCounter);
                        }
                        board.roundPlaces(playerList);
                        for (PlayerModel player : playerList) {
                            player.get_outClient().writeUTF("KONIEC RUNDY");
                        }

                        board.addCubesAfterRound();
//                    Thread.sleep(1000);
                        roundCounter++;
                    }
                    board.printRoundResults(playerList, roundCounter);
                    board.addFinalPoints(playerList);
                }

            }
            board.printFinalResult(playerList);
        }catch(Exception io){
            io.printStackTrace();
        }
    }
}
