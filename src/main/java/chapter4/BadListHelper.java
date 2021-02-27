package chapter4;

import annotations.NotThreadSafe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ListHelder
 * <p/>
 * Example non-thread-safe implementation of
 * put-if-absent helper methods for List
 *
 */
@NotThreadSafe
public class BadListHelper<E> {
    public List<E> list = Collections.synchronizedList(new ArrayList<>());

    public synchronized boolean putIfAbsent(E e) {
        boolean absent = !list.contains(e);
        if (absent) {
            list.add(e);
        }
        return absent;
    }
}
