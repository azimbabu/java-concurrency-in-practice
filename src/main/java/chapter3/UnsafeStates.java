package chapter3;

/**
 * UnsafeStates
 *
 * <p>Allowing internal mutable state to escape
 */
public class UnsafeStates {
  private String[] states = new String[] {"AK", "AL" /*...*/};

  public String[] getStates() {
    return states;
  }
}
