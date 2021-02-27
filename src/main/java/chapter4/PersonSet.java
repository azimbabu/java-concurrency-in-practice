package chapter4;

import annotations.GuardedBy;
import annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * PersonSet
 *
 * <p>Using confinement to ensure thread safety
 */
@ThreadSafe
public class PersonSet {
  @GuardedBy("this")
  private final Set<Person> mySet = new HashSet<>();

  public synchronized void addPerson(Person person) {
    mySet.add(person);
  }

  public synchronized boolean containsPerson(Person person) {
    return mySet.contains(person);
  }

  interface Person {}
}
