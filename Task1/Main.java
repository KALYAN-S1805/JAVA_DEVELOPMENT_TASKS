import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {

    static final Scanner sc = new Scanner(System.in);
    static final String FILE = "ecommerce_data.txt";

    static final ArrayList<String[]> products = new ArrayList<>();
    static final ArrayList<String[]> customers = new ArrayList<>();
    static final ArrayList<String[]> orders = new ArrayList<>();

    static int nextProductId = 1;
    static int nextCustomerId = 1;
    static int nextOrderId = 1001;

    static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public static void main(String[] args) {

        loadData();

        if (products.isEmpty()) {
            seedProducts();
        }

        while (true) {

            printMenu();

            try {
                int choice = readInt("Choice: ");

                switch (choice) {
                    case 1 -> browseProducts();
                    case 2 -> searchProducts();
                    case 3 -> filterProducts();
                    case 4 -> addProduct();
                    case 5 -> updateStock();
                    case 6 -> addCustomer();
                    case 7 -> viewCustomers();
                    case 8 -> createOrder();
                    case 9 -> viewOrders();
                    case 10 -> updateOrderStatus();
                    case 11 -> salesReport();
                    case 12 -> {
                        saveData();
                        System.out.println("Data saved. Goodbye!");
                        return;
                    }
                    default ->
                            System.out.println("Please choose 1 to 12.");
                }

            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    static void printMenu() {

        System.out.println("\n==============================================");
        System.out.println("       E-COMMERCE MANAGEMENT SYSTEM");
        System.out.println("==============================================");
        System.out.println("1. Browse Products");
        System.out.println("2. Search Products");
        System.out.println("3. Filter Products");
        System.out.println("4. Add Product");
        System.out.println("5. Update Stock");
        System.out.println("6. Register Customer");
        System.out.println("7. View Customers");
        System.out.println("8. Create Order / Shopping Cart");
        System.out.println("9. View Orders");
        System.out.println("10. Update Order Status");
        System.out.println("11. Sales Report");
        System.out.println("12. Save & Exit");
    }

    static int readInt(String prompt) {

        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid integer.");
            }
        }
    }

    static double readDouble(String prompt) {

        while (true) {
            try {
                System.out.print(prompt);
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a valid amount.");
            }
        }
    }

    static String readText(String prompt) {

        System.out.print(prompt);
        String value = sc.nextLine().trim();

        if (value.isEmpty()) {
            throw new IllegalArgumentException(
                    "Input cannot be empty."
            );
        }

        return value.replace("|", "/");
    }

    // =========================================================
    // DEMO PRODUCTS
    // =========================================================

    static void seedProducts() {

        addProductData(
                "Wireless Headphones",
                "Electronics",
                2499,
                15
        );

        addProductData(
                "Mechanical Keyboard",
                "Electronics",
                3999,
                10
        );

        addProductData(
                "Running Shoes",
                "Sports",
                2899,
                20
        );

        addProductData(
                "Cotton T-Shirt",
                "Clothing",
                799,
                30
        );

        addProductData(
                "Java Programming Book",
                "Books",
                699,
                25
        );

        addProductData(
                "Coffee Maker",
                "Home",
                4499,
                8
        );

        saveData();
    }

    static void addProductData(
            String name,
            String category,
            double price,
            int stock
    ) {

        products.add(
                new String[]{
                        String.valueOf(nextProductId++),
                        name,
                        category,
                        String.valueOf(price),
                        String.valueOf(stock)
                }
        );
    }

    // =========================================================
    // PRODUCTS
    // =========================================================

    static void browseProducts() {

        System.out.println("\n================ PRODUCTS ================");

        if (products.isEmpty()) {
            System.out.println("No products available.");
            return;
        }

        printProductHeader();

        for (String[] product : products) {
            printProduct(product);
        }
    }

    static void printProductHeader() {

        System.out.printf(
                "%-5s %-25s %-16s %-12s %-8s%n",
                "ID",
                "Product",
                "Category",
                "Price",
                "Stock"
        );

        System.out.println(
                "----------------------------------------------------------------"
        );
    }

    static void printProduct(String[] product) {

        System.out.printf(
                "%-5s %-25s %-16s ₹%-11.2f %-8s%n",
                product[0],
                product[1],
                product[2],
                Double.parseDouble(product[3]),
                product[4]
        );
    }

    static String[] findProduct(int id) {

        for (String[] product : products) {

            if (Integer.parseInt(product[0]) == id) {
                return product;
            }
        }

        return null;
    }

    static void searchProducts() {

        String keyword =
                readText("Search product/category: ")
                        .toLowerCase();

        boolean found = false;

        System.out.println("\nSEARCH RESULTS");

        for (String[] product : products) {

            if (
                    product[1]
                            .toLowerCase()
                            .contains(keyword)
                    ||
                    product[2]
                            .toLowerCase()
                            .contains(keyword)
            ) {

                if (!found) {
                    printProductHeader();
                }

                printProduct(product);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching product found.");
        }
    }

    static void filterProducts() {

        System.out.println("\n1. Category");
        System.out.println("2. Price Range");
        System.out.println("3. In Stock");

        int choice = readInt("Filter: ");

        if (choice == 1) {
            filterByCategory();
            return;
        }

        boolean found = false;

        if (choice == 2) {

            double min = readDouble("Minimum price: ");
            double max = readDouble("Maximum price: ");

            if (min < 0 || max < min) {
                throw new IllegalArgumentException(
                        "Invalid price range."
                );
            }

            for (String[] product : products) {

                double price =
                        Double.parseDouble(product[3]);

                if (price >= min && price <= max) {

                    printProduct(product);
                    found = true;
                }
            }

        } else if (choice == 3) {

            for (String[] product : products) {

                if (Integer.parseInt(product[4]) > 0) {

                    printProduct(product);
                    found = true;
                }
            }

        } else {

            throw new IllegalArgumentException(
                    "Invalid filter."
            );
        }

        if (!found) {
            System.out.println("No products matched.");
        }
    }

    static void filterByCategory() {

        String category =
                readText("Category: ");

        boolean found = false;

        for (String[] product : products) {

            if (
                    product[2]
                            .equalsIgnoreCase(category)
            ) {

                printProduct(product);
                found = true;
            }
        }

        if (!found) {
            System.out.println(
                    "No products found in this category."
            );
        }
    }

    static void addProduct() {

        System.out.println("\n============= ADD PRODUCT =============");

        String name =
                readText("Product name: ");

        String category =
                readText("Category: ");

        double price =
                readDouble("Price: ₹");

        int stock =
                readInt("Stock: ");

        if (price < 0 || stock < 0) {

            throw new IllegalArgumentException(
                    "Price and stock cannot be negative."
            );
        }

        addProductData(
                name,
                category,
                price,
                stock
        );

        saveData();

        System.out.println(
                "Product added. ID: " +
                (nextProductId - 1)
        );
    }

    static void updateStock() {

        browseProducts();

        int id =
                readInt("Product ID: ");

        String[] product =
                findProduct(id);

        if (product == null) {

            throw new IllegalArgumentException(
                    "Product not found."
            );
        }

        int quantity =
                readInt("Quantity to add: ");

        if (quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be positive."
            );
        }

        int current =
                Integer.parseInt(product[4]);

        product[4] =
                String.valueOf(
                        current + quantity
                );

        saveData();

        System.out.println(
                "Stock updated successfully."
        );
    }

    // =========================================================
    // CUSTOMERS
    // =========================================================

    static void addCustomer() {

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
                        String.valueOf(nextCustomerId++),
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
    }

    static void viewCustomers() {

        System.out.println("\n================ CUSTOMERS ================");

        if (customers.isEmpty()) {
            System.out.println("No customers.");
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
    // ORDERS / SHOPPING CART
    // =========================================================

    static void createOrder() {

        if (customers.isEmpty()) {

            throw new IllegalArgumentException(
                    "Register a customer first."
            );
        }

        viewCustomers();

        int customerId =
                readInt("Customer ID: ");

        String[] customer =
                findCustomer(customerId);

        if (customer == null) {

            throw new IllegalArgumentException(
                    "Customer not found."
            );
        }

        LinkedHashMap<Integer, Integer> cart =
                new LinkedHashMap<>();

        while (true) {

            browseProducts();

            int productId =
                    readInt(
                            "Product ID (0 = finish): "
                    );

            if (productId == 0) {
                break;
            }

            String[] product =
                    findProduct(productId);

            if (product == null) {

                System.out.println(
                        "Product not found."
                );

                continue;
            }

            int stock =
                    Integer.parseInt(product[4]);

            int quantity =
                    readInt("Quantity: ");

            if (quantity <= 0) {

                throw new IllegalArgumentException(
                        "Quantity must be positive."
                );
            }

            int alreadyAdded =
                    cart.getOrDefault(
                            productId,
                            0
                    );

            if (
                    alreadyAdded + quantity
                            > stock
            ) {

                throw new IllegalArgumentException(
                        "Only " +
                        stock +
                        " units available."
                );
            }

            cart.put(
                    productId,
                    alreadyAdded + quantity
            );

            System.out.println(
                    "Added to shopping cart."
            );

            String more =
                    readText(
                            "Add another product? (yes/no): "
                    );

            if (
                    more.equalsIgnoreCase("no")
            ) {
                break;
            }
        }

        if (cart.isEmpty()) {

            System.out.println(
                    "Cart is empty. Order not created."
            );

            return;
        }

        double subtotal = 0;

        StringBuilder items =
                new StringBuilder();

        for (
                Map.Entry<Integer, Integer> entry :
                cart.entrySet()
        ) {

            String[] product =
                    findProduct(entry.getKey());

            int quantity =
                    entry.getValue();

            subtotal +=
                    Double.parseDouble(product[3])
                            * quantity;

            if (items.length() > 0) {
                items.append(";");
            }

            items.append(
                    entry.getKey()
            ).append(":")
                    .append(quantity);
        }

        double shipping =
                subtotal >= 1000 ? 0 : 60;

        double tax =
                subtotal * 0.18;

        double total =
                subtotal + shipping + tax;

        System.out.println("\n================ BILL =================");

        System.out.println(
                "Customer: " + customer[1]
        );

        System.out.printf(
                "Subtotal : ₹%.2f%n",
                subtotal
        );

        System.out.printf(
                "Shipping : ₹%.2f%n",
                shipping
        );

        System.out.printf(
                "Tax 18%%  : ₹%.2f%n",
                tax
        );

        System.out.printf(
                "TOTAL    : ₹%.2f%n",
                total
        );

        String confirm =
                readText(
                        "Place order? (yes/no): "
                );

        if (
                !confirm.equalsIgnoreCase("yes")
        ) {

            System.out.println(
                    "Order cancelled."
            );

            return;
        }

        for (
                Map.Entry<Integer, Integer> entry :
                cart.entrySet()
        ) {

            String[] product =
                    findProduct(entry.getKey());

            int stock =
                    Integer.parseInt(product[4]);

            product[4] =
                    String.valueOf(
                            stock - entry.getValue()
                    );
        }

        orders.add(
                new String[]{
                        String.valueOf(nextOrderId++),
                        String.valueOf(customerId),
                        customer[1],
                        items.toString(),
                        String.valueOf(subtotal),
                        String.valueOf(shipping),
                        String.valueOf(tax),
                        String.valueOf(total),
                        "PLACED",
                        LocalDateTime.now().format(FMT)
                }
        );

        saveData();

        System.out.println(
                "Order placed successfully."
        );

        System.out.println(
                "Order ID: " +
                (nextOrderId - 1)
        );
    }

    static void viewOrders() {

        System.out.println("\n================ ORDERS =================");

        if (orders.isEmpty()) {
            System.out.println("No orders.");
            return;
        }

        for (String[] order : orders) {

            System.out.printf(
                    "Order: %-5s Customer: %-20s Total: ₹%-10.2f Status: %-10s Date: %s%n",
                    order[0],
                    order[2],
                    Double.parseDouble(order[7]),
                    order[8],
                    order[9]
            );
        }
    }

    static String[] findOrder(int id) {

        for (String[] order : orders) {

            if (
                    Integer.parseInt(order[0])
                            == id
            ) {
                return order;
            }
        }

        return null;
    }

    static void updateOrderStatus() {

        viewOrders();

        if (orders.isEmpty()) {
            return;
        }

        int id =
                readInt("Order ID: ");

        String[] order =
                findOrder(id);

        if (order == null) {

            throw new IllegalArgumentException(
                    "Order not found."
            );
        }

        if (
                order[8].equals("DELIVERED")
                ||
                order[8].equals("CANCELLED")
        ) {

            throw new IllegalArgumentException(
                    "This order is already closed."
            );
        }

        System.out.println("1. CONFIRMED");
        System.out.println("2. SHIPPED");
        System.out.println("3. DELIVERED");
        System.out.println("4. CANCELLED");

        int choice =
                readInt("Status: ");

        String status;

        switch (choice) {

            case 1 -> status = "CONFIRMED";
            case 2 -> status = "SHIPPED";
            case 3 -> status = "DELIVERED";
            case 4 -> status = "CANCELLED";

            default ->
                    throw new IllegalArgumentException(
                            "Invalid status."
                    );
        }

        if (
                status.equals("CANCELLED")
        ) {

            restoreStock(order);
        }

        order[8] = status;

        saveData();

        System.out.println(
                "Order status updated."
        );
    }

    static void restoreStock(String[] order) {

        if (order[8].equals("CANCELLED")) {
            return;
        }

        String[] items =
                order[3].split(";");

        for (String item : items) {

            if (item.isBlank()) {
                continue;
            }

            String[] parts =
                    item.split(":");

            int productId =
                    Integer.parseInt(parts[0]);

            int quantity =
                    Integer.parseInt(parts[1]);

            String[] product =
                    findProduct(productId);

            if (product != null) {

                int stock =
                        Integer.parseInt(product[4]);

                product[4] =
                        String.valueOf(
                                stock + quantity
                        );
            }
        }
    }

    // =========================================================
    // REPORT
    // =========================================================

    static void salesReport() {

        double totalSales = 0;
        int cancelled = 0;

        for (String[] order : orders) {

            if (
                    order[8].equals("CANCELLED")
            ) {

                cancelled++;

            } else {

                totalSales +=
                        Double.parseDouble(
                                order[7]
                        );
            }
        }

        System.out.println(
                "\n=========== SALES REPORT ==========="
        );

        System.out.println(
                "Products  : " + products.size()
        );

        System.out.println(
                "Customers : " + customers.size()
        );

        System.out.println(
                "Orders    : " + orders.size()
        );

        System.out.println(
                "Cancelled : " + cancelled
        );

        System.out.printf(
                "Sales     : ₹%.2f%n",
                totalSales
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

            out.println("#PRODUCTS");

            for (String[] p : products) {
                out.println(String.join("|", p));
            }

            out.println("#CUSTOMERS");

            for (String[] c : customers) {
                out.println(String.join("|", c));
            }

            out.println("#ORDERS");

            for (String[] o : orders) {
                out.println(String.join("|", o));
            }

        } catch (IOException e) {

            System.out.println(
                    "Could not save data: " +
                    e.getMessage()
            );
        }
    }

    static void loadData() {

        File file = new File(FILE);

        if (!file.exists()) {
            return;
        }

        try (
                BufferedReader br =
                        new BufferedReader(
                                new FileReader(file)
                        )
        ) {

            String section = "";
            String line;

            while ((line = br.readLine()) != null) {

                if (line.equals("#PRODUCTS")) {
                    section = "PRODUCTS";
                    continue;
                }

                if (line.equals("#CUSTOMERS")) {
                    section = "CUSTOMERS";
                    continue;
                }

                if (line.equals("#ORDERS")) {
                    section = "ORDERS";
                    continue;
                }

                if (line.isBlank()) {
                    continue;
                }

                String[] data =
                        line.split("\\|", -1);

                if (
                        section.equals("PRODUCTS")
                        &&
                        data.length == 5
                ) {

                    products.add(data);

                    nextProductId =
                            Math.max(
                                    nextProductId,
                                    Integer.parseInt(data[0]) + 1
                            );

                } else if (
                        section.equals("CUSTOMERS")
                        &&
                        data.length == 5
                ) {

                    customers.add(data);

                    nextCustomerId =
                            Math.max(
                                    nextCustomerId,
                                    Integer.parseInt(data[0]) + 1
                            );

                } else if (
                        section.equals("ORDERS")
                        &&
                        data.length == 10
                ) {

                    orders.add(data);

                    nextOrderId =
                            Math.max(
                                    nextOrderId,
                                    Integer.parseInt(data[0]) + 1
                            );
                }
            }

        } catch (Exception e) {

            products.clear();
            customers.clear();
            orders.clear();

            nextProductId = 1;
            nextCustomerId = 1;
            nextOrderId = 1001;

            System.out.println(
                    "Old data could not be loaded. Starting fresh."
            );
        }
    }
}