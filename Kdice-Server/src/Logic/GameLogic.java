package Logic;

import Models.PlayerModel;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class GameLogic
{
    static void Start(List<PlayerModel> playerList) throws IOException {
        for (PlayerModel player : playerList) {
            player.get_outClient().writeUTF("START " + player.get_id() + " 1");
            player.set_isReady(false);
        }
    }

    public static void cleanBuffor(List<PlayerModel> playerList) throws IOException {
        for (PlayerModel player : playerList) {
            player.get_outClient().flush();
        }
    }
}
