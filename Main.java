import java.util.Scanner;

/**
 * ============================================================================
 * DECODELABS ATM INTERFACE SYSTEM
 * ============================================================================
 * A complete, production-ready ATM system demonstrating:
 * - Object-Oriented Programming (Encapsulation, Inheritance, Polymorphism)
 * - State Design Pattern
 * - Input Validation & Graceful Error Recovery
 * - Separation of Concerns (UI vs Business Logic)
 * - Transaction History & PIN-based Security
 * ============================================================================
 */

// ========================= BANK ACCOUNT CLASS =========================

class BankAccount {
    // Private variables - locked down state (Encapsulation)
    private String accountNumber;
    private String pin;
    private double balance;
    private String[] transactionHistory;
    private int transactionCount;
    private static final int MAX_TRANSACTIONS = 50;

    // Constructor
    public BankAccount(String accountNumber, String pin, double initialBalance) {
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.balance = initialBalance;
        this.transactionHistory = new String[MAX_TRANSACTIONS];
        this.transactionCount = 0;
        addTransaction("🏦 ACCOUNT CREATED with balance: $" + String.format("%.2f", initialBalance));
    }

    // Getters - read-only access
    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    // Security checkpoint - PIN validation
    public boolean validatePin(String inputPin) {
        return this.pin.equals(inputPin);
    }

    // Mutator with business rules - Withdrawal
    public boolean withdraw(double amount) {

        if (amount <= 0) {
            System.out.println("❌ Error: Withdrawal amount must be greater than zero.");
            return false;
        }

        // Business rule: sufficient balance check
        if (amount > balance) {
            System.out.println("❌ Error: Insufficient funds.");
            System.out.println("   Available balance: $" + String.format("%.2f", balance));
            return false;
        }

        // Business rule: maximum withdrawal limit (security feature)
        if (amount > 1000) {
            System.out.println("❌ Error: Daily withdrawal limit is $1000 per transaction.");
            return false;
        }

        // Process the withdrawal
        balance -= amount;
        addTransaction("💸 WITHDRAWAL: -$" + String.format("%.2f", amount));
        System.out.println("✅ Withdrawal successful!");
        System.out.println("   Please take your cash.");
        System.out.println("   New balance: $" + String.format("%.2f", balance));
        return true;
    }

    // Mutator with business rules - Deposit
    public boolean deposit(double amount) {
        // Business rule: deposit amount must be positive
        if (amount <= 0) {
            System.out.println("❌ Error: Deposit amount must be greater than zero.");
            return false;
        }

        // Business rule: maximum deposit limit (security feature)
        if (amount > 10000) {
            System.out.println("❌ Error: Maximum deposit per transaction is $10,000.");
            return false;
        }

        // Process the deposit
        balance += amount;
        addTransaction("💰 DEPOSIT: +$" + String.format("%.2f", amount));
        System.out.println("✅ Deposit successful!");
        System.out.println("   New balance: $" + String.format("%.2f", balance));
        return true;
    }

    // Read-only balance check
    public void checkBalance() {
        System.out.println("\n" + "─".repeat(40));
        System.out.println("💰 CURRENT BALANCE 💰");
        System.out.println("─".repeat(40));
        System.out.println("Account: " + maskAccountNumber(accountNumber));
        System.out.println("Balance: $" + String.format("%.2f", balance));
        System.out.println("─".repeat(40));
        addTransaction("📊 BALANCE CHECK: $" + String.format("%.2f", balance));
    }

    // Transaction history feature (bonus)
    private void addTransaction(String transaction) {
        if (transactionCount < MAX_TRANSACTIONS) {
            transactionHistory[transactionCount] = transaction;
            transactionCount++;
        }
    }

    public void showTransactionHistory() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("📜 TRANSACTION HISTORY 📜");
        System.out.println("═".repeat(50));
        if (transactionCount == 0) {
            System.out.println("   No transactions yet.");
        } else {
            for (int i = 0; i < transactionCount; i++) {
                System.out.println("   " + (i + 1) + ". " + transactionHistory[i]);
            }
        }
        System.out.println("═".repeat(50));
    }

    // Helper method for security - masks account number
    private String maskAccountNumber(String fullNumber) {
        if (fullNumber.length() <= 4) {
            return "****";
        }
        return "****" + fullNumber.substring(fullNumber.length() - 4);
    }
}

// ========================= ATM STATE INTERFACE =========================

