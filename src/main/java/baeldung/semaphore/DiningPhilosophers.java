package baeldung.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiningPhilosophers {

    static class Philosopher implements Runnable {
        // The forks on either side of this Philosopher
        private Object leftFork;
        private Object rightFork;

        public Philosopher(Object leftFork, Object rightFork) {
            this.leftFork = leftFork;
            this.rightFork = rightFork;
        }

        @Override
        public void run() {
            try{
                // thinking
                doAction(System.nanoTime() + ": Thinking");

                synchronized (leftFork) {
                    doAction(System.nanoTime() + ": Picked up left fork");

                    synchronized (rightFork) {
                        doAction(System.nanoTime() + ": Picked up right fork");

                        doAction(System.nanoTime() + ": Eating...");

                    }

                    doAction(System.nanoTime() + ": Put down right fork");
                }

                doAction(System.nanoTime() + ": Put down left fork, back to thinking");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void doAction(String action) throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " " + action);
            Thread.sleep((long) (Math.random() * 100));
        }
    }

  public static void main(String[] args) {
      //deadlockDemo();
      //simpleSolutionDemo();
      orderedLocksDemo();
  }

    private static void deadlockDemo() {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[philosophers.length];

        for (int i=0; i < forks.length; i++) {
            forks[i] = new Object();
        }

        Thread[] threads = new Thread[philosophers.length];
        for (int i=0; i < philosophers.length; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i+1) % forks.length];
            philosophers[i] = new Philosopher(leftFork, rightFork);
            Thread thread = new Thread(philosophers[i], "Philosopher " + i);
            threads[i] = thread;
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=0; i < threads.length; i++) {
            executorService.execute(threads[i]);
        }
        executorService.shutdown();
    }

    private static void simpleSolutionDemo() {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[philosophers.length];

        for (int i=0; i < forks.length; i++) {
            forks[i] = new Object();
        }

        Thread[] threads = new Thread[philosophers.length];
        for (int i=0; i < philosophers.length; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i+1) % forks.length];
            if (i == philosophers.length-1) {
                philosophers[i] = new Philosopher(rightFork, leftFork);
            } else {
                philosophers[i] = new Philosopher(leftFork, rightFork);
            }
            Thread thread = new Thread(philosophers[i], "Philosopher " + i);
            threads[i] = thread;
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=0; i < threads.length; i++) {
            executorService.execute(threads[i]);
        }
        executorService.shutdown();
    }

    private static void orderedLocksDemo() {
        Philosopher[] philosophers = new Philosopher[5];
        Object[] forks = new Object[philosophers.length];

        for (int i=0; i < forks.length; i++) {
            forks[i] = new Object();
        }

        Thread[] threads = new Thread[philosophers.length];
        for (int i=0; i < philosophers.length; i++) {
            Object leftFork = forks[i];
            Object rightFork = forks[(i+1) % forks.length];
            if (leftFork.hashCode() <= rightFork.hashCode()) {
                philosophers[i] = new Philosopher(leftFork, rightFork);
            } else {
                philosophers[i] = new Philosopher(rightFork, leftFork);
            }
            Thread thread = new Thread(philosophers[i], "Philosopher " + i);
            threads[i] = thread;
        }

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=0; i < threads.length; i++) {
            executorService.execute(threads[i]);
        }
        executorService.shutdown();
    }
}
