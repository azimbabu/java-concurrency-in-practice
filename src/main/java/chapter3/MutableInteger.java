package chapter3;

import annotations.NotThreadSafe;

/**
 * MutableInteger
 * <p/>
 * Non-thread-safe mutable integer holder
 *
 */
@NotThreadSafe
public class MutableInteger {
    private int value;

    public int get() {
        return value;
    }

    public void set(int value) {
        this.value = value;
    }
}
