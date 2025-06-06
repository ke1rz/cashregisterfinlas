import java.util.*;
import java.util.regex.*;
import java.io.*;

public class Main {

    static Scanner mark = new Scanner(System.in);
    static int[] menuNums = {1, 2, 3, 4, 5, 6};
    static String[] menuPducts = {"Chicken ni Joy", "Jolly Spaghetti ni mark", 
        "Burger Steak na may kanin", "Yummy burger", "Pinoy Fries", "Coke Float"};
    static double[] menuPrices = {99.00, 75.00, 90.00, 55.00, 45.00, 39.00};

    static ArrayList<String> usernames = new ArrayList<>();
    static ArrayList<String> passwords = new ArrayList<>();
    static String currentUser = "";

    public static void signup() {
        System.out.println("\n------------------< SIGN UP >------------------");

        String username;
        String password;

        while (true) {
            System.out.println("Username can be Non-alphanumeric (eg. Mark-bagayao@gmail.com) ");
            System.out.print("Enter a username: ");
            username = mark.nextLine();

            if (username.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
                break;
            } else {
                System.out.println("Invalid username. Try again.");
            }
        }

        while (true) {
            System.out.print("Enter a password (8-15 characters, at least 1 uppercase and 1 number.): ");
            password = mark.nextLine();

            if (password.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Za-z0-9]{8,15}$")) {
                break;
            } else {
                System.out.println("Invalid password. Try again.");
            }
        }

        usernames.add(username);
        passwords.add(password);

