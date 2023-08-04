package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.BasicConfigurator;
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class Main {

    private static MongoClient mongoClient;
    private static MongoDatabase database;

    private static MongoCollection<Document> accounts;
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("Main.class");
        BasicConfigurator.configure();
        connectMongo();

        ArrayList<BankAccount> bankAccountList = new ArrayList<>();

        while(true){
            int condition = 0;
            System.out.println("Menu");
            System.out.println("Enter a: Add Account\nEnter I: Display Accounts\nEnter q: Quit\nEnter s: Save to database\nEnter d: Deposit funds\nEnter w: Withdraw funds");
            Scanner sc = new Scanner(System.in);
            char input = sc.next().charAt(1);
            switch (input){
                case 'a':
                    System.out.println("Enter Account name");
                    sc = new Scanner(System.in);
                    String name = sc.nextLine();
                    System.out.println("Enter Balance");
                    sc = new Scanner(System.in);
                    double balance = sc.nextDouble();
                    BankAccount bankAccount = new SavingsAccount(name,balance);
                    System.out.println("Saving Account created");
                    bankAccountList.add(bankAccount);
                    System.out.println("Account name: "+bankAccount.getName()+" Balance: "+bankAccount.getBalance());
                    break;
                case 'I':
                    System.out.println("Bank Accounts List:");
                    for(BankAccount bankAccount1:bankAccountList){
                        System.out.println("Account holder's name: "+bankAccount1.getName()+" Balance: "+bankAccount1.getBalance());
                    }
                    break;
                case 'd':
                    /*accounts = database.getCollection("accountsData");
                    List<Document> accountList = accounts.find().into(new ArrayList<>());
                    for(Document doc:accountList){
                        System.out.println("Account name: "+doc.get("name") + " Balance: "+doc.get("balance"));
                    }
                    */
                     
                    break;
                case 's':
                    insertDocument(bankAccountList);
                    break;
                case 'q':
                    condition = 1;
                    break;
            }
            if(condition==1)
                break;
        }
    }

    private static void connectMongo() {
       mongoClient= MongoClients.create("mongodb://localhost:27017");
       database = mongoClient.getDatabase("accounts");
    }

    private static void insertDocument(ArrayList<BankAccount>bankAccountArrayList){
        accounts = database.getCollection("accountsData");
        for(BankAccount bankAccount1:bankAccountArrayList) {
            Document doc = new Document();
            doc.put("name", bankAccount1.getName());
            doc.put("balance", bankAccount1.getBalance());
            accounts.insertOne(doc);
        }
    }
}