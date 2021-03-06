package chapter15;

import annotations.ThreadSafe;

import java.util.concurrent.atomic.AtomicReference;

/**
 * ConcurrentStack
 *
 * <p>Nonblocking stack using Treiber's algorithm
 */
@ThreadSafe
public class ConcurrentStack<E> {
  private AtomicReference<Node<E>> top = new AtomicReference<>();

  public void push(E item) {
    Node<E> newHead = new Node<>(item);
    Node<E> oldHead;
    do {
      oldHead = top.get();
      newHead.next = oldHead;
    } while (!top.compareAndSet(oldHead, newHead));
  }

  public E pop() {
    Node<E> oldHead;
    Node<E> newHead;
    do {
      oldHead = top.get();
      if (oldHead == null) {
        return null;
      }
      newHead = oldHead.next;
      oldHead.next = null;
    } while (!top.compareAndSet(oldHead, newHead));
    return oldHead.item;
  }

  private static class Node<E> {
    private final E item;
    private Node<E> next;

    public Node(E item) {
      this.item = item;
    }
  }
}
