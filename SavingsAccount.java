public class SavingsAccount extends BankAccount {
    public SavingsAccount(String accountName, double balance) {
        super(accountName, balance);
    }

    @Override
    public void withdraw(double amount) throws InsufficientFundsException {
        if (getBalance() - amount < 100) {
            throw new InsufficientFundsException("Minimum balance of 100 required. Cannot withdraw.");
        }
        super.withdraw(amount);
    }
}
