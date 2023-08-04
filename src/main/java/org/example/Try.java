package org.example;

import org.apache.log4j.BasicConfigurator;
import java.util.ArrayList;
//Full class written by Nehal

//##################################################################
// Use this file to understand how to call the functions I wrote - nehal
//##################################################################
//Person who does main method should figure out a way to make the database logging dissapear- happening due to line #12 and also prettify it
//##################################################################

public class Try {
    public static void main(String[] args) {
        BasicConfigurator.configure();

        //Create bank account using handler class (factory pattern)
        BankAccount acc= BankAccountHandler.getBankAccount(true, "Middly", 1000d);
        System.out.println(acc.getBalance()+acc.getName());

        //Create DB object (singleton pattern) and connect it to db
        DB mongo= DB.createDBConnector();
        mongo.connect();

        //Add created account to db
//        mongo.addAccount(acc);

        //Get a list of all available bank accounts in db
        ArrayList<BankAccount> arr= mongo.getAccounts();
        for(BankAccount temp : arr){
            System.out.println(temp.getName()+" "+temp.getBalance()+" "+ temp.getClass());
        }

        //Update db with balance (negative for withdrawal)
        mongo.update("Middly", -1d);
        arr= mongo.getAccounts();
        for(BankAccount temp : arr){
            System.out.println(temp.getName()+" "+temp.getBalance()+" "+ temp.getClass());
        }


        //Get Balance for particular account with name
        System.out.println(mongo.getBalance("Middly"));
    }
}
