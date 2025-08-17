// Class representing a simple bank account
class BankAccount {
    // Private fields
    private String accountNumber; 
    private double balance;

    // Constructor to initialize account number and balance
    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }

    // Getter method to retrieve the current balance
    public double getBalance() {
        return balance;
    }

    // Setter method to update the balance with validation
    public void setBalance(double amount) {
        if (amount >= 0) {
            balance = amount;
        } else {
            System.out.println("Invalid amount: Balance cannot be negative");
        }
    }

    // Method to deposit money into the account
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount; // add to current balance
            System.out.println("Deposited: " + amount);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }

    // Method to withdraw money from the account with validation
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount; // subtract from current balance
            System.out.println("Withdrawn: " + amount);
        } else {
            System.out.println("Invalid withdrawal amount");
        }
    }
}

// Main class to test BankAccount functionality
public class Main {
    public static void main(String[] args) {
        // Create a new BankAccount instance with initial balance
        BankAccount account = new BankAccount("12345", 1000);

        // Deposit money
        account.deposit(500);

        // Withdraw money
        account.withdraw(280);

        // Print final balance
        System.out.println("Final balance: " + account.getBalance());

        // Uncommenting the following line will cause an error 
        // because balance is private and cannot be accessed directly
        // account.balance = 103; 
    }
}
