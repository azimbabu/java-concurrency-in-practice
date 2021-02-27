package chapter10;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DynamicOrderDeadlock
 * <p/>
 * Dynamic lock-ordering deadlock
 *
 */
public class DynamicOrderDeadlock {

    public static void transferMoney(Account fromAccount, Account toAccount, DollarAmount amount) throws InsufficientFundsException {
        synchronized (fromAccount) {
            synchronized (toAccount) {
                if (fromAccount.getBalance().compareTo(amount) < 0) {
                    throw new InsufficientFundsException();
                } else {
                    fromAccount.debit(amount);
                    toAccount.credit(amount);
                }
            }
        }
    }
    static class DollarAmount implements Comparable<DollarAmount> {
        private BigDecimal amount;
        public DollarAmount(BigDecimal amount) {
            this.amount = amount;
        }

        public DollarAmount add(DollarAmount dollarAmount) {
            return new DollarAmount(this.amount.add(dollarAmount.amount));
        }

        public DollarAmount subtract(DollarAmount dollarAmount) {
            return new DollarAmount(this.amount.subtract(dollarAmount.amount));
        }

        @Override
        public int compareTo(DollarAmount dollarAmount) {
            return this.amount.compareTo(dollarAmount.amount);
        }
    }

    static class Account {
        private DollarAmount balance;
        private final int accountNumber;
        private static final AtomicInteger sequence = new AtomicInteger();

        public Account() {
            accountNumber = sequence.incrementAndGet();
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

        public int getAccountNumber() {
            return accountNumber;
        }
    }

    static class InsufficientFundsException extends Exception {
    }
}
