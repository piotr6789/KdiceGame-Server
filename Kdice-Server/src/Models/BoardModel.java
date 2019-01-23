package Models;

import java.io.IOException;
import java.util.List;
import java.util.Random;

public class BoardModel
{
    private FieldModel[][] _boardModel = new FieldModel[5][5];
    private List<PlayerModel> playerList;

    public BoardModel(List<PlayerModel> playerList)
    {
        this.playerList = playerList;
        createBoard();
        setUpPlayers();
        generateRandomCubes();
    }

    private void generateRandomCubes()
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

    private void setUpPlayers()
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
    }

    private void createBoard()
    {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                _boardModel[i][j] = new FieldModel();
            }
        }
    }

    public void printBoard() throws IOException {

        for (PlayerModel player : playerList) {
            for (int i = 0; i < 5 ; i++) {
                for (int j = 0 ; j < 5 ; j++) {
                    player.get_outClient().writeUTF("PLANSZA " + i + " " + j + " " + _boardModel[i][j].getOwnerId() + " " + _boardModel[i][j].getCubesCount());
                }
            }
        }
    }

    public FieldModel[][] getBoardModel() {
        return _boardModel;
    }

    public void setBoardModel(FieldModel[][] _fieldModel) {
        this._boardModel = _fieldModel;
    }
}
