package Logic;

import Models.BoardHelper;
import Models.PlayerModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.*;

public class MainLogic
{
    private static List<PlayerModel> playerList = new ArrayList<>();
    private static int counter = 0;
    private static int gameCounter = 1;

    public static void startConnect(ServerSocket serverSocket) throws IOException, InterruptedException {
        int id = 1;

        try {
            while (counter < 1) {
                counter++;
                if (playerList.size() < 5) {
                    while (playerList.size() < 5) {
                        Socket clientSocket;
                        clientSocket = serverSocket.accept();

                        //Obtaining input and out streams
                        DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                        DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

                        //Create a new thread object
                        PlayerModel newPlayer = new PlayerModel(id, inputStream, outputStream);
                        playerList.add(newPlayer);

                        //Invoking the start() method
                        newPlayer.start();
                        id++;
                    }
                }

                if (playerList.size() == 5) {
                    try {
                        GameHelper.Start(playerList);
                        Thread.sleep(1000);
                    } catch (Exception io) {
                        io.printStackTrace();
                    }
                }

                while (gameCounter < 11) {
                    gameCounter++;
                    int roundCounter = 0;
                    new BoardHelper(playerList);
                    BoardHelper.clearPoints(playerList);
                    BoardHelper.createBoard();
                    BoardHelper.setUpPlayersAndCubes();


                    Thread.sleep(2000);
                    while (roundCounter < 100) {


                        if(!BoardHelper.checkPlayers()){
                            break;
                        }
                        BoardHelper.clearEliminates();
                        for(PlayerModel playerModel : playerList){
                            BoardHelper.checkEliminates(playerModel);
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

                        BoardHelper.printBoard();
                        GameHelper.cleanBuffor(playerList);


                        for (PlayerModel player : playerList) {
                            if(!player.is_isEliminated()){
                                player.get_outClient().writeUTF("TWOJ RUCH");
                                BoardHelper.printBoard();
                                String answerClient = player.get_inClient().readUTF();
                                while (answerClient.substring(0, 4).startsWith("ATAK")) {
                                    player.get_outClient().writeUTF("OK");
                                    for (PlayerModel players : playerList) {
                                        if (players.get_id() != player.get_id() && !players.is_isEliminated()) {
                                            players.get_outClient().writeUTF(answerClient);
                                        }
                                    }
                                    BoardHelper.updateBoardAfterAttack(answerClient, player.get_id());
                                    BoardHelper.printBoard();
                                    GameHelper.cleanBuffor(playerList);
                                    for(PlayerModel playerModel : playerList){
                                        if(!playerModel.is_isEliminated()){
                                            playerModel.get_outClient().writeUTF(BoardHelper.attackResult());
                                        }
                                    }
                                    answerClient = player.get_inClient().readUTF();
                                }
                                if (answerClient.startsWith("PASS")) {
                                    player.get_outClient().writeUTF("PASS");
                                }
                            }
                        }
                        for (PlayerModel player : playerList) {
                            BoardHelper.roundResults(player, playerList);
                        }
                        BoardHelper.roundPlaces(playerList);
                        for (PlayerModel player : playerList) {
                            player.get_outClient().writeUTF("KONIEC RUNDY");
                        }

                        BoardHelper.addCubesAfterRound();
                        roundCounter++;
                    }
                    BoardHelper.printRoundResults(playerList, gameCounter);
                    BoardHelper.addFinalPoints(playerList);
                }

            }
            BoardHelper.printFinalResult(playerList);
        }catch(SocketException se){
            System.out.println("Klient przestal dzialac.");
        }
    }
}
