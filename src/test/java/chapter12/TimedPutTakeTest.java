package chapter12;

import org.junit.jupiter.api.Assertions;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimedPutTakeTest extends PutTakeTest {
    private BarrierTimer timer = new BarrierTimer();

    public TimedPutTakeTest(int capacity, int numPairs, int numTrials) {
        super(capacity, numPairs, numTrials);
        barrier = new CyclicBarrier(2 * numPairs + 1, timer);
    }

    void test() {
        try {
            timer.clear();
            for (int i=0; i < numPairs; i++) {
                executorService.execute(new PutTakeTest.Producer());
                executorService.execute(new PutTakeTest.Consumer());
            }

            barrier.await();    // wait for all threads to be ready
            barrier.await();    // wait for all threads to finish

            long nsPerItem = timer.getTime() / (numPairs * (long) numTrials);
            System.out.println("Throughput: " + nsPerItem + " ns/item");
            assertEquals(putSum.get(), takeSum.get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

  public static void main(String[] args) throws Exception {
        int trialsPerThread = 100000;
        for (int capacity=1; capacity <= 1000; capacity *= 10) {
            System.out.println("Capacity: " + capacity);
            for (int numPairs = 1; numPairs <= 128; numPairs *= 2) {
                TimedPutTakeTest putTakeTest = new TimedPutTakeTest(capacity, numPairs, trialsPerThread);
                System.out.println("Pairs: " + numPairs + "\t");
                putTakeTest.test();
                System.out.println("\t");
                Thread.sleep(1000);
                putTakeTest.test();
                System.out.println();
                Thread.sleep(1000);
            }
        }

        PutTakeTest.executorService.shutdown();
  }
}
