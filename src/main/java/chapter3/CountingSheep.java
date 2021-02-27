package chapter3;

/**
 * CountingSheep
 * <p/>
 * Counting sheep
 *
 */
public class CountingSheep {
    volatile boolean asleep;

    void tryToSleep() {
        while (!asleep) {
            countSomeSheep();
        }
    }

    private void countSomeSheep() {
        // One, two, three...
    }
}
