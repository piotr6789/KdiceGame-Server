package Logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GameLogic
{
    public void Start(DataInputStream inputStream, DataOutputStream outputStream, int id) throws IOException
    {
        String startString = "START " + id + " " + 1;
        outputStream.writeUTF(startString);
    }
}
