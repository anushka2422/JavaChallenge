package org.example;

//Full class written by Nehal
public class BankAccountHandler {
    static BankAccount getBankAccount(boolean savings, String name, Double balance){

        if(savings) return new SavingsAccount(name, balance);
        else return new BankAccount(name, balance);
    }
}
