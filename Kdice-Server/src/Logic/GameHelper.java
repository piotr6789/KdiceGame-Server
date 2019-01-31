package Logic;

import Models.PlayerModel;

import java.io.IOException;
import java.util.List;

class GameHelper
{
    static void Start(List<PlayerModel> playerList) throws IOException {
        for (PlayerModel player : playerList) {
            player.get_outClient().writeUTF("START " + player.get_id() + " 1");
        }
    }

    static void cleanBuffor(List<PlayerModel> playerList) throws IOException {
        for (PlayerModel player : playerList) {
            player.get_outClient().flush();
        }
    }


}
