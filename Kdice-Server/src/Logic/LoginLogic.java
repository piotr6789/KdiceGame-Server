package Logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class LoginLogic
{

    public String Login(DataInputStream inputStream, DataOutputStream outputStream) throws IOException
    {
        outputStream.writeUTF("POLACZONO");

        String login = inputStream.readUTF();

        return getLogin(login);
    }

    private String getLogin(String login)
    {
        return login.substring(login.indexOf(" ") + 1);
    }
}
