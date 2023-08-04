package org.example;

public class BankAccount {
    private String name;
    private double balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public BankAccount(String name, double balance) {
        this.name = name;
        this.balance = balance;
    }

    public void deposit(double _balance){
        this.balance+=_balance;
    }

    public void withdraw(double _balance){
        this.balance-=_balance;
    }
}
