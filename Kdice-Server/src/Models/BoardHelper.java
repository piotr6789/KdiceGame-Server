package Models;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class BoardHelper
{
    private static FieldModel[][] _boardModel = new FieldModel[5][5];
    private static List<PlayerModel> playerList;
    private static List<Integer> attackerCubesValues = new ArrayList<>();
    private static List<Integer> defenderCubesValues = new ArrayList<>();
    private static int attackerID;
    private static int defenderID;
    private static int winner = 0;
    private static int summaryAttacker = 0;
    private static int summaryDefender = 0;


    public BoardHelper(List<PlayerModel> playerList)
    {
        BoardHelper.playerList = playerList;

    }

    public static void clearPoints(List<PlayerModel> playerList){
        for(PlayerModel player : playerList){
            player.setRoundPoints(0);
        }for(PlayerModel player : playerList){
            player.setRoundPlace(0);
        }
    }

    public static void printFinalResult(List<PlayerModel> playerList) throws IOException {
        StringBuilder wynik = new StringBuilder("WYNIK ");
        playerList.sort(Comparator.comparing(PlayerModel::get_finalResult));
        for(PlayerModel player : playerList){
            wynik.append(player.get_login()).append(" ").append(player.get_finalResult()).append(" ");
        }
        for(PlayerModel playerModel : playerList){
            playerModel.get_outClient().writeUTF(wynik.toString());
        }
    }

    public static void addFinalPoints(List<PlayerModel> playerList){
        for(PlayerModel playerModel : playerList){
            playerModel.set_finalResult(playerModel.get_finalResult() + playerModel.getRoundPoints());
        }
    }

    public static void printRoundResults(List<PlayerModel> playerList, int gameCount) throws IOException {
        for(PlayerModel player : playerList){
            if(player.getRoundPlace() == 0){
                player.setRoundPoints(2);
                player.setRoundPlace(2);
            }
            player.get_outClient().writeUTF("TURA " + (gameCount - 1) + " " + player.getRoundPlace());
        }
    }

    public static void roundResults(PlayerModel playerModel, List<PlayerModel> playerList){
        int checker = 0;
        if(playerModel.getRoundPoints() == 0 && playerModel.is_isEliminated()){
            for(PlayerModel player : playerList){
                if(player.get_id() != playerModel.get_id()){
                    if(player.getRoundPoints() != 0){
                        checker++;
                    }
                }
            }
            if(checker == 0){
                playerModel.setRoundPoints(5);
            }
            if(checker == 1){
                playerModel.setRoundPoints(4);
            }
            if(checker == 2){
                playerModel.setRoundPoints(3);
            }
            if(checker == 3){
                playerModel.setRoundPoints(2);
            }
            if(checker == 4){
                playerModel.setRoundPoints(1);
            }
        }
    }

    public static void roundPlaces(List<PlayerModel> playerList){
        for(PlayerModel player : playerList){
            if(player.getRoundPoints() == 1){
                player.setRoundPlace(1);
            }
            if(player.getRoundPoints() == 2){
                player.setRoundPlace(2);
            }
            if(player.getRoundPoints() == 3){
                player.setRoundPlace(3);
            }
            if(player.getRoundPoints() == 4){
                player.setRoundPlace(4);
            }
            if(player.getRoundPoints() == 5){
                player.setRoundPlace(5);
            }
        }
    }

    public static boolean checkPlayers(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(_boardModel[1][1].getOwnerId() != _boardModel[i][j].getOwnerId()){
                    return true;
                }
            }
        }
        for(PlayerModel players : playerList){
            if(players.get_id() == _boardModel[3][3].getOwnerId()){
                players.setRoundPoints(1);
                players.setRoundPlace(1);
            }
        }
        return false;
    }
    public static void checkEliminates(PlayerModel player){
        player.set_isEliminated(true);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(_boardModel[i][j].getOwnerId() == player.get_id()){
                    player.set_isEliminated(false);
                }
            }
        }
    }

    public static void clearEliminates(){
        for(PlayerModel player : playerList){
            player.set_isEliminated(false);
        }
    }

    private static void generateRandomCubes()
    {
        int x = new Random().nextInt(5);
        int y = new Random().nextInt(5);
        int counter = 1;

        while(counter < 6)
        {
            while(_boardModel[x][y].getCubesCount() != 0)
            {
                x = new Random().nextInt(5);
                y = new Random().nextInt(5);
            }
            int numberOfCubes = new Random().nextInt(5);
            _boardModel[x][y].setCubesCount(numberOfCubes);
            counter++;
        }
    }

    public static void setUpPlayersAndCubes()
    {
        int x = new Random().nextInt(5);
        int y = new Random().nextInt(5);
        int newOwnerId = 1;

        while(newOwnerId < 6)
        {
            while(_boardModel[x][y].getOwnerId() != 0)
            {
                x = new Random().nextInt(5);
                y = new Random().nextInt(5);
            }
            _boardModel[x][y].setOwnerId(newOwnerId);
            _boardModel[x][y].setCubesCount(2);
            newOwnerId++;
        }

        generateRandomCubes();
    }

    public static void createBoard()
    {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                _boardModel[i][j] = new FieldModel();
            }
        }
    }

    public static void printBoard() throws IOException {

        for (PlayerModel player : playerList) {
            if(!player.is_isEliminated())
            for (int i = 0; i < 5 ; i++) {
                for (int j = 0 ; j < 5 ; j++) {
                    player.get_outClient().writeUTF("PLANSZA " + i + " " + j + " " + _boardModel[i][j].getOwnerId() + " " + _boardModel[i][j].getCubesCount());
                }
            }
        }
    }

    public static void updateBoardAfterAttack(String command, int ID){
        attackerCubesValues.clear();
        defenderCubesValues.clear();

        setAttackerID(0);
        setDefenderID(0);


        String replaceString = command.replaceAll("\\s","");
        String finalString = replaceString.replaceAll("[^0-9.]", "");
        int totalValueAttacker = 0;
        int totalValueDeffender = 0;

        setAttackerID(_boardModel[Character.getNumericValue(finalString.charAt(0))][Character.getNumericValue(finalString.charAt(1))].getOwnerId());
        setDefenderID(_boardModel[Character.getNumericValue(finalString.charAt(2))][Character.getNumericValue(finalString.charAt(3))].getOwnerId());

        for(int i = 0; i < _boardModel[Character.getNumericValue(finalString.charAt(0))][Character.getNumericValue(finalString.charAt(1))].getCubesCount(); i++){
            int random = new Random().nextInt(6) + 1;
            totalValueAttacker = totalValueAttacker + random;
            attackerCubesValues.add(random);
        }

        for(int i = 0; i < _boardModel[Character.getNumericValue(finalString.charAt(2))][Character.getNumericValue(finalString.charAt(3))].getCubesCount(); i++){
            int random = new Random().nextInt(6) + 1;
            totalValueDeffender = totalValueDeffender + random;
            defenderCubesValues.add(random);
        }



        if(totalValueAttacker > totalValueDeffender){
            _boardModel[Character.getNumericValue(finalString.charAt(2))][Character.getNumericValue(finalString.charAt(3))].setOwnerId(ID);
            _boardModel[Character.getNumericValue(finalString.charAt(2))][Character.getNumericValue(finalString.charAt(3))]
                    .setCubesCount(_boardModel[Character.getNumericValue(finalString.charAt(0))][Character.getNumericValue(finalString.charAt(1))].getCubesCount() - 1);

            _boardModel[Character.getNumericValue(finalString.charAt(0))][Character.getNumericValue(finalString.charAt(1))].setCubesCount(1);
        }
        if(totalValueAttacker < totalValueDeffender){
            _boardModel[Character.getNumericValue(finalString.charAt(0))][Character.getNumericValue(finalString.charAt(1))].setCubesCount(1);
        }



        setSummaryAttacker(0);
        setSummaryDefender(0);

        for(Integer values : attackerCubesValues){
            setSummaryAttacker(getSummaryAttacker() + values);
        }

        for(Integer values : defenderCubesValues){
            setSummaryDefender(getSummaryDefender() + values);
        }

        if(summaryAttacker > summaryDefender){
            setWinner(_boardModel[Character.getNumericValue(finalString.charAt(0))][Character.getNumericValue(finalString.charAt(1))].getOwnerId());
        }

        if(summaryDefender > summaryAttacker){
            setWinner(_boardModel[Character.getNumericValue(finalString.charAt(2))][Character.getNumericValue(finalString.charAt(3))].getOwnerId());
        }
    }


    public static void addCubesAfterRound(){
        int counter1 = 0;
        int counter2 = 0;
        int counter3 = 0;
        int counter4 = 0;
        int counter5 = 0;

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if(_boardModel[i][j].getOwnerId() == 1){
                    counter1++;
                }
                if(_boardModel[i][j].getOwnerId() == 2){
                    counter2++;
                }
                if(_boardModel[i][j].getOwnerId() == 3){
                    counter3++;
                }
                if(_boardModel[i][j].getOwnerId() == 4){
                    counter4++;
                }
                if(_boardModel[i][j].getOwnerId() == 5){
                    counter5++;
                }
            }

            for (int k = 0; k < 5; k++) {
                for (int l = 0; l < 5; l++) {
                    if(_boardModel[k][l].getOwnerId() == 1){
                        if(counter1 > 0){
                            if(_boardModel[k][l].getCubesCount() < 8){
                                _boardModel[k][l].setCubesCount(_boardModel[k][l].getCubesCount() + 1);
                                counter1--;
                            }
                        }
                    }
                }
            }

            for (int k = 0; k < 5; k++) {
                for (int l = 0; l < 5; l++) {
                    if(_boardModel[k][l].getOwnerId() == 2){
                        if(counter2 > 0){
                            if(_boardModel[k][l].getCubesCount() < 8){
                                _boardModel[k][l].setCubesCount(_boardModel[k][l].getCubesCount() + 1);
                                counter2--;
                            }
                        }
                    }
                }
            }

            for (int k = 0; k < 5; k++) {
                for (int l = 0; l < 5; l++) {
                    if(_boardModel[k][l].getOwnerId() == 3){
                        if(counter3 > 0){
                            if(_boardModel[k][l].getCubesCount() < 8){
                                _boardModel[k][l].setCubesCount(_boardModel[k][l].getCubesCount() + 1);
                                counter3--;
                            }
                        }
                    }
                }
            }

            for (int k = 0; k < 5; k++) {
                for (int l = 0; l < 5; l++) {
                    if(_boardModel[k][l].getOwnerId() == 4){
                        if(counter4 > 0){
                            if(_boardModel[k][l].getCubesCount() < 8){
                                _boardModel[k][l].setCubesCount(_boardModel[k][l].getCubesCount() + 1);
                                counter4--;
                            }
                        }
                    }
                }
            }

            for (int k = 0; k < 5; k++) {
                for (int l = 0; l < 5; l++) {
                    if(_boardModel[k][l].getOwnerId() == 5){
                        if(counter5 > 0){
                            if(_boardModel[k][l].getCubesCount() < 8){
                                _boardModel[k][l].setCubesCount(_boardModel[k][l].getCubesCount() + 1);
                                counter5--;
                            }
                        }
                    }
                }
            }
        }
    }

    public static String attackResult(){
        StringBuilder result = new StringBuilder("WYNIK " + getAttackerID() + " " + getAttackerCubesValues().size() + " ");
        for(Integer valuesAttacker : attackerCubesValues){
            result.append(valuesAttacker).append(" ");
        }
        result.append(getDefenderID()).append(" ").append(getDefenderCubesValues().size()).append(" ");

        for(Integer valuesDefender : defenderCubesValues){
            result.append(valuesDefender).append(" ");
        }
        return result.toString() + getWinner();
    }

    private static List<Integer> getAttackerCubesValues() {
        return attackerCubesValues;
    }


    private static List<Integer> getDefenderCubesValues() {
        return defenderCubesValues;
    }


    private static int getAttackerID() {
        return attackerID;
    }

    private static void setAttackerID(int attackerID) {
        BoardHelper.attackerID = attackerID;
    }

    private static int getDefenderID() {
        return defenderID;
    }

    private static void setDefenderID(int defenderID) {
        BoardHelper.defenderID = defenderID;
    }

    private static int getWinner() {
        return winner;
    }

    private static void setWinner(int winner) {
        BoardHelper.winner = winner;
    }

    private static int getSummaryAttacker() {
        return summaryAttacker;
    }

    private static void setSummaryAttacker(int summaryAttacker) {
        BoardHelper.summaryAttacker = summaryAttacker;
    }

    private static int getSummaryDefender() {
        return summaryDefender;
    }

    private static void setSummaryDefender(int summaryDefender) {
        BoardHelper.summaryDefender = summaryDefender;
    }


}