        System.out.println("Signup successful! You can now login.");
    }

    public static void login() {
        System.out.println("\n------------------< LOGIN >------------------");

        while (true) {
            System.out.print("Enter your username: ");
            String inputUsername = mark.nextLine();
            System.out.print("Enter your password: ");
            String inputPassword = mark.nextLine();

            boolean found = false;
            for (int i = 0; i < usernames.size(); i++) {
                if (usernames.get(i).equals(inputUsername) && passwords.get(i).equals(inputPassword)) {
                    currentUser = inputUsername;
                    found = true;
                    break;
                }
            }

            if (found) {
                System.out.println("\nLogin successful! Welcome to MamaMOJollibee Cash Register.\n");
                break;
            } else {
                System.out.println("Incorrect username or password. Try again!");
            }
        }
    }

    public static void logTransaction(String username, ArrayList<String> items, ArrayList<Integer> quantities, ArrayList<Double> prices, double totalAmount) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("transactions.txt", true))) {
            StringBuffer log = new StringBuffer();

            Date now = new Date(); // No need for extra imports
            log.append("------------------------------------------------------------\n");
            log.append("Transaction Date: ").append(now.toString()).append("\n");
            log.append("Cashier: ").append(username).append("\n");
            log.append("Items:\n");

            for (int i = 0; i < items.size(); i++) {
                log.append(String.format("- %s | Quantity: %d | Unit Price: Php %.2f | Total: Php %.2f\n",
                    items.get(i),
                    quantities.get(i),
                    prices.get(i),
                    prices.get(i) * quantities.get(i)
                ));
            }

            log.append(String.format("Total Amount: Php %.2f\n", totalAmount));
            log.append("------------------------------------------------------------\n\n");

            writer.write(log.toString());

        } catch (IOException e) {
            System.out.println("Error writing to transaction file.");
        }
    }

    public static double checkout(ArrayList<String> cartProducts, ArrayList<Double> cartTotalPrices, ArrayList<Integer> cartQuantities, ArrayList<Double> cartProductPrices) {
        double total = 0;
        System.out.println("\n---------------------------------------------------------");
        System.out.println("                 { ITO ANG RECEIPT MO }                    ");
        System.out.println("---------------------------------------------------------");
        System.out.printf("%-25s  %-10s %-10s %-10s %n", "Product", "Qty", "Price", "Total");

        for (int i = 0; i < cartProducts.size(); i++) {
            double subtotal = cartTotalPrices.get(i);
            total += subtotal;
            System.out.printf("%-25s  %-10d %-10.2f %-10.2f %n", cartProducts.get(i), cartQuantities.get(i), cartProductPrices.get(i), subtotal);
        }

        System.out.println("\n---------------------------------------------------------");
        System.out.printf("%-25s  %10.2f %n", "Total of your Order:", total);
        System.out.println("---------------------------------------------------------");      
        System.out.println("\nAre you a PWD or Senior Citizen (y/n): ");
        String discount = mark.nextLine();

        if (discount.equalsIgnoreCase("y")) {
            double discountAmount = total * 0.20;
            total -= discountAmount;
            System.out.printf("\nA discount of 20%% (Php. %.2f) has been applied\n", discountAmount);
        }

        System.out.println("---------------------------------------------------------");
        System.out.printf(" %-25s  %10.2f %n", "Total Amount:", total);
        System.out.println("---------------------------------------------------------");      
        return total;
    }

    public static void processPayment(double total, ArrayList<String> cartPducts, ArrayList<Double> cartPductPrices, ArrayList<Integer> cartQntties) {
        double payment = 0;
        while (true) {
            try {
                System.out.print("Enter your payment amount: Php. ");
                payment = Double.parseDouble(mark.nextLine());

                if (payment < total) {
                    System.out.println("\nKulang ang bayad mo. Mag enter ka atleast Php. " + total);
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        double change = payment - total;
        System.out.println("Your change po: Php. " + change);
        System.out.println("\n-----------------------< ('__') >------------------------");
        System.out.println("Enjoy your meal! Sana di ka mabulunan:)");
        System.out.println("---------------------------------------------------------");

        logTransaction(currentUser, cartPducts, cartQntties, cartPductPrices, total);
    }

    public static void transactionLoop() {
        ArrayList<String> cartProducts = new ArrayList<>();
        ArrayList<Double> cartProductPrices = new ArrayList<>();
        ArrayList<Integer> cartQuantities = new ArrayList<>();
        ArrayList<Double> cartTotalPrices = new ArrayList<>();

        while (true) {
            System.out.println("\n======= MENU =======");
            System.out.println("[1] Add Item");
            System.out.println("[2] View Orders");
            System.out.println("[3] Update Quantity");
            System.out.println("[4] Remove Item");
            System.out.println("[5] Checkout");
            System.out.println("[6] Logout");
            System.out.print("Choose an option: ");
            String option = mark.nextLine();

            switch (option) {
                case "1":
                    while (true) {
                        System.out.println("\nAvailable Menu:");
                        for (int i = 0; i < menuNums.length; i++) {
                            System.out.printf("[%d] %s - Php %.2f\n", menuNums[i], menuPducts[i], menuPrices[i]);
                        }
                        System.out.print("Enter product number (0 to stop): ");
                        int prodNum = Integer.parseInt(mark.nextLine());
                        if (prodNum == 0) break;
                        if (prodNum < 1 || prodNum > menuNums.length) {
                            System.out.println("Invalid product number.");
                            continue;
                        }
                        System.out.print("Enter quantity: ");
                        int qty = Integer.parseInt(mark.nextLine());
                        String selectedProduct = menuPducts[prodNum - 1];
                        double price = menuPrices[prodNum - 1];
                        cartProducts.add(selectedProduct);
                        cartQuantities.add(qty);
                        cartProductPrices.add(price);
                        cartTotalPrices.add(price * qty);
                        System.out.println("Item added to cart!");
                    }
                    break;
                case "2":
                    System.out.println("\n== Current Cart ==");
                    for (int i = 0; i < cartProducts.size(); i++) {
                        System.out.printf("%d. %s - Qty: %d - Price: Php %.2f - Total: Php %.2f\n",
                                i + 1,
                                cartProducts.get(i),
                                cartQuantities.get(i),
                                cartProductPrices.get(i),
                                cartTotalPrices.get(i));
                    }
                    break;
                case "3":
                    System.out.print("Enter item number to update: ");
                    int updateIndex = Integer.parseInt(mark.nextLine()) - 1;
                    if (updateIndex < 0 || updateIndex >= cartProducts.size()) {
                        System.out.println("Invalid item number.");
                        break;
                    }
                    System.out.print("Enter new quantity: ");
                    int newQty = Integer.parseInt(mark.nextLine());
                    cartQuantities.set(updateIndex, newQty);
                    double newTotal = newQty * cartProductPrices.get(updateIndex);
                    cartTotalPrices.set(updateIndex, newTotal);
                    System.out.println("Quantity updated!");
                    break;
                case "4":
                    System.out.print("Enter item number to remove: ");
                    int removeIndex = Integer.parseInt(mark.nextLine()) - 1;
                    if (removeIndex < 0 || removeIndex >= cartProducts.size()) {
                        System.out.println("Invalid item number.");
                        break;
                    }
                    cartProducts.remove(removeIndex);
                    cartQuantities.remove(removeIndex);
                    cartProductPrices.remove(removeIndex);
                    cartTotalPrices.remove(removeIndex);
                    System.out.println("Item removed.");
                    break;
                case "5":
                    double total = checkout(cartProducts, cartTotalPrices, cartQuantities, cartProductPrices);
                    processPayment(total, cartProducts, cartProductPrices, cartQuantities);
                    cartProducts.clear();
                    cartQuantities.clear();
                    cartProductPrices.clear();
                    cartTotalPrices.clear();
                    System.out.println("\n[1] Continue Transaction");
                    System.out.println("[2] Logout");
                    System.out.println("[3] Exit");
                    System.out.print("Choose an option: ");
                    String postCheckoutChoice = mark.nextLine();
                    if (postCheckoutChoice.equals("2")) return;
                    if (postCheckoutChoice.equals("3")) System.exit(0);
                    break;
                case "6":
                    System.out.println("Logging out...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n==== MamaMOJollibee Cash Register ====");
            System.out.println("[1] Sign up");
            System.out.println("[2] Login");
            System.out.println("[3] Exit");
            System.out.print("Choose an option: ");
            String choice = mark.nextLine();

            switch (choice) {
                case "1":
                    signup();
                    break;
                case "2":
                    login();
                    System.out.println("Do you want to start an order? (y/n): ");
                    String ans = mark.nextLine();
                    if (ans.equalsIgnoreCase("y")) {
                        transactionLoop();
                    }
                    break;
                case "3":
                    System.out.println("Thank you for using MamaMOJollibee Cash Register!");
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
