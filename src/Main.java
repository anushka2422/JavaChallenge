import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static List<BankAccount> accounts = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);
    private static MongoClient mongoClient;
    private static MongoCollection<Document> accountsCollection;

    public static void main(String[] args) {
        char option;

        // Initialize MongoDB client and collection
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("bank");
        accountsCollection = database.getCollection("accounts");

        loadAccountsFromDatabase();

        // Add a shutdown hook to save accounts when the program ends
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            saveAccountsToDatabase();
            mongoClient.close(); // Close the MongoDB connection
            System.out.println("Accounts saved to the database.");
            logger.info("Accounts saved to the database.");
        }));

        do {
            displayMenu();
            option = scanner.next().charAt(0);
            scanner.nextLine(); // Consume the newline character after reading the char

            switch (option) {
                case 'a':
                    addAccount();
                    break;
                case 'l':
                    displayAccounts();
                    break;
                case 's':
                    saveAccountsToDatabase();
                    break;
                case 'd':
                    depositFunds();
                    break;
                case 'w':
                    withdrawFunds();
                    break;
                case 'q':
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
            System.out.println(); // Add a blank line for better readability
        } while (option != 'q');
    }

    private static void displayMenu() {
        System.out.println("Choose an option:");
        System.out.println("(a) Add Account");
        System.out.println("(l) Display Accounts");
        System.out.println("(s) Save to database");
        System.out.println("(d) Deposit funds");
        System.out.println("(w) Withdraw funds");
        System.out.println("(q) Quit");
    }

    private static void addAccount() {
        System.out.println("Enter account name:");
        String accountName = scanner.nextLine();

        System.out.println("Enter account balance:");
        double balance = readDoubleInput();

        try {
            BankAccount account = new SavingsAccount(accountName, balance);
            accounts.add(account);
            System.out.println("Account added successfully.");
            logger.info("Account added: {}", accountName);
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to add account: " + e.getMessage());
            logger.error("Error adding account: {}", e.getMessage(), e);
        }
    }

    private static void displayAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        System.out.println("Accounts:");
        for (int i = 0; i < accounts.size(); i++) {
            BankAccount account = accounts.get(i);
            System.out.println((i + 1) + ". Account Name: " + account.getAccountName());
            System.out.println("   Account Balance: " + account.getBalance());
            System.out.println("----------------------------");
        }
    }

    private static void saveAccountsToDatabase() {
        accountsCollection.deleteMany(new Document()); // Clear existing data in the collection

        for (BankAccount account : accounts) {
            Document doc = new Document("accountName", account.getAccountName())
                    .append("balance", account.getBalance());
            accountsCollection.insertOne(doc);
        }
    }

    private static void loadAccountsFromDatabase() {
        accounts.clear(); // Clear existing accounts list

        for (Document doc : accountsCollection.find()) {
            String accountName = doc.getString("accountName");
            double balance = doc.getDouble("balance");
            try {
                BankAccount account = new SavingsAccount(accountName, balance);
                accounts.add(account);
            } catch (IllegalArgumentException e) {
                System.out.println("Failed to load account: " + e.getMessage());
                logger.error("Error loading account: {}", e.getMessage(), e);
            }
        }
    }

    private static int readIntegerInput() {
        int input = -1;
        while (input == -1) {
            try {
                input = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return input;
    }

    private static double readDoubleInput() {
        double input = -1;
        while (input == -1) {
            try {
                input = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return input;
    }

    private static void depositFunds() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        displayAccounts();

        System.out.println("Enter the account number to deposit into:");
        int accountNumber = readIntegerInput();

        if (accountNumber < 1 || accountNumber > accounts.size()) {
            System.out.println("Invalid account number.");
            return;
        }

        BankAccount account = accounts.get(accountNumber - 1);

        System.out.println("Enter the amount to deposit:");
        double amount = readDoubleInput();

        try {
            account.deposit(amount);
            System.out.println("Deposit successful. Updated balance: " + account.getBalance());
            logger.info("Deposit: {} to account: {}", amount, account.getAccountName());
        } catch (IllegalArgumentException e) {
            System.out.println("Failed to deposit: " + e.getMessage());
            logger.error("Error depositing funds: {}", e.getMessage(), e);
        }
    }

    private static void withdrawFunds() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found.");
            return;
        }

        displayAccounts();

        System.out.println("Enter the account number to withdraw from:");
        int accountNumber = readIntegerInput();

        if (accountNumber < 1 || accountNumber > accounts.size()) {
            System.out.println("Invalid account number.");
            return;
        }

        BankAccount account = accounts.get(accountNumber - 1);

        System.out.println("Enter the amount to withdraw:");
        double amount = readDoubleInput();

        try {
            account.withdraw(amount);
            System.out.println("Withdrawal successful. Updated balance: " + account.getBalance());
            logger.info("Withdrawal: {} from account: {}", amount, account.getAccountName());
        } catch (IllegalArgumentException | InsufficientFundsException e) {
            System.out.println("Failed to withdraw: " + e.getMessage());
            logger.error("Error withdrawing funds: {}", e.getMessage(), e);
        }
    }
}
