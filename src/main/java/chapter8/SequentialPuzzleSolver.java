package chapter8;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * SequentialPuzzleSolver
 * <p/>
 * Sequential puzzle solver
 *
 */
public class SequentialPuzzleSolver<P, M> {
    private final Puzzle<P, M> puzzle;
    private final Set<P> seen = new HashSet<>();

    public SequentialPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
    }

    public List<M> solve() {
        P position = puzzle.initialPosition();
        return search(new PuzzleNode<>(position, null, null));
    }

    private List<M> search(PuzzleNode<P, M> currentNode) {
        P currentPosition = currentNode.getPosition();
        if (!seen.contains(currentPosition)) {
            seen.add(currentPosition);

            if (puzzle.isGoal(currentPosition)) {
                return currentNode.asMoveList();
            }

            for (M move : puzzle.legalMoves(currentPosition)) {
                P nextPosition = puzzle.move(currentPosition, move);
                PuzzleNode<P, M> nextNode = new PuzzleNode<>(nextPosition, move, currentNode);
                List<M> result = search(nextNode);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
