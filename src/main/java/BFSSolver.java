import org.tinylog.Logger;
import puzzle.solver.BreadthFirstSearch;
import puzzle.solver.Node;
import util.Moves;

import java.util.Optional;

public class BFSSolver {
    public static void main(String[] args) {
        Logger.info("The Sokoban BFS solver has been started!");
        SokobanState initial = new SokobanState();
        BreadthFirstSearch<Moves> breadthFirstSearchSolver = new BreadthFirstSearch<>();

        Logger.info("The breadth first search solver algorithm has started!");
        Optional<Node<Moves>> solution = breadthFirstSearchSolver.solveAndPrintSolution(initial);

        if (solution.isPresent()){
            Logger.info("Solution found!");
        }else{
            Logger.warn("No solution found for the game");
        }
        Logger.info("The Sokoban solver application has finished!");
    }
}