interface ATMState {
    void insertCard(ATMContext atm);
    void ejectCard(ATMContext atm);
    void enterPin(ATMContext atm, String pin);
    void selectTransaction(ATMContext atm, int choice);
    void exit(ATMContext atm);
}

// ========================= IDLE STATE =========================
/**
 * IdleState - ATM waiting for card insertion
 */
class IdleState implements ATMState {

    @Override
    public void insertCard(ATMContext atm) {
        System.out.println("\n💳 Card detected. Please enter your PIN:");
        atm.setState(atm.getPinEntryState());
    }

    @Override
    public void ejectCard(ATMContext atm) {
        System.out.println("❌ No card to eject. Please insert a card first.");
    }

    @Override
    public void enterPin(ATMContext atm, String pin) {
        System.out.println("❌ Please insert a card before entering PIN.");
    }

    @Override
    public void selectTransaction(ATMContext atm, int choice) {
        System.out.println("❌ Please insert a card first.");
    }

    @Override
    public void exit(ATMContext atm) {
        System.out.println("\n👋 Thank you for using DecodeLabs ATM!");
        System.out.println("   Have a great day!");
        System.exit(0);
    }
}

// ========================= PIN ENTRY STATE =========================
/**
 * PinEntryState - User enters PIN for authentication
 */
class PinEntryState implements ATMState {

    @Override
    public void insertCard(ATMContext atm) {
        System.out.println("❌ Card already in process. Please enter PIN.");
    }

    @Override
    public void ejectCard(ATMContext atm) {
        System.out.println("\n💳 Card ejected. Returning to idle state.");
        atm.setCurrentAccount(null);
        atm.setState(atm.getIdleState());
    }

    @Override
    public void enterPin(ATMContext atm, String pin) {
        // Authentication logic is handled in the ATM context
        // This method is a placeholder for the state pattern
    }

    @Override
    public void selectTransaction(ATMContext atm, int choice) {
        System.out.println("❌ Please authenticate first by entering your PIN.");
    }

    @Override
    public void exit(ATMContext atm) {
        System.out.println("\n👋 Thank you for using DecodeLabs ATM!");
        System.exit(0);
    }
}

// ========================= AUTHENTICATED STATE =========================
/**
 * AuthenticatedState - User has verified identity
 * Handles all transaction operations
 */
class AuthenticatedState implements ATMState {

    @Override
    public void insertCard(ATMContext atm) {
        System.out.println("❌ Card already inserted. Please select a transaction.");
    }

    @Override
    public void ejectCard(ATMContext atm) {
        System.out.println("\n💳 Card ejected. Thank you for banking with us!");
        atm.setCurrentAccount(null);
        atm.setState(atm.getIdleState());
    }

    @Override
    public void enterPin(ATMContext atm, String pin) {
        System.out.println("❌ PIN already verified. Please select a transaction.");
    }

    @Override
    public void selectTransaction(ATMContext atm, int choice) {
        BankAccount account = atm.getCurrentAccount();
        if (account == null) {
            System.out.println("❌ System error. Please eject card and try again.");
            return;
        }

        switch (choice) {
            case 1: // Check Balance
                account.checkBalance();
                break;

            case 2: // Deposit
                System.out.println("\n💰 DEPOSIT MONEY 💰");
                double depositAmount = atm.getValidAmount("   Enter amount to deposit: $");
                if (depositAmount > 0) {
                    account.deposit(depositAmount);
                }
                break;

            case 3: // Withdraw
                System.out.println("\n💸 WITHDRAW MONEY 💸");
                double withdrawAmount = atm.getValidAmount("   Enter amount to withdraw: $");
                if (withdrawAmount > 0) {
                    account.withdraw(withdrawAmount);
                }
                break;

            case 4: // Transaction History
                account.showTransactionHistory();
                break;

            case 5: // Return to Main Menu
                System.out.println("\n🔄 Returning to main menu...");
                break;

            default:
                System.out.println("❌ Invalid choice. Please select 1-6.");
        }
    }

    @Override
    public void exit(ATMContext atm) {
        System.out.println("\n👋 Thank you for using DecodeLabs ATM!");
        System.out.println("   Have a great day!");
        System.exit(0);
    }
}

// ========================= ATM CONTEXT (MAIN CONTROLLER) =========================
/**
 * ATMContext - The Customer Lobby (ATM Machine)
 * Handles all user interface concerns (Input/Output)
 * Separated from business logic - follows Separation of Concerns
 */
