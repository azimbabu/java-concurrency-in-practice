package chapter15;

import annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * LinkedQueue
 *
 * <p>Insertion in the Michael-Scott nonblocking queue algorithm
 */
@ThreadSafe
public class LinkedQueue<E> {

  private final Node<E> dummy = new Node<>(null, null);
  private final AtomicReference<Node<E>> head = new AtomicReference<>(dummy);
  private final AtomicReference<Node<E>> tail = new AtomicReference<>(dummy);

  public boolean put(E item) {
    Node<E> newNode = new Node<>(item, null);
    while (true) {
      Node<E> currentTail = tail.get();
      Node<E> currentTailNext = currentTail.next.get();
      if (currentTail == tail.get()) {
        if (currentTailNext != null) {
          // Queue in indeterminate state, advance tail
          tail.compareAndSet(currentTail, currentTailNext);
        } else {
          // In quiescent state, try inserting new node
          if (currentTail.next.compareAndSet(null, newNode)) {
            // Insertion suceeded, try advancing tail
            tail.compareAndSet(currentTail, newNode);
            return true;
          }
        }
      }
    }
  }

  private static class Node<E> {
    private final E item;
    private final AtomicReference<Node<E>> next;

    public Node(E item, Node<E> next) {
      this.item = item;
      this.next = new AtomicReference<>(next);
    }
  }
}
