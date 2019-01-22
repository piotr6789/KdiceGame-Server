package Models;

import Logic.GameLogic;
import Logic.LoginLogic;

import java.io.*;
import java.net.Socket;

public class PlayerModel extends Thread
{
    private String _login;
    private int _id;
    private int _currentResult;
    private int _finalResult;
    private DataInputStream _inClient;
    private DataOutputStream _outClient;
    private boolean _isReady;
    private Socket _clientSocket;

    public PlayerModel(int id, Socket clientSocket, DataInputStream inputStream, DataOutputStream outputStream) throws IOException
    {
        _id = id;
        _clientSocket = clientSocket;
        _inClient = inputStream;
        _outClient = outputStream;
        _isReady = false;
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
        set_isReady(true);

        GameLogic gameLogic = new GameLogic();
        try
        {
            gameLogic.Start(get_inClient(), get_outClient(), get_id());
        }
        catch(Exception io)
        {
            io.printStackTrace();
        }
    }

    public String get_login() {
        return _login;
    }

    public void set_login(String _login) {
        this._login = _login;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_currentResult() {
        return _currentResult;
    }

    public void set_currentResult(int _currentResult) {
        this._currentResult = _currentResult;
    }

    public int get_finalResult() {
        return _finalResult;
    }

    public void set_finalResult(int _finalResult) {
        this._finalResult = _finalResult;
    }

    public DataInputStream get_inClient() {
        return _inClient;
    }

    public void set_inClient(DataInputStream _inClient) {
        this._inClient = _inClient;
    }

    public DataOutputStream get_outClient() {
        return _outClient;
    }

    public void set_outClient(DataOutputStream _outClient) {
        this._outClient = _outClient;
    }

    public boolean is_isReady() {
        return _isReady;
    }

    public void set_isReady(boolean _isReady) {
        this._isReady = _isReady;
    }

    public Socket get_clientSocket() {
        return _clientSocket;
    }

    public void set_clientSocket(Socket _clientSocket) {
        this._clientSocket = _clientSocket;
    }
}