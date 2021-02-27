package chapter8;

import common.ValueLatch;

import java.util.List;
import java.util.concurrent.*;

/**
 * ConcurrentPuzzleSolver
 * <p/>
 * Concurrent version of puzzle solver
 *
 */
public class ConcurrentPuzzleSolver <P, M> {
    private final Puzzle<P, M> puzzle;
    private final ExecutorService exec;
    private final ConcurrentMap<P, Boolean> seen;
    protected final ValueLatch<PuzzleNode<P, M>> solution = new ValueLatch<>();

    public ConcurrentPuzzleSolver(Puzzle<P, M> puzzle) {
        this.puzzle = puzzle;
        this.exec = initThreadPool();
        this.seen = new ConcurrentHashMap<>();
        if (exec instanceof ThreadPoolExecutor) {
            ThreadPoolExecutor tpe = (ThreadPoolExecutor) exec;
            tpe.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());
        }
    }

    private ExecutorService initThreadPool() {
        return Executors.newCachedThreadPool();
    }

    public List<M> solve() throws InterruptedException {
        try {
            P position = puzzle.initialPosition();
            exec.execute(newTask(position, null, null));
            // block until solution found
            PuzzleNode<P, M> solutionPuzzleNode = solution.getValue();
            return (solutionPuzzleNode == null) ? null : solutionPuzzleNode.asMoveList();
        } finally {
            exec.shutdown();
        }
    }

    protected Runnable newTask(P position, M move, PuzzleNode<P, M> node) {
        return new SolverTask(position, move, node);
    }

    protected class SolverTask extends PuzzleNode<P, M> implements Runnable {

        public SolverTask(P position, M move, PuzzleNode<P, M> prev) {
            super(position, move, prev);
        }

        @Override
        public void run() {
            if (solution.isSet() || seen.putIfAbsent(getPosition(), true) != null) {
                // already solved or seen this position
                return;
            }
            if (puzzle.isGoal(getPosition())) {
                solution.setValue(this);
            } else {
                for (M move : puzzle.legalMoves(getPosition())) {
                    exec.execute(newTask(puzzle.move(getPosition(), move), move, this));
                }
            }
        }
    }
}
