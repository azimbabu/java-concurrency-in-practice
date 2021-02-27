package chapter3;

import java.util.HashSet;
import java.util.Set;

/**
 * Secrets
 *
 * Publishing an object
 *
 */
public class Secrets {
    public static Set<Secret> knownSecrets;

    public void initialize() {
        knownSecrets = new HashSet<>();
    }
}

class Secret {
}
