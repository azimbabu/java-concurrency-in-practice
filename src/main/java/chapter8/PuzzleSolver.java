package chapter8;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * PuzzleSolver
 *
 * <p>Solver that recognizes when no solution exists
 */
public class PuzzleSolver<P, M> extends ConcurrentPuzzleSolver<P, M> {
  private final AtomicInteger tasksCount = new AtomicInteger();

  public PuzzleSolver(Puzzle<P, M> puzzle) {
    super(puzzle);
  }

  @Override
  protected Runnable newTask(P position, M move, PuzzleNode<P, M> node) {
    return new CountingSolverTask(position, move, node);
  }

  class CountingSolverTask extends SolverTask {

    public CountingSolverTask(P position, M move, PuzzleNode<P, M> prev) {
      super(position, move, prev);
      tasksCount.incrementAndGet();
    }

    @Override
    public void run() {
      try {
        super.run();
      } finally {
        if (tasksCount.decrementAndGet() == 0) {
          solution.setValue(null);
        }
      }
    }
  }
}