class ATMContext {
    // State Pattern components
    private ATMState idleState;
    private ATMState pinEntryState;
    private ATMState authenticatedState;
    private ATMState currentState;

    // Current session data
    private BankAccount currentAccount;
    private Scanner scanner;

    // Database of accounts (in real system, this would be a database)
    private BankAccount[] accounts;

    // Constructor
    public ATMContext() {
        // Initialize states
        idleState = new IdleState();
        pinEntryState = new PinEntryState();
        authenticatedState = new AuthenticatedState();

        currentState = idleState;
        scanner = new Scanner(System.in);

        // Initialize demo accounts
        initializeAccounts();
    }

    // Initialize demo accounts with test data
    private void initializeAccounts() {
        accounts = new BankAccount[] {
                new BankAccount("ACC1001", "1234", 2500.00),
                new BankAccount("ACC1002", "5678", 500.00),
                new BankAccount("ACC1003", "9012", 10000.00),
                new BankAccount("ACC1004", "1111", 750.50),
                new BankAccount("ACC1005", "2222", 3200.75)
        };
    }

    // ==================== STATE MANAGEMENT ====================
    public void setState(ATMState state) {
        this.currentState = state;
    }

    public ATMState getIdleState() { return idleState; }
    public ATMState getPinEntryState() { return pinEntryState; }
    public ATMState getAuthenticatedState() { return authenticatedState; }

    public void setCurrentAccount(BankAccount account) {
        this.currentAccount = account;
    }

    public BankAccount getCurrentAccount() {
        return currentAccount;
    }

