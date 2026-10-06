import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    static final Scanner sc = new Scanner(System.in);
    static final String FILE = "payroll_data.txt";

    static final ArrayList<String[]> employees =
            new ArrayList<>();

    static int nextId = 1001;

    static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void main(String[] args) {

        loadData();

        if (employees.isEmpty()) {
            seedEmployees();
        }

        while (true) {

            printMenu();

            try {

                int choice =
                        readInt("Choice: ");

                switch (choice) {

                    case 1 -> addEmployee();
                    case 2 -> viewEmployees();
                    case 3 -> searchEmployee();
                    case 4 -> updateEmployee();
                    case 5 -> removeEmployee();
                    case 6 -> generatePayroll();
                    case 7 -> salarySlip();
                    case 8 -> payrollSummary();

                    case 9 -> {
                        saveData();
                        System.out.println(
                                "Data saved. Goodbye!"
                        );
                        return;
                    }

                    default ->
                            System.out.println(
                                    "Choose 1 to 9."
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
                "\n============================================="
        );

        System.out.println(
                "        EMPLOYEE PAYROLL MANAGEMENT"
        );

        System.out.println(
                "============================================="
        );

        System.out.println("1. Add Employee");
        System.out.println("2. View Employees");
        System.out.println("3. Search Employee");
        System.out.println("4. Update Employee");
        System.out.println("5. Remove Employee");
        System.out.println("6. Generate Payroll");
        System.out.println("7. Salary Slip");
        System.out.println("8. Payroll Summary");
        System.out.println("9. Save & Exit");
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

    static double readNonNegative(String prompt) {

        double value =
                readDouble(prompt);

        if (value < 0) {

            throw new IllegalArgumentException(
                    "Value cannot be negative."
            );
        }

        return value;
    }

    // =========================================================
    // DEMO EMPLOYEES
    // =========================================================

    static void seedEmployees() {

        addEmployeeData(
                "Asha",
                "FULL_TIME",
                40000,
                2000,
                1500,
                3000
        );

        addEmployeeData(
                "Rahul",
                "PART_TIME",
                22000,
                1000,
                500,
                1000
        );

        addEmployeeData(
                "Meera",
                "COMMISSION",
                30000,
                1500,
                1000,
                2000
        );

        saveData();
    }

    static void addEmployeeData(
            String name,
            String type,
            double basic,
            double allowance,
            double deduction,
            double bonus
    ) {

        employees.add(
                new String[]{
                        String.valueOf(nextId++),
                        name,
                        type,
                        String.valueOf(basic),
                        String.valueOf(allowance),
                        String.valueOf(deduction),
                        String.valueOf(bonus)
                }
        );
    }

    // =========================================================
    // ADD EMPLOYEE
    // =========================================================

    static void addEmployee() {

        System.out.println(
                "\n=========== ADD EMPLOYEE ==========="
        );

        String name =
                readText("Name: ");

        String type =
                readEmployeeType();

        double basic =
                readNonNegative(
                        "Basic salary: ₹"
                );

        double allowance =
                readNonNegative(
                        "Allowance: ₹"
                );

        double deduction =
                readNonNegative(
                        "Deduction: ₹"
                );

        double bonus =
                readNonNegative(
                        "Bonus: ₹"
                );

        addEmployeeData(
                name,
                type,
                basic,
                allowance,
                deduction,
                bonus
        );

        saveData();

        System.out.println(
                "Employee added successfully."
        );

        System.out.println(
                "Employee ID: " +
                (nextId - 1)
        );
    }

    // =========================================================
    // EMPLOYEE TYPE
    // =========================================================

    static String readEmployeeType() {

        System.out.println(
                "1. FULL_TIME"
        );

        System.out.println(
                "2. PART_TIME"
        );

        System.out.println(
                "3. COMMISSION"
        );

        int choice =
                readInt("Type: ");

        return switch (choice) {

            case 1 -> "FULL_TIME";
            case 2 -> "PART_TIME";
            case 3 -> "COMMISSION";

            default ->
                    throw new IllegalArgumentException(
                            "Invalid employee type."
                    );
        };
    }

    // =========================================================
    // VIEW EMPLOYEES
    // =========================================================

    static void viewEmployees() {

        System.out.println(
                "\n================ EMPLOYEES ================"
        );

        if (employees.isEmpty()) {

            System.out.println(
                    "No employees."
            );

            return;
        }

        System.out.printf(
                "%-7s %-20s %-12s %-12s %-12s%n",
                "ID",
                "Name",
                "Type",
                "Basic",
                "Net Salary"
        );

        System.out.println(
                "----------------------------------------------------------------"
        );

        for (String[] employee : employees) {

            System.out.printf(
                    "%-7s %-20s %-12s ₹%-11.2f ₹%-11.2f%n",
                    employee[0],
                    employee[1],
                    employee[2],
                    Double.parseDouble(employee[3]),
                    calculateSalary(employee)
            );
        }
    }

    // =========================================================
    // FIND EMPLOYEE
    // =========================================================

    static String[] findEmployee(int id) {

        for (String[] employee : employees) {

            if (
                    Integer.parseInt(employee[0])
                            == id
            ) {

                return employee;
            }
        }

        return null;
    }

    // =========================================================
    // SEARCH
    // =========================================================

    static void searchEmployee() {

        String keyword =
                readText(
                        "Search name/type: "
                ).toLowerCase();

        boolean found = false;

        for (String[] employee : employees) {

            if (
                    employee[1]
                            .toLowerCase()
                            .contains(keyword)
                    ||
                    employee[2]
                            .toLowerCase()
                            .contains(keyword)
            ) {

                System.out.println(
                        "ID: " + employee[0]
                        + " | Name: " + employee[1]
                        + " | Type: " + employee[2]
                        + " | Net Salary: ₹"
                        + String.format(
                                "%.2f",
                                calculateSalary(employee)
                        )
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No matching employee."
            );
        }
    }

    // =========================================================
    // UPDATE
    // =========================================================

    static void updateEmployee() {

        viewEmployees();

        int id =
                readInt(
                        "Employee ID: "
                );

        String[] employee =
                findEmployee(id);

        if (employee == null) {

            throw new IllegalArgumentException(
                    "Employee not found."
            );
        }

        employee[1] =
                readText("New name: ");

        employee[2] =
                readEmployeeType();

        employee[3] =
                String.valueOf(
                        readNonNegative(
                                "New basic salary: ₹"
                        )
                );

        employee[4] =
                String.valueOf(
                        readNonNegative(
                                "New allowance: ₹"
                        )
                );

        employee[5] =
                String.valueOf(
                        readNonNegative(
                                "New deduction: ₹"
                        )
                );

        employee[6] =
                String.valueOf(
                        readNonNegative(
                                "New bonus: ₹"
                        )
                );

        saveData();

        System.out.println(
                "Employee updated."
        );
    }

    // =========================================================
    // REMOVE
    // =========================================================

    static void removeEmployee() {

        viewEmployees();

        int id =
                readInt(
                        "Employee ID: "
                );

        String[] employee =
                findEmployee(id);

        if (employee == null) {

            throw new IllegalArgumentException(
                    "Employee not found."
            );
        }

        employees.remove(employee);

        saveData();

        System.out.println(
                "Employee removed."
        );
    }

    // =========================================================
    // SALARY CALCULATION
    // =========================================================

    static double calculateSalary(
            String[] employee
    ) {

        double basic =
                Double.parseDouble(
                        employee[3]
                );

        double allowance =
                Double.parseDouble(
                        employee[4]
                );

        double deduction =
                Double.parseDouble(
                        employee[5]
                );

        double bonus =
                Double.parseDouble(
                        employee[6]
                );

        double typeBenefit = 0;

        switch (employee[2]) {

            case "FULL_TIME":

                // HRA 20% + DA 10%
                typeBenefit =
                        basic * 0.30;

                break;

            case "PART_TIME":

                // No percentage benefit
                typeBenefit = 0;

                break;

            case "COMMISSION":

                // 10% commission
                typeBenefit =
                        basic * 0.10;

                break;

            default:

                throw new IllegalArgumentException(
                        "Unknown employee type."
                );
        }

        double gross =
                basic
                + allowance
                + bonus
                + typeBenefit;

        double net =
                gross - deduction;

        return Math.max(
                0,
                net
        );
    }

    // =========================================================
    // PAYROLL
    // =========================================================

    static void generatePayroll() {

        if (employees.isEmpty()) {

            System.out.println(
                    "No employees."
            );

            return;
        }

        double total = 0;

        System.out.println(
                "\n=============== PAYROLL ==============="
        );

        for (String[] employee : employees) {

            double salary =
                    calculateSalary(employee);

            total += salary;

            System.out.printf(
                    "ID: %-6s Name: %-20s Type: %-12s Net: ₹%.2f%n",
                    employee[0],
                    employee[1],
                    employee[2],
                    salary
            );
        }

        System.out.println(
                "--------------------------------------"
        );

        System.out.printf(
                "Total Payroll: ₹%.2f%n",
                total
        );

        System.out.println(
                "Generated: " +
                LocalDateTime.now().format(FMT)
        );
    }

    // =========================================================
    // SALARY SLIP
    // =========================================================

    static void salarySlip() {

        int id =
                readInt(
                        "Employee ID: "
                );

        String[] employee =
                findEmployee(id);

        if (employee == null) {

            throw new IllegalArgumentException(
                    "Employee not found."
            );
        }

        double basic =
                Double.parseDouble(
                        employee[3]
                );

        double allowance =
                Double.parseDouble(
                        employee[4]
                );

        double deduction =
                Double.parseDouble(
                        employee[5]
                );

        double bonus =
                Double.parseDouble(
                        employee[6]
                );

        double benefit;

        if (employee[2].equals("FULL_TIME")) {

            benefit =
                    basic * 0.30;

        } else if (
                employee[2].equals("COMMISSION")
        ) {

            benefit =
                    basic * 0.10;

        } else {

            benefit = 0;
        }

        double gross =
                basic
                + allowance
                + bonus
                + benefit;

        double net =
                Math.max(
                        0,
                        gross - deduction
                );

        System.out.println(
                "\n=============== SALARY SLIP ==============="
        );

        System.out.println(
                "Employee ID : " + employee[0]
        );

        System.out.println(
                "Name        : " + employee[1]
        );

        System.out.println(
                "Type        : " + employee[2]
        );

        System.out.printf(
                "Basic       : ₹%.2f%n",
                basic
        );

        System.out.printf(
                "Allowance   : ₹%.2f%n",
                allowance
        );

        System.out.printf(
                "Type Benefit: ₹%.2f%n",
                benefit
        );

        System.out.printf(
                "Bonus       : ₹%.2f%n",
                bonus
        );

        System.out.printf(
                "Deduction   : ₹%.2f%n",
                deduction
        );

        System.out.printf(
                "NET SALARY  : ₹%.2f%n",
                net
        );
    }

    // =========================================================
    // SUMMARY
    // =========================================================

    static void payrollSummary() {

        double total = 0;

        long fullTime = 0;
        long partTime = 0;
        long commission = 0;

        for (String[] employee : employees) {

            total +=
                    calculateSalary(employee);

            if (
                    employee[2]
                            .equals("FULL_TIME")
            ) {

                fullTime++;

            } else if (
                    employee[2]
                            .equals("PART_TIME")
            ) {

                partTime++;

            } else if (
                    employee[2]
                            .equals("COMMISSION")
            ) {

                commission++;
            }
        }

        System.out.println(
                "\n============== PAYROLL SUMMARY =============="
        );

        System.out.println(
                "Employees       : " +
                employees.size()
        );

        System.out.println(
                "Full Time       : " +
                fullTime
        );

        System.out.println(
                "Part Time       : " +
                partTime
        );

        System.out.println(
                "Commission      : " +
                commission
        );

        System.out.printf(
                "Monthly Payroll : ₹%.2f%n",
                total
        );
    }

    // =========================================================
    // FILE HANDLING
    // =========================================================

    static void saveData() {

        try (
                PrintWriter out =
                        new PrintWriter(
                                new FileWriter(FILE)
                        )
        ) {

            for (String[] employee : employees) {

                out.println(
                        String.join(
                                "|",
                                employee
                        )
                );
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not save data."
            );
        }
    }

    static void loadData() {

        File file =
                new File(FILE);

        if (!file.exists()) {
            return;
        }

        try (
                BufferedReader br =
                        new BufferedReader(
                                new FileReader(file)
                        )
        ) {

            String line;

            while (
                    (line = br.readLine())
                            != null
            ) {

                String[] employee =
                        line.split(
                                "\\|",
                                -1
                        );

                if (employee.length == 7) {

                    employees.add(employee);

                    nextId =
                            Math.max(
                                    nextId,
                                    Integer.parseInt(
                                            employee[0]
                                    ) + 1
                            );
                }
            }

        } catch (Exception e) {

            employees.clear();
            nextId = 1001;

            System.out.println(
                    "Old payroll data could not be loaded."
            );
        }
    }
}