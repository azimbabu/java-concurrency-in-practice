package chapter11;

import annotations.GuardedBy;
import annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * ServerStatusBeforeSplit
 * <p/>
 * Candidate for lock splitting
 *
 */
@ThreadSafe
public class ServerStatusBeforeSplit {
    @GuardedBy("this") private final Set<String> users;
    @GuardedBy("this") private final Set<String> queries;

    public ServerStatusBeforeSplit() {
        users = new HashSet<>();
        queries = new HashSet<>();
    }

    public synchronized void addUser(String user) {
        users.add(user);
    }

    public synchronized void removeUser(String user) {
        users.remove(user);
    }

    public synchronized void addQuery(String query) {
        queries.add(query);
    }

    public synchronized void removeQuery(String query) {
        queries.remove(query);
    }
}
