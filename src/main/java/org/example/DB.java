package org.example;

import com.mongodb.client.*;
import org.bson.Document;

import java.util.ArrayList;

//Full class written by Nehal - After referring a bit to code by Anushka present in main
public class DB {

    static DB dbObj;
    private MongoClient mongoClient;
    private MongoDatabase database;
    private DB(){

    }

    public static DB createDBConnector(){
        if(dbObj==null){
            dbObj=new DB();
        }
        return dbObj;
    }

    public void connect(){
        dbObj.mongoClient= MongoClients.create("mongodb://localhost:27017");
        dbObj.database= mongoClient.getDatabase("db");
    }
    public ArrayList<BankAccount> getAccounts(){
        MongoCollection<Document> accounts= dbObj.database.getCollection("accountData");
        ArrayList<BankAccount> toReturn= new ArrayList<>();
        ArrayList<Document> temp= accounts.find().into(new ArrayList<>());

        BankAccount tempAcc;
        for(Document doc: temp){
            toReturn.add(BankAccountHandler.getBankAccount("Savings".compareTo((String) doc.get("Type"))==0, (String) doc.get("Name"), Double.valueOf(doc.get("Balance").toString())));
        }

        return toReturn;
    }

    public void saveToDB(ArrayList<BankAccount> accounts){
        for(BankAccount acc:accounts){
            dbObj.addAccount(acc);
        }
    }

    public void addAccount(BankAccount acc){
        Document temp=new Document();
        temp.append("Name", acc.getName());
        temp.append("Balance", acc.getBalance());
        if(acc instanceof org.example.SavingsAccount) temp.append("Type", "Savings");
        else temp.append("Type", "Current");
        dbObj.database.getCollection("accountData").insertOne(temp);
    }

    public void update(String name, double amt){
        Document query=new Document();
        query.append("Name",name);
        Document field= new Document();
        field.append("Balance", amt);
        Document update= new Document();
        update.append("$inc", field);
        dbObj.database.getCollection("accountData").updateOne(query,update);
    }

    public double getBalance(String name){
        Document query=new Document();
        query.append("Name", name);

        Document ans= (Document) dbObj.database.getCollection("accountData").find(query).first();
        return (double) ans.get("Balance");
    }
}
