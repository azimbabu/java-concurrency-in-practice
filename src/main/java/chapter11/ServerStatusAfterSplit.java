package chapter11;

import annotations.GuardedBy;
import annotations.ThreadSafe;

import java.util.HashSet;
import java.util.Set;

/**
 * ServerStatusAfterSplit
 * <p/>
 * ServerStatus refactored to use split locks
 *
 */
@ThreadSafe
public class ServerStatusAfterSplit {
    @GuardedBy("users") private final Set<String> users;
    @GuardedBy("queries") private final Set<String> queries;

    public ServerStatusAfterSplit() {
        users = new HashSet<>();
        queries = new HashSet<>();
    }

    public void addUser(String user) {
        synchronized (users) {
            users.add(user);
        }
    }

    public synchronized void removeUser(String user) {
        synchronized (users) {
            users.remove(user);
        }
    }

    public synchronized void addQuery(String query) {
        synchronized (queries) {
            queries.add(query);
        }
    }

    public synchronized void removeQuery(String query) {
        synchronized (queries) {
            queries.remove(query);
        }
    }
}
