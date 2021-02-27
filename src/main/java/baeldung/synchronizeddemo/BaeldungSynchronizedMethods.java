package baeldung.synchronizeddemo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class BaeldungSynchronizedMethods {

    private int sum = 0;
    private static int staticSum = 0;

    public void calculate() {
        setSum(getSum() + 1);
    }

    public synchronized void synchronizedMethodCalculate() {
        setSum(getSum() + 1);
    }

    public void synchronizedBlockCalculate() {
        synchronized (this) {
            setSum(getSum() + 1);
        }
    }

    public static synchronized void staticSynchronizedMethodCalculate() {
        staticSum = staticSum + 1;
    }

    public static void staticSynchronizedBlockCalculate() {
        synchronized (BaeldungSynchronizedMethods.class) {
            staticSum = staticSum + 1;
        }
    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

  public static void main(String[] args) throws InterruptedException {
      raceConditionDemo();
      synchronizedMethodDemo();
      synchronizedBlockDemo();
      staticSynchronizedMethodDemo();
      staticSynchronizedBlockDemo();
  }

    private static void raceConditionDemo() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        BaeldungSynchronizedMethods summation = new BaeldungSynchronizedMethods();

        IntStream.range(0, 1000).forEach(value -> executorService.submit(summation::calculate));
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);

        System.out.println("raceCondition, Sum=" + summation.getSum());
        executorService.shutdown();
    }

    private static void synchronizedMethodDemo() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        BaeldungSynchronizedMethods summation = new BaeldungSynchronizedMethods();

        IntStream.range(0, 1000).forEach(value -> executorService.submit(summation::synchronizedMethodCalculate));
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);

        System.out.println("synchronizedMethodDemo, Sum=" + summation.getSum());
        executorService.shutdown();
    }

    private static void synchronizedBlockDemo() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        BaeldungSynchronizedMethods summation = new BaeldungSynchronizedMethods();

        IntStream.range(0, 1000).forEach(value -> executorService.submit(summation::synchronizedBlockCalculate));
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);

        System.out.println("synchronizedBlockDemo, Sum=" + summation.getSum());
        executorService.shutdown();
    }

    private static void staticSynchronizedMethodDemo() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        IntStream.range(0, 1000).forEach(value -> executorService.submit(BaeldungSynchronizedMethods::staticSynchronizedMethodCalculate));
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);

        System.out.println("staticSynchronizedMethodDemo, Sum=" + BaeldungSynchronizedMethods.staticSum);
        executorService.shutdown();
    }

    private static void staticSynchronizedBlockDemo() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        IntStream.range(0, 1000).forEach(value -> executorService.submit(BaeldungSynchronizedMethods::staticSynchronizedBlockCalculate));
        executorService.awaitTermination(1000, TimeUnit.MILLISECONDS);

        System.out.println("staticSynchronizedBlockDemo, Sum=" + BaeldungSynchronizedMethods.staticSum);
        executorService.shutdown();
    }
}
