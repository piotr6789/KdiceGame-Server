package Models;

import Logic.LoginLogic;

import java.io.*;

public class PlayerModel extends Thread
{
    private String _login;
    private int _id;
    private int _finalResult;
    private DataInputStream _inClient;
    private DataOutputStream _outClient;
    private boolean _isEliminated;
    private int roundPoints;
    private int roundPlace;

    public PlayerModel(int id, DataInputStream inputStream, DataOutputStream outputStream) {
        _id = id;
        _inClient = inputStream;
        _outClient = outputStream;
        _isEliminated = false;
        roundPoints = 0;
    }

    @Override
    public void run() {
        LoginLogic loginLogic = new LoginLogic();
        try
        {
            set_login(loginLogic.Login(get_inClient(), get_outClient()));
        }
        catch(Exception io)
        {
            io.printStackTrace();
        }
    }

    String get_login() {
        return _login;
    }

    private void set_login(String _login) {
        this._login = _login;
    }

    public int get_id() {
        return _id;
    }

    int get_finalResult() {
        return _finalResult;
    }

    void set_finalResult(int _finalResult) {
        this._finalResult = _finalResult;
    }

    public DataInputStream get_inClient() {
        return _inClient;
    }

    public DataOutputStream get_outClient() {
        return _outClient;
    }

    public boolean is_isEliminated() {
        return _isEliminated;
    }

    void set_isEliminated(boolean _isEliminated) {
        this._isEliminated = _isEliminated;
    }

    int getRoundPoints() {
        return roundPoints;
    }

    void setRoundPoints(int roundPoints) {
        this.roundPoints = roundPoints;
    }

    int getRoundPlace() {
        return roundPlace;
    }

    void setRoundPlace(int roundPlace) {
        this.roundPlace = roundPlace;
    }

}
