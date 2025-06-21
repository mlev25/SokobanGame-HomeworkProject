import puzzle.State;
import util.Moves;
import util.Position;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SokobanState implements State<Moves> {
    private Position characterPosition;

    private Position ball1Position;
    private Position ball2Position;
    private Position ball3Position;

    private Set<Position> goalPositions;

    private static char[][] field = {
            {'*', '*', '*', '*', '*', '*', '*', '*', '*'},
            {'*', ' ', ' ', ' ', '*', '*', '*', '*', '*'},
            {'*', ' ', ' ', ' ', '*', '*', '*', '*', '*'},
            {'*', ' ', ' ', ' ', '*', '*', '*', ' ', '*'},
            {'*', '*', '*', ' ', '*', '*', '*', ' ', '*'},
            {'*', '*', '*', ' ', ' ', ' ', ' ', ' ', '*'},
            {'*', '*', ' ', ' ', ' ', '*', ' ', ' ', '*'},
            {'*', '*', ' ', ' ', ' ', '*', '*', '*', '*'},
            {'*', '*', '*', '*', '*', '*', '*', '*', '*'},
    };

    public SokobanState(){
        this.characterPosition = new Position(1,1);
        this.ball1Position = new Position(2,2);
        this.ball2Position = new Position(2,3);
        this.ball3Position = new Position(3,2);
        this.goalPositions = Set.of(new Position(3,7), new Position(4,7), new Position(5,7));
    }

    private boolean isWall(Position pos) {
        return field[pos.row()][pos.column()] == '*';
    }

    @Override
    public boolean isSolved() {
        return goalPositions.containsAll(Set.of(ball1Position, ball2Position, ball3Position));
    }

    @Override
    public Set<Moves> getLegalMoves() {
        var moves = new HashSet<Moves>();
        for (Moves move : Moves.values()) {
            if (isLegalMove(move)){
                moves.add(move);
            }
        }
        return moves;
    }

    @Override
    public State<Moves> clone() {
        SokobanState copy = new SokobanState();
        copy.characterPosition = new Position(characterPosition.row(), characterPosition.column());
        copy.ball1Position = new Position(ball1Position.row(), ball1Position.column());
        copy.ball2Position = new Position(ball2Position.row(), ball2Position.column());
        copy.ball3Position = new Position(ball3Position.row(), ball3Position.column());
        copy.goalPositions = new HashSet<>(goalPositions);
        return copy;
    }

    @Override
    public boolean isLegalMove(Moves move) {
        Position nextPossiblePosition = characterPosition.move(move);
        if (isWall(nextPossiblePosition)){
            return false;
        }

        if (nextPossiblePosition.equals(ball1Position) || nextPossiblePosition.equals(ball2Position) || nextPossiblePosition.equals(ball3Position)){
            Position possiblePositionOfBall = nextPossiblePosition.move(move);

            if (isWall(possiblePositionOfBall)){
                return false;
            }

            return !possiblePositionOfBall.equals(ball1Position) && !possiblePositionOfBall.equals(ball2Position) && !possiblePositionOfBall.equals(ball3Position);
        }

        return true;
    }

    @Override
    public void makeMove(Moves move) {
        Position nextPossiblePosition = characterPosition.move(move);

        if (nextPossiblePosition.equals(ball1Position)){
            ball1Position = ball1Position.move(move);
        } else if (nextPossiblePosition.equals(ball2Position)) {
            ball2Position = ball2Position.move(move);
        } else if (nextPossiblePosition.equals(ball3Position)) {
            ball3Position = ball3Position.move(move);
        }

        characterPosition = nextPossiblePosition;
    }

    @Override
    public String toString(){
        char[][] temp = new char[field.length][field[0].length];
        for (int i = 0; i < field.length; i++) {
            temp[i] = field[i].clone();
        }

        temp[characterPosition.row()][characterPosition.column()] = 'C';
        temp[ball1Position.row()][ball1Position.column()] = '0';
        temp[ball2Position.row()][ball2Position.column()] = '0';
        temp[ball3Position.row()][ball3Position.column()] = '0';

        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (char[] row : temp) {
            for (char c : row) {
                sb.append("  ").append(c).append("  ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SokobanState that = (SokobanState) o;
        return characterPosition.equals(that.characterPosition) &&
                Set.of(ball1Position, ball2Position, ball3Position)
                        .equals(Set.of(that.ball1Position, that.ball2Position, that.ball3Position));
    }

    @Override
    public int hashCode() {
        return Objects.hash(characterPosition,
                Set.of(ball1Position, ball2Position, ball3Position));
    }
}
