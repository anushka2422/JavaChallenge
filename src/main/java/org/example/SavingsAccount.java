package org.example;

public class SavingsAccount extends BankAccount{

    public SavingsAccount(String name, double balance) {
        super(name, balance);
    }


    public boolean withdraw(double _balance){
        double balance = getBalance();

        if(balance-_balance<100){
            System.out.println("Insufficient bank balance");
            return false;
        }
        balance=-_balance;
        return true;
    }
}
