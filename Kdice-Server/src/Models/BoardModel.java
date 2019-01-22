package Models;

import java.util.Random;

public class BoardModel
{
    private FieldModel[][] _boardModel = new FieldModel[5][5];

    public BoardModel()
    {
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
            for (int j = 0; i < 5; j++) {
                _boardModel[i][j] = new FieldModel();
            }
        }
    }

    public void printBoard()
    {
        for (int i = 0; i < 5 ; i++) {
            for (int j = 0 ; j < 5 ; j++) {
                System.out.println("Owner: " + _boardModel[i][j].getOwnerId() + " CUBES: " + _boardModel[i][j].getCubesCount());
            }
            System.out.println();
        }
    }

    public FieldModel[][] getBoardModel() {
        return _boardModel;
    }

    public void setBoardModel(FieldModel[][] _fieldModel) {
        this._boardModel = _fieldModel;
    }
}
