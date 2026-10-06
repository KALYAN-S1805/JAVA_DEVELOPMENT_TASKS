import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    static final Scanner sc = new Scanner(System.in);

    static final String CUSTOMER_FILE =
            "bank_customers.txt";

    static final String ACCOUNT_FILE =
            "bank_accounts.txt";

    static final String TRANSACTION_FILE =
            "bank_transactions.txt";

    static final ArrayList<String[]> customers =
            new ArrayList<>();

    static final ArrayList<String[]> accounts =
            new ArrayList<>();

    static final ArrayList<String[]> transactions =
            new ArrayList<>();

    static int nextCustomerId = 1;
    static int nextAccountNo = 10001;
    static int nextTransactionId = 50001;

    static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern(
                    "dd-MM-yyyy HH:mm:ss"
            );

    public static void main(String[] args) {

        loadData();

        while (true) {

            printMenu();

            try {

                int choice =
                        readInt("Choice: ");

                switch (choice) {

                    case 1 -> registerCustomer();
                    case 2 -> createAccount();
                    case 3 -> viewCustomers();
                    case 4 -> viewAccounts();
                    case 5 -> deposit();
                    case 6 -> withdraw();
                    case 7 -> transfer();
                    case 8 -> checkBalance();
                    case 9 -> transactionHistory();
                    case 10 -> closeAccount();
                    case 11 -> bankReport();

                    case 12 -> {
                        saveData();
                        System.out.println(
                                "Data saved. Goodbye!"
                        );
                        return;
                    }

                    default ->
                            System.out.println(
                                    "Choose 1 to 12."
                            );
                }

            } catch (Exception e) {

                System.out.println(
                        "ERROR: " +
                        e.getMessage()
                );
            }
        }
    }

    // =========================================================
    // MENU
    // =========================================================

    static void printMenu() {

        System.out.println(
                "\n============================================"
        );

        System.out.println(
                "         BANKING MANAGEMENT SYSTEM"
        );

        System.out.println(
                "============================================"
        );

        System.out.println(
                "1. Register Customer"
        );

        System.out.println(
                "2. Create Account"
        );

        System.out.println(
                "3. View Customers"
        );

        System.out.println(
                "4. View Accounts"
        );

        System.out.println(
                "5. Deposit"
        );

        System.out.println(
                "6. Withdraw"
        );

        System.out.println(
                "7. Transfer Funds"
        );

        System.out.println(
                "8. Check Balance"
        );

        System.out.println(
                "9. Transaction History"
        );

        System.out.println(
                "10. Close Account"
        );

        System.out.println(
                "11. Bank Report"
        );

        System.out.println(
                "12. Save & Exit"
        );
    }

    // =========================================================
    // INPUT
    // =========================================================

    static int readInt(String prompt) {

        while (true) {

            try {

                System.out.print(prompt);

                return Integer.parseInt(
                        sc.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Enter a valid integer."
                );
            }
        }
    }

    static double readDouble(String prompt) {

        while (true) {

            try {

                System.out.print(prompt);

                return Double.parseDouble(
                        sc.nextLine().trim()
                );

            } catch (NumberFormatException e) {

                System.out.println(
                        "Enter a valid amount."
                );
            }
        }
    }

    static String readText(String prompt) {

        System.out.print(prompt);

        String value =
                sc.nextLine().trim();

        if (value.isEmpty()) {

            throw new IllegalArgumentException(
                    "Input cannot be empty."
            );
        }

        return value.replace("|", "/");
    }

    static double readPositiveAmount(
            String prompt
    ) {

        double amount =
                readDouble(prompt);

        if (amount <= 0) {

            throw new IllegalArgumentException(
                    "Amount must be greater than zero."
            );
        }

        return amount;
    }

    // =========================================================
    // CUSTOMER
    // =========================================================

    static void registerCustomer() {

        System.out.println(
                "\n========== CUSTOMER REGISTRATION =========="
        );

        String name =
                readText("Name: ");

        String phone =
                readText("Phone: ");

        String email =
                readText("Email: ");

        String address =
                readText("Address: ");

        if (phone.length() < 10) {

            throw new IllegalArgumentException(
                    "Invalid phone number."
            );
        }

        if (!email.contains("@")) {

            throw new IllegalArgumentException(
                    "Invalid email address."
            );
        }

        customers.add(
                new String[]{
                        String.valueOf(
                                nextCustomerId++
                        ),
                        name,
                        phone,
                        email,
                        address
                }
        );

        saveData();

        System.out.println(
                "Customer registered successfully."
        );

        System.out.println(
                "Customer ID: " +
                (nextCustomerId - 1)
        );
    }

    static void viewCustomers() {

        System.out.println(
                "\n================ CUSTOMERS ================"
        );

        if (customers.isEmpty()) {

            System.out.println(
                    "No customers."
            );

            return;
        }

        for (String[] customer : customers) {

            System.out.printf(
                    "ID: %-4s Name: %-20s Phone: %-12s Email: %-25s Address: %s%n",
                    customer[0],
                    customer[1],
                    customer[2],
                    customer[3],
                    customer[4]
            );
        }
    }

    static String[] findCustomer(int id) {

        for (String[] customer : customers) {

            if (
                    Integer.parseInt(customer[0])
                            == id
            ) {

                return customer;
            }
        }

        return null;
    }

    // =========================================================
    // ACCOUNT
    // =========================================================

    static void createAccount() {

        if (customers.isEmpty()) {

            throw new IllegalArgumentException(
                    "Register a customer first."
            );
        }

        viewCustomers();

        int customerId =
                readInt(
                        "Customer ID: "
                );

        if (
                findCustomer(customerId)
                        == null
        ) {

            throw new IllegalArgumentException(
                    "Customer not found."
            );
        }

        System.out.println(
                "1. SAVINGS - Minimum balance ₹500"
        );

        System.out.println(
                "2. CURRENT - Minimum balance ₹1000"
        );

        int choice =
                readInt("Account type: ");

        String type;

        if (choice == 1) {

            type = "SAVINGS";

        } else if (choice == 2) {

            type = "CURRENT";

        } else {

            throw new IllegalArgumentException(
                    "Invalid account type."
            );
        }

        double minimum =
                type.equals("SAVINGS")
                        ? 500
                        : 1000;

        double opening =
                readDouble(
                        "Opening deposit: ₹"
                );

        if (opening < minimum) {

            throw new IllegalArgumentException(
                    "Opening deposit must be at least ₹"
                            + minimum
            );
        }

        int accountNumber =
                nextAccountNo++;

        accounts.add(
                new String[]{
                        String.valueOf(
                                accountNumber
                        ),
                        String.valueOf(
                                customerId
                        ),
                        type,
                        String.valueOf(
                                opening
                        ),
                        "ACTIVE"
                }
        );

        addTransaction(
                "ACCOUNT_OPENED",
                String.valueOf(accountNumber),
                "-",
                opening,
                "Opening deposit"
        );

        saveData();

        System.out.println(
                "Account created successfully."
        );

        System.out.println(
                "Account Number: " +
                accountNumber
        );
    }

    static String[] findAccount(
            int accountNumber
    ) {

        for (String[] account : accounts) {

            if (
                    Integer.parseInt(
                            account[0]
                    )
                            == accountNumber
            ) {

                return account;
            }
        }

        return null;
    }

    static double minimumBalance(
            String[] account
    ) {

        if (
                account[2]
                        .equals("SAVINGS")
        ) {

            return 500;

        } else {

            return 1000;
        }
    }

    static String[] getActiveAccount() {

        int accountNumber =
                readInt(
                        "Account number: "
                );

        String[] account =
                findAccount(
                        accountNumber
                );

        if (account == null) {

            throw new IllegalArgumentException(
                    "Account not found."
            );
        }

        if (
                !account[4]
                        .equals("ACTIVE")
        ) {

            throw new IllegalArgumentException(
                    "Account is closed."
            );
        }

        return account;
    }

    static void viewAccounts() {

        System.out.println(
                "\n================ ACCOUNTS ================"
        );

        if (accounts.isEmpty()) {

            System.out.println(
                    "No accounts."
            );

            return;
        }

        for (String[] account : accounts) {

            String[] customer =
                    findCustomer(
                            Integer.parseInt(
                                    account[1]
                            )
                    );

            String customerName =
                    customer == null
                            ? "Unknown"
                            : customer[1];

            System.out.printf(
                    "Account: %-7s Customer: %-20s Type: %-8s Balance: ₹%-12.2f Status: %s%n",
                    account[0],
                    customerName,
                    account[2],
                    Double.parseDouble(
                            account[3]
                    ),
                    account[4]
            );
        }
    }

    // =========================================================
    // DEPOSIT
    // =========================================================

    static void deposit() {

        String[] account =
                getActiveAccount();

        double amount =
                readPositiveAmount(
                        "Deposit amount: ₹"
                );

        double balance =
                Double.parseDouble(
                        account[3]
                );

        account[3] =
                String.valueOf(
                        balance + amount
                );

        addTransaction(
                "DEPOSIT",
                account[0],
                "-",
                amount,
                "Cash deposit"
        );

        saveData();

        System.out.printf(
                "Deposit successful. New balance: ₹%.2f%n",
                Double.parseDouble(account[3])
        );
    }

    // =========================================================
    // WITHDRAW
    // =========================================================

    static void withdraw() {

        String[] account =
                getActiveAccount();

        double amount =
                readPositiveAmount(
                        "Withdrawal amount: ₹"
                );

        double balance =
                Double.parseDouble(
                        account[3]
                );

        double minimum =
                minimumBalance(account);

        if (
                balance - amount
                        < minimum
        ) {

            throw new IllegalArgumentException(
                    "Minimum balance ₹"
                            + minimum
                            + " must remain."
            );
        }

        account[3] =
                String.valueOf(
                        balance - amount
                );

        addTransaction(
                "WITHDRAW",
                account[0],
                "-",
                amount,
                "Cash withdrawal"
        );

        saveData();

        System.out.printf(
                "Withdrawal successful. New balance: ₹%.2f%n",
                Double.parseDouble(account[3])
        );
    }

    // =========================================================
    // TRANSFER
    // =========================================================

    static void transfer() {

        String[] sender =
                getActiveAccount();

        int receiverNumber =
                readInt(
                        "Receiver account number: "
                );

        String[] receiver =
                findAccount(
                        receiverNumber
                );

        if (receiver == null) {

            throw new IllegalArgumentException(
                    "Receiver account not found."
            );
        }

        if (
                sender[0]
                        .equals(receiver[0])
        ) {

            throw new IllegalArgumentException(
                    "Cannot transfer to same account."
            );
        }

        if (
                !receiver[4]
                        .equals("ACTIVE")
        ) {

            throw new IllegalArgumentException(
                    "Receiver account is closed."
            );
        }

        double amount =
                readPositiveAmount(
                        "Transfer amount: ₹"
                );

        double senderBalance =
                Double.parseDouble(
                        sender[3]
                );

        double minimum =
                minimumBalance(sender);

        if (
                senderBalance - amount
                        < minimum
        ) {

            throw new IllegalArgumentException(
                    "Insufficient transferable balance. "
                            +
                    "Minimum balance ₹"
                            +
                    minimum
                            +
                    " must remain."
            );
        }

        sender[3] =
                String.valueOf(
                        senderBalance - amount
                );

        double receiverBalance =
                Double.parseDouble(
                        receiver[3]
                );

        receiver[3] =
                String.valueOf(
                        receiverBalance + amount
                );

        addTransaction(
                "TRANSFER_OUT",
                sender[0],
                receiver[0],
                amount,
                "Fund transfer"
        );

        addTransaction(
                "TRANSFER_IN",
                receiver[0],
                sender[0],
                amount,
                "Fund transfer"
        );

        saveData();

        System.out.println(
                "Transfer successful."
        );
    }

    // =========================================================
    // BALANCE
    // =========================================================

    static void checkBalance() {

        String[] account =
                getActiveAccount();

        System.out.printf(
                "Account: %s | Type: %s | Balance: ₹%.2f%n",
                account[0],
                account[2],
                Double.parseDouble(
                        account[3]
                )
        );
    }

    // =========================================================
    // TRANSACTION HISTORY
    // =========================================================

    static void transactionHistory() {

        int accountNumber =
                readInt(
                        "Account number: "
                );

        if (
                findAccount(accountNumber)
                        == null
        ) {

            throw new IllegalArgumentException(
                    "Account not found."
            );
        }

        boolean found = false;

        System.out.println(
                "\n============= TRANSACTION HISTORY ============="
        );

        for (String[] transaction :
                transactions) {

            if (
                    transaction[2]
                            .equals(
                                    String.valueOf(
                                            accountNumber
                                    )
                            )
                    ||
                    transaction[3]
                            .equals(
                                    String.valueOf(
                                            accountNumber
                                    )
                            )
            ) {

                System.out.printf(
                        "ID: %-7s Type: %-16s From: %-7s To: %-7s Amount: ₹%-10.2f Date: %s%n",
                        transaction[0],
                        transaction[1],
                        transaction[2],
                        transaction[3],
                        Double.parseDouble(
                                transaction[4]
                        ),
                        transaction[5]
                );

                System.out.println(
                        "    " +
                        transaction[6]
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No transactions found."
            );
        }
    }

    // =========================================================
    // CLOSE ACCOUNT
    // =========================================================

    static void closeAccount() {

        String[] account =
                getActiveAccount();

        double balance =
                Double.parseDouble(
                        account[3]
                );

        if (balance != 0) {

            throw new IllegalArgumentException(
                    "Account can only be closed when "
                            +
                    "balance is ₹0.00."
            );
        }

        account[4] = "CLOSED";

        addTransaction(
                "ACCOUNT_CLOSED",
                account[0],
                "-",
                0,
                "Account closed"
        );

        saveData();

        System.out.println(
                "Account closed successfully."
        );
    }

    // =========================================================
    // BANK REPORT
    // =========================================================

    static void bankReport() {

        int active = 0;
        int closed = 0;
        double totalBalance = 0;

        for (String[] account :
                accounts) {

            if (
                    account[4]
                            .equals("ACTIVE")
            ) {

                active++;

            } else {

                closed++;
            }

            totalBalance +=
                    Double.parseDouble(
                            account[3]
                    );
        }

        System.out.println(
                "\n============== BANK REPORT =============="
        );

        System.out.println(
                "Customers       : " +
                customers.size()
        );

        System.out.println(
                "Accounts        : " +
                accounts.size()
        );

        System.out.println(
                "Active Accounts : " +
                active
        );

        System.out.println(
                "Closed Accounts : " +
                closed
        );

        System.out.printf(
                "Total Balance   : ₹%.2f%n",
                totalBalance
        );

        System.out.println(
                "Transactions    : " +
                transactions.size()
        );
    }

    // =========================================================
    // TRANSACTION CREATION
    // =========================================================

    static void addTransaction(
            String type,
            String from,
            String to,
            double amount,
            String remark
    ) {

        transactions.add(
                new String[]{
                        String.valueOf(
                                nextTransactionId++
                        ),
                        type,
                        from,
                        to,
                        String.valueOf(
                                amount
                        ),
                        LocalDateTime.now()
                                .format(FMT),
                        remark
                }
        );
    }

    // =========================================================
    // FILE SAVE
    // =========================================================

    static void saveData() {

        writeFile(
                CUSTOMER_FILE,
                customers
        );

        writeFile(
                ACCOUNT_FILE,
                accounts
        );

        writeFile(
                TRANSACTION_FILE,
                transactions
        );
    }

    static void writeFile(
            String file,
            ArrayList<String[]> data
    ) {

        try (
                PrintWriter out =
                        new PrintWriter(
                                new FileWriter(file)
                        )
        ) {

            for (String[] row : data) {

                out.println(
                        String.join(
                                "|",
                                row
                        )
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not save " +
                    file
            );
        }
    }

    // =========================================================
    // FILE LOAD
    // =========================================================

    static void loadData() {

        loadFile(
                CUSTOMER_FILE,
                customers,
                5
        );

        loadFile(
                ACCOUNT_FILE,
                accounts,
                5
        );

        loadFile(
                TRANSACTION_FILE,
                transactions,
                7
        );

        for (String[] customer :
                customers) {

            nextCustomerId =
                    Math.max(
                            nextCustomerId,
                            Integer.parseInt(
                                    customer[0]
                            ) + 1
                    );
        }

        for (String[] account :
                accounts) {

            nextAccountNo =
                    Math.max(
                            nextAccountNo,
                            Integer.parseInt(
                                    account[0]
                            ) + 1
                    );
        }

        for (String[] transaction :
                transactions) {

            nextTransactionId =
                    Math.max(
                            nextTransactionId,
                            Integer.parseInt(
                                    transaction[0]
                            ) + 1
                    );
        }
    }

    static void loadFile(
            String file,
            ArrayList<String[]> list,
            int fieldCount
    ) {

        File f =
                new File(file);

        if (!f.exists()) {
            return;
        }

        try (
                BufferedReader br =
                        new BufferedReader(
                                new FileReader(f)
                        )
        ) {

            String line;

            while (
                    (line = br.readLine())
                            != null
            ) {

                String[] data =
                        line.split(
                                "\\|",
                                -1
                        );

                if (
                        data.length
                                == fieldCount
                ) {

                    list.add(data);
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not load " +
                    file
            );
        }
    }
}