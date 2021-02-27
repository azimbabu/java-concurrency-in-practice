package chapter13;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * DeadlockAvoidance
 *
 * <p>Avoiding lock-ordering deadlock using tryLock
 */
public class DeadlockAvoidance {

  public static final int DELAY_RANDOM = 2;
  public static final int DELAY_FIXED = 1;
  private static Random random = new Random();

  public boolean transferMoney(
      Account fromAccount,
      Account toAccount,
      DollarAmount dollarAmount,
      long timeout,
      TimeUnit timeUnit)
      throws InsufficientFundsException, InterruptedException {
    long fixedDelay = getFixedDelayComponentNanos(timeout, timeUnit);
    long randMod = getRandomDelayModulusNanos(timeout, timeUnit);
    long stopTime = System.nanoTime() + timeUnit.toNanos(timeout);

    while (true) {
      if (fromAccount.lock.tryLock()) {
        try {
          if (toAccount.lock.tryLock()) {
            try {
              if (fromAccount.getBalance().compareTo(dollarAmount) < 0) {
                throw new InsufficientFundsException();
              } else {
                fromAccount.debit(dollarAmount);
                toAccount.credit(dollarAmount);
                return true;
              }
            } finally {
              toAccount.lock.unlock();
            }
          }
        } finally {
          fromAccount.lock.unlock();
        }
      }

      if (System.nanoTime() > stopTime) {
        return false;
      }
      TimeUnit.NANOSECONDS.sleep(fixedDelay + random.nextInt() % randMod);
    }
  }

  private long getRandomDelayModulusNanos(long timeout, TimeUnit timeUnit) {
    return DELAY_RANDOM;
  }

  private long getFixedDelayComponentNanos(long timeout, TimeUnit timeUnit) {
    return DELAY_FIXED;
  }

  static class Account {
    private DollarAmount balance;
    private Lock lock;

    public Account() {
      balance = new DollarAmount(BigDecimal.ZERO);
    }

    public void debit(DollarAmount dollarAmount) {
      balance = balance.subtract(dollarAmount);
    }

    public void credit(DollarAmount dollarAmount) {
      balance = balance.add(dollarAmount);
    }

    public DollarAmount getBalance() {
      return balance;
    }
  }

  static class DollarAmount implements Comparable<DollarAmount> {
    private BigDecimal amount;

    public DollarAmount(BigDecimal amount) {
      this.amount = amount;
    }

    public DollarAmount add(DollarAmount dollarAmount) {
      return new DollarAmount(amount.add(dollarAmount.amount));
    }

    public DollarAmount subtract(DollarAmount dollarAmount) {
      return new DollarAmount(amount.subtract(dollarAmount.amount));
    }

    @Override
    public int compareTo(DollarAmount dollarAmount) {
      return amount.compareTo(dollarAmount.amount);
    }
  }

  static class InsufficientFundsException extends Exception {}
}
