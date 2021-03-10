package chapter16;

import annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Map;

/**
 * SafeStates
 *
 * <p>Initialization safety for immutable objects
 */
@ThreadSafe
public class SafeStates {
  private final Map<String, String> states;

  public SafeStates() {
    states = new HashMap<>();
    states.put("alaska", "AK");
    states.put("alabama", "AL");
    /*...*/
    states.put("wyoming", "WY");
  }

  public String getAbbreviation(String state) {
    return states.get(state);
  }
}
