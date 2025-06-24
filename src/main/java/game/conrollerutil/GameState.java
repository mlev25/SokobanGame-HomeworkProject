package game.conrollerutil;

import java.util.ArrayList;
import java.util.List;

public class GameState {
    public int playerRow;
    public int playerCol;
    public List<Coordinate> ballPositions;

    public GameState(int playerRow, int playerCol, List<Coordinate> ballPositions) {
        this.playerRow = playerRow;
        this.playerCol = playerCol;
        this.ballPositions = new ArrayList<>(ballPositions);
    }
}
