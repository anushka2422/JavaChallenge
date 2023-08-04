package org.example;

public class SavingsAccount extends BankAccount{

    public SavingsAccount(String name, double balance) {
        super(name, balance);
    }


    public void withdraw(double _balance){
        double balance = getBalance();
        if(_balance>=100)
            balance-=_balance;
    }
}
