package Models;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BoardModel
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


    public BoardModel(List<PlayerModel> playerList)
    {
        this.playerList = playerList;

    }

    public static void generateRandomCubes()
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
        String result = "WYNIK " + getAttackerID() + " " + getAttackerCubesValues().size() + " ";
        for(Integer valuesAttacker : attackerCubesValues){
            result = result + valuesAttacker + " ";
        }
        result = result + getDefenderID() + " " + getDefenderCubesValues().size() + " ";

        for(Integer valuesDefender : defenderCubesValues){
            result = result + valuesDefender + " ";
        }
        return result + getWinner();
    }

    public static List<Integer> getAttackerCubesValues() {
        return attackerCubesValues;
    }

    public static void setAttackerCubesValues(List<Integer> attackerCubesValues) {
        BoardModel.attackerCubesValues = attackerCubesValues;
    }

    public static List<Integer> getDefenderCubesValues() {
        return defenderCubesValues;
    }

    public static void setDefenderCubesValues(List<Integer> defenderCubesValues) {
        BoardModel.defenderCubesValues = defenderCubesValues;
    }

    public static int getAttackerID() {
        return attackerID;
    }

    public static void setAttackerID(int attackerID) {
        BoardModel.attackerID = attackerID;
    }

    public static int getDefenderID() {
        return defenderID;
    }

    public static void setDefenderID(int defenderID) {
        BoardModel.defenderID = defenderID;
    }

    public static int getWinner() {
        return winner;
    }

    public static void setWinner(int winner) {
        BoardModel.winner = winner;
    }

    public static int getSummaryAttacker() {
        return summaryAttacker;
    }

    public static void setSummaryAttacker(int summaryAttacker) {
        BoardModel.summaryAttacker = summaryAttacker;
    }

    public static int getSummaryDefender() {
        return summaryDefender;
    }

    public static void setSummaryDefender(int summaryDefender) {
        BoardModel.summaryDefender = summaryDefender;
    }


    public FieldModel[][] getBoardModel() {
        return _boardModel;
    }

    public void setBoardModel(FieldModel[][] _fieldModel) {
        this._boardModel = _fieldModel;
    }
}
