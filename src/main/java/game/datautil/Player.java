package game.datautil;

public class Player {
    private String name;
    private int score;

    public Player(){}

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Player: ");
        sb.append(name).append(", ").append(score);
        return sb.toString();
    }
}
