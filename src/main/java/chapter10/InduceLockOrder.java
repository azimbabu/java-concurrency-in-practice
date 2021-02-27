package chapter10;

/**
 * InduceLockOrder
 *
 * Inducing a lock order to avoid deadlock
 *
 */
public class InduceLockOrder {

    private static final Object tieLock = new Object();

    public void transferMoney(final Account fromAccount,
                              final Account toAccount,
                              final DollarAmount amount) throws InsufficientFundsException {
        int fromHash = System.identityHashCode(fromAccount);
        int toHash = System.identityHashCode(toAccount);
        if (fromHash < toHash) {
            synchronized (fromAccount) {
                synchronized (toAccount) {
                    transfer(fromAccount, toAccount, amount);
                }
            }
        } else if (fromHash > toHash) {
            synchronized (toAccount) {
                synchronized (fromAccount) {
                    transfer(fromAccount, toAccount, amount);
                }
            }
        } else {
            synchronized (tieLock) {
                synchronized (fromAccount) {
                    synchronized (toAccount) {
                        transfer(fromAccount, toAccount, amount);
                    }
                }
            }
        }
    }

    private void transfer(Account fromAccount, Account toAccount, DollarAmount amount) throws InsufficientFundsException {
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException();
        } else {
            fromAccount.debit(amount);
            toAccount.credit(amount);
        }
    }


    interface DollarAmount extends Comparable<DollarAmount> {
    }

    interface Account {
        void debit(DollarAmount dollarAmount);
        void credit(DollarAmount dollarAmount);
        DollarAmount getBalance();
        int getAccountNumber();
    }

    static class InsufficientFundsException extends Exception {
    }
}
