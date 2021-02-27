package chapter8;

import annotations.Immutable;

import java.util.LinkedList;
import java.util.List;

@Immutable
public class PuzzleNode<P, M> {
  private final P position;
  private final M move;
  private final PuzzleNode<P, M> prev;

  public PuzzleNode(P position, M move, PuzzleNode<P, M> prev) {
    this.position = position;
    this.move = move;
    this.prev = prev;
  }

  public List<M> asMoveList() {
    LinkedList<M> solution = new LinkedList<>();
    for (PuzzleNode<P, M> node = this; node.move != null; node = node.prev) {
      solution.addFirst(node.move);
    }
    return solution;
  }

  public P getPosition() {
    return position;
  }

  public M getMove() {
    return move;
  }

  public PuzzleNode<P, M> getPrev() {
    return prev;
  }
}
