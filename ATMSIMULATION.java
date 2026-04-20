import java.util.*;
import java.time.LocalDateTime;
import java.io.*;

public class ATMSIMULATION {

    static final String FILE_NAME = "accounts.txt";

    // ---------------- TRANSACTION CLASS ----------------
    static class Transaction {
        String type;
        double amount;
        LocalDateTime time;

        public Transaction(String type, double amount) {
            this.type = type;
            this.amount = amount;
            this.time = LocalDateTime.now();
        }

        public void display() {
            System.out.println(type + " : " + amount + " at " + time);
        }
    }

    // ---------------- ACCOUNT CLASS ----------------
    static class Account {
        String accNumber;
        String name;
        int pin;
        double balance;
        ArrayList<Transaction> transactions;

        public Account(String accNumber, String name, int pin, double balance) {
            this.accNumber = accNumber;
            this.name = name;
            this.pin = pin;
            this.balance = balance;
            this.transactions = new ArrayList<>();
        }

        public boolean checkPin(int inputPin) {
            return this.pin == inputPin;
        }

        public void changePin(int newPin) {
            this.pin = newPin;
            System.out.println("PIN changed successfully!");
        }

        public void deposit(double amount) {
            balance += amount;
            transactions.add(new Transaction("Deposit", amount));
            System.out.println("Deposited Successfully!");
        }

        public void withdraw(double amount) {
            if (amount > balance) {
                System.out.println("Insufficient Balance!");
            } else {
                balance -= amount;
                transactions.add(new Transaction("Withdraw", amount));
                System.out.println("Withdraw Successful!");
            }
        }

        public void showBalance() {
            System.out.println("Current Balance: " + balance);
        }

        public void showHistory() {
            System.out.println("Transaction History:");
            for (Transaction t : transactions) {
                t.display();
            }
        }
    }

    // ---------------- LOAD FROM FILE ----------------
    static HashMap<String, Account> loadAccounts() {
        HashMap<String, Account> accounts = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                String accNo = data[0];
                String name = data[1];
                int pin = Integer.parseInt(data[2]);
                double bal = Double.parseDouble(data[3]);

                accounts.put(accNo, new Account(accNo, name, pin, bal));
            }
        } catch (Exception e) {
            System.out.println("No previous data found, starting fresh...");
        }

        return accounts;
    }

    // ---------------- SAVE TO FILE ----------------
    static void saveAccounts(HashMap<String, Account> accounts) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Account acc : accounts.values()) {
                bw.write(acc.accNumber + "," + acc.name + "," + acc.pin + "," + acc.balance);
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println("Error saving data!");
        }
    }

    // ---------------- MAIN ----------------
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 🔥 LOAD DATA HERE
        HashMap<String, Account> accounts = loadAccounts();

        while (true) {
            System.out.println("\n===== ATM SYSTEM =====");
            System.out.println("1. Login");
            System.out.println("2. Create Account");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");

            int mainChoice = sc.nextInt();

            switch (mainChoice) {

                case 1:
                    System.out.print("Enter Account Number: ");
                    String accNo = sc.next();

                    if (!accounts.containsKey(accNo)) {
                        System.out.println("Account not found!");
                        break;
                    }

                    Account acc = accounts.get(accNo);

                    System.out.print("Enter PIN: ");
                    int pin = sc.nextInt();

                    if (!acc.checkPin(pin)) {
                        System.out.println("Wrong PIN!");
                        break;
                    }

                    System.out.println("Login Successful! Welcome " + acc.name);

                    int choice;
                    do {
                        System.out.println("\n----- ATM MENU -----");
                        System.out.println("1. Check Balance");
                        System.out.println("2. Deposit");
                        System.out.println("3. Withdraw");
                        System.out.println("4. Transaction History");
                        System.out.println("5. Change PIN");
                        System.out.println("6. Logout");
                        System.out.print("Enter choice: ");

                        choice = sc.nextInt();

                        switch (choice) {
                            case 1:
                                acc.showBalance();
                                break;

                            case 2:
                                System.out.print("Enter amount: ");
                                double dep = sc.nextDouble();
                                acc.deposit(dep);
                                saveAccounts(accounts); // 🔥 save after change
                                break;

                            case 3:
                                System.out.print("Enter amount: ");
                                double wit = sc.nextDouble();
                                acc.withdraw(wit);
                                saveAccounts(accounts);
                                break;

                            case 4:
                                acc.showHistory();
                                break;

                            case 5:
                                System.out.print("Enter new PIN: ");
                                int newPin = sc.nextInt();
                                acc.changePin(newPin);
                                saveAccounts(accounts);
                                break;

                            case 6:
                                System.out.println("Logged out!");
                                break;

                            default:
                                System.out.println("Invalid choice!");
                        }

                    } while (choice != 6);
                    break;

                case 2:
                    System.out.print("Enter New Account Number: ");
                    String newAccNo = sc.next();

                    if (accounts.containsKey(newAccNo)) {
                        System.out.println("Account already exists!");
                        break;
                    }

                    System.out.print("Enter Name: ");
                    String name = sc.next();

                    System.out.print("Set PIN: ");
                    int newPin = sc.nextInt();

                    System.out.print("Enter Initial Balance: ");
                    double bal = sc.nextDouble();

                    accounts.put(newAccNo, new Account(newAccNo, name, newPin, bal));

                    saveAccounts(accounts); // 🔥 save new account
                    System.out.println("Account Created Successfully!");
                    break;

                case 3:
                    saveAccounts(accounts); // 🔥 final save
                    System.out.println("Thank you!");
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}

