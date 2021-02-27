package chapter10;

import chapter10.DynamicOrderDeadlock.Account;
import chapter10.DynamicOrderDeadlock.DollarAmount;

import java.math.BigDecimal;
import java.util.Random;

/**
 * DemonstrateDeadlock
 *
 * <p>Driver loop that induces deadlock under typical conditions
 */
public class DemonstrateDeadlock {
  private static final int NUM_THREADS = 20;
  private static final int NUM_ACCOUNTS = 5;
  private static final int NUM_ITERATIONS = 1000000;

  public static void main(String[] args) {
    final Random random = new Random();
    final Account[] accounts = new Account[NUM_ACCOUNTS];

    for (int i = 0; i < NUM_ACCOUNTS; i++) {
      accounts[i] = new Account();
    }

    class TransferThread extends Thread {
      @Override
      public void run() {
        for (int i = 0; i < NUM_ITERATIONS; i++) {
          int fromAccount = random.nextInt(NUM_ACCOUNTS);
          int toAccount = random.nextInt(NUM_ACCOUNTS);
          DollarAmount amount = new DollarAmount(BigDecimal.valueOf(random.nextInt(1000)));
          try {
            DynamicOrderDeadlock.transferMoney(accounts[fromAccount], accounts[toAccount], amount);
          } catch (DynamicOrderDeadlock.InsufficientFundsException ignored) {
          }
        }
      }
    }

    for (int i = 0; i < NUM_THREADS; i++) {
      new TransferThread().start();
    }
  }
}