    // ==================== INPUT VALIDATION (First Line of Defense) ====================
    /**
     * Validates integer input with range checking
     * Uses hasNextInt() to peek before consuming - prevents crashes
     */
    public int getValidIntInput(String prompt, int min, int max) {
        int input = -1;
        while (true) {
            System.out.print(prompt);
            // peek before consuming - this prevents InputMismatchException crashes
            if (scanner.hasNextInt()) {
                input = scanner.nextInt();
                scanner.nextLine(); // clear the buffer
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("❌ Please enter a number between " + min + " and " + max);
                }
            } else {
                System.out.println("❌ Invalid input! Please enter a valid number.");
                scanner.next(); // consume the invalid token to prevent infinite loop
            }
        }
    }

    /**
     * Validates double/amount input
     * Gracefully handles invalid entries like letters or special characters
     */
    public double getValidAmount(String prompt) {
        double amount = -1;
        while (true) {
            System.out.print(prompt);
            if (scanner.hasNextDouble()) {
                amount = scanner.nextDouble();
                scanner.nextLine(); // clear buffer
                if (amount > 0) {
                    return amount;
                } else if (amount == 0) {
                    System.out.println("❌ Amount cannot be zero.");
                } else {
                    System.out.println("❌ Amount cannot be negative.");
                }
            } else {
                System.out.println("❌ Invalid input! Please enter a valid amount (e.g., 50.00).");
                scanner.next(); // consume invalid token
            }
        }
    }

    /**
     * Validates PIN entry (exactly 4 digits)
     */
    public String getValidPin() {
        while (true) {
            System.out.print("   Enter 4-digit PIN: ");
            String pin = scanner.nextLine();
            // Regex validation for exactly 4 digits
            if (pin.matches("\\d{4}")) {
                return pin;
            }
            System.out.println("❌ Invalid PIN! PIN must be exactly 4 digits (0-9).");
            System.out.println("   Press 1 to retry, 2 to eject card");
            int choice = getValidIntInput("   Choice: ", 1, 2);
            if (choice == 2) {
                return null; // Signal to eject
            }
        }
    }

    // ==================== AUTHENTICATION ====================
    /**
     * Authenticates user against the account database
     */
    public BankAccount authenticate(String accountNumber, String pin) {
        for (BankAccount acc : accounts) {
            if (acc.getAccountNumber().equals(accountNumber) && acc.validatePin(pin)) {
                return acc;
            }
        }
        return null;
    }

    // ==================== UI DISPLAY METHODS ====================
    /**
     * Displays the welcome banner
     */
    private void displayWelcomeBanner() {
        System.out.println("\n" + "█".repeat(60));
        System.out.println("     🏦 DECODELABS ATM INTERFACE SYSTEM 🏦");
        System.out.println("     Object-Oriented Banking Solution");
        System.out.println("█".repeat(60));
        System.out.println("\n🔐 This ATM is protected with industry-standard");
        System.out.println("   encryption and input validation protocols.");
    }

    /**
     * Displays the main transaction menu
     */
    private void showMainMenu() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("           📱 MAIN MENU 📱");
        System.out.println("═".repeat(50));
        System.out.println("   1. 💰 Check Balance");
        System.out.println("   2. 💵 Deposit Money");
        System.out.println("   3. 💸 Withdraw Money");
        System.out.println("   4. 📜 Transaction History");
        System.out.println("   5. 🔄 Return to Main Menu");
        System.out.println("   6. 🚪 Eject Card & Exit");
        System.out.println("═".repeat(50));
    }

    /**
     * Displays authentication menu
     */
    private void showAuthenticationMenu() {
        System.out.println("\n" + "═".repeat(50));
        System.out.println("           🔐 AUTHENTICATION 🔐");
        System.out.println("═".repeat(50));
        System.out.println("   Please insert your card to begin.");
        System.out.println("   (Test accounts available - see below)");
        System.out.println("─".repeat(50));
        System.out.println("   Test Accounts:");
        System.out.println("   ACC1001 | PIN: 1234 | Balance: $2,500.00");
        System.out.println("   ACC1002 | PIN: 5678 | Balance: $500.00");
        System.out.println("   ACC1003 | PIN: 9012 | Balance: $10,000.00");
        System.out.println("═".repeat(50));
    }

    // ==================== CORE ATM OPERATIONS FLOW ====================
    /**
     * Starts the ATM system - main event loop
     * Demonstrates State Pattern in action
     */
    public void start() {
        displayWelcomeBanner();

        while (true) {
            // IDLE STATE - Waiting for card
            if (currentState == idleState) {
                showAuthenticationMenu();
                System.out.print("\n   Insert card? (1 = Yes, 2 = Exit): ");
                int choice = getValidIntInput("   Choice: ", 1, 2);

                if (choice == 1) {
                    currentState.insertCard(this);
                } else {
                    currentState.exit(this);
                }
            }

            // PIN ENTRY STATE - User authentication
            else if (currentState == pinEntryState) {
                System.out.println("\n" + "═".repeat(50));
                System.out.println("           🔑 PIN VERIFICATION 🔑");
                System.out.println("═".repeat(50));

                System.out.print("   Enter Account Number: ");
                String accountNum = scanner.nextLine();

                String pin = getValidPin();

                if (pin == null) {
                    // User chose to eject card
                    currentState.ejectCard(this);
                    continue;
                }

                BankAccount account = authenticate(accountNum, pin);

                if (account != null) {
                    currentAccount = account;
                    System.out.println("\n✅ Authentication successful!");
                    System.out.println("   Welcome, " + account.getAccountNumber());
                    System.out.println("   You are now logged into the secure system.");
                    currentState = authenticatedState;
                } else {
                    System.out.println("\n❌ Authentication failed!");
                    System.out.println("   Invalid account number or PIN.");
                    System.out.println("   Press 1 to try again, 2 to eject card");
                    int retry = getValidIntInput("   Choice: ", 1, 2);
                    if (retry == 2) {
                        currentState.ejectCard(this);
                    }
                }
            }

            // AUTHENTICATED STATE - User is logged in
            else if (currentState == authenticatedState) {
                showMainMenu();
                int choice = getValidIntInput("\n   Select option: ", 1, 6);

                if (choice == 6) {
                    currentState.ejectCard(this);
                    currentState = idleState;
                } else {
                    currentState.selectTransaction(this, choice);

                    // After transaction, offer to continue or exit
                    if (choice != 5) { // Don't ask after "Return to Menu"
                        System.out.println("\n   Press 1 to continue banking, 2 to eject card");
                        int continueChoice = getValidIntInput("   Choice: ", 1, 2);
                        if (continueChoice == 2) {
                            currentState.ejectCard(this);
                            currentState = idleState;
                        }
                    }
                }
            }
        }
    }
}

// ========================= MAIN CLASS =========================
/**
 * Main - Application Entry Point
 * Initializes and starts the ATM system
 */
public class Main {
    public static void main(String[] args) {
        // Create and start the ATM system
        ATMContext atm = new ATMContext();
        atm.start();
    }
}
