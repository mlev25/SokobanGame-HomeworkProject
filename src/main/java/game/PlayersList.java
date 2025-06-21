package game;

import java.util.ArrayList;
import java.util.List;

public class PlayersList {
    private List<Player> playerList = new ArrayList<>();

    public List<Player> getPlayers() {
        return playerList;
    }

    public void setPlayers(List<Player> playerList) {
        this.playerList = playerList;
    }

    public void addPlayer(Player player){
        playerList.add(player);
    }
}
