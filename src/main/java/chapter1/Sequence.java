package chapter1;

import annotations.GuardedBy;
import annotations.ThreadSafe;

@ThreadSafe
public class Sequence {
    @GuardedBy("this") private int value;

    // Returns a unique value
    public synchronized
    int getNext() {
        return value++;
    }
}
