package chapter10;

/**
 * LeftRightDeadlock
 *
 * <p>Simple lock-ordering deadlock
 */
public class LeftRightDeadlock {
  private final Object left = new Object();
  private final Object right = new Object();

  public void leftRight() {
    synchronized (left) {
      synchronized (right) {
        doSomething();
      }
    }
  }

  public void rightLeft() {
    synchronized (right) {
      synchronized (left) {
        doSomethingElse();
      }
    }
  }

  private void doSomethingElse() {}

  private void doSomething() {}
}
