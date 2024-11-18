package Bank;

public class BankAccount {
    private long amount = 20000000;

    public synchronized long getBalance() {
        return amount;
    }

    public boolean checkAccountBalance(long withdrawAmount) {
        try {
            Thread.sleep(2000); // Simulate delay
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return withdrawAmount <= amount;
    }

    public void withdraw(String threadName, long withdrawAmount) {
        System.out.println(threadName + " checks: " + withdrawAmount);

        synchronized (this) {
            if (checkAccountBalance(withdrawAmount)) {
                try {
                    Thread.sleep(2000); // Simulate processing delay
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                amount -= withdrawAmount;
                System.out.println(threadName + " withdraws successfully: " + withdrawAmount);
            } else {
                System.out.println(threadName + " withdraw error: Insufficient funds!");
            }
        }

        System.out.println(threadName + " sees balance: " + amount);
    }
}
