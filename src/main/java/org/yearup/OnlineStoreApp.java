package org.yearup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class OnlineStoreApp {

    private Scanner scanner = null;

    private HashMap<String, Product> products = new HashMap<>(); // declare the products hashmap
    private ArrayList<Product> shoppingCart = new ArrayList<>(); // declare the shopping cart

    String userInput; // stores the id of the product to be added to the cart
    private boolean isValidOption = false; // checks if the option selected is valid
    private Set<Product> distinct; // delcare a set to store all the distinct products
    private int productQuantity;


    public void run() {

        loadInventory();
        homeScreen();
    }


    public void loadInventory() {

        try {
            FileInputStream fileInputStream = new FileInputStream("inventory.csv");
            String line = null;
            scanner = new Scanner(fileInputStream);

            while (scanner.hasNextLine()) {

                line = scanner.nextLine();
                String[] productInfo = line.split("\\|");
                String id = productInfo[0];
                String name = productInfo[1];
                double price = Double.parseDouble(productInfo[2]);

                products.put(id, new Product(id, name, price));

            }

        } catch (FileNotFoundException ex) {
            System.out.println(ConsoleColor.RED + "File not found!" + ConsoleColor.RESET);
        } finally {
            scanner.close();
        }


    }

    public void homeScreen() {

        scanner = new Scanner(System.in);
        int option; // stores the value of the option selected

        do {
            // home screen UI
            System.out.println("=========================");
            System.out.println("ONLINE STORE HOME SCREEN");
            System.out.println("========================");
            System.out.println("1 - Show Products");
            System.out.println("2 - Show Shopping Carts");
            System.out.println("0 -" + ConsoleColor.RED_BOLD + " Exit" + ConsoleColor.RESET);

            System.out.print("Please select a commands (1, 2, or 0): ");

            try // try/catch clause in case the user inputs a string
            {
                option = scanner.nextInt();

                switch (option) {
                    case 0:
                        System.exit(1);
                    case 1: {
                        displayProduct();
                        isValidOption = true;
                        break;
                    }
                    case 2: {
                        scanner.nextLine();
                        displayShoppingCart();
                        isValidOption = true;
                        break;

                    }
                    default:
                        System.out.println(ConsoleColor.RED + "The command selected is invalid!" + ConsoleColor.RESET);
                        isValidOption = false;
                }
            } catch (InputMismatchException ex) {
                System.out.println(ConsoleColor.RED + "Invalid Input! Valid inputs are integers between 0 - 2 " + ConsoleColor.RESET);

                homeScreen();
            }

        }
        while (!isValidOption);

    }

    private void displayProduct() {


        scanner = new Scanner(System.in);
        isValidOption = false;

        //formatting the header of the CLI table
        System.out.println("------- ----------------------------------- ------");
        System.out.printf("|%-7s|%-35s|%-7s|\n", "ID", "NAME", "PRICE");
        System.out.println("------- ----------------------------------- ------");

        for (Product product : products.values()) // loop through the values of the hashmap
        {
            //format and display product
            System.out.printf("|%7s|%-35s|%7.2f|\n", product.getId(), product.getName(), product.getPrice(), "000");
            System.out.println("------- ----------------------------------- ------");
        }

        do {

            System.out.println("Would you like to add a product to your shopping cart?");
            System.out.println("- Enter the ID of the product to add to cart");
            System.out.println("- Press 'X' for home screen");
            System.out.print("Product ID: ");
            userInput = scanner.nextLine().toUpperCase().trim();

            if (products.containsKey(userInput)) {

                shoppingCart.add(products.get(userInput));
                isValidOption = true;
                homeScreen();
            } else {
                System.out.println(ConsoleColor.RED + "Invalid Input! Please try again" + ConsoleColor.RESET);
            }

            if (userInput.equalsIgnoreCase("X")) {
                homeScreen();
            }

        } while (!isValidOption);


    }


    // TODO - add user input validation
// TODO - add try and catch
    private void displayShoppingCart() {

        distinct = new HashSet<>(shoppingCart);
        isValidOption = false;
        int productQuantity;

        shoppingCartContent("shopping-cart", 0.00); // display shopping cart content

        do {
            System.out.println("Would you like to" + ConsoleColor.BLUE_BRIGHT + " check out " + ConsoleColor.RESET + "?");
            System.out.println(ConsoleColor.GREEN + "- 'C' to check out" + ConsoleColor.RESET);
            System.out.println(ConsoleColor.RED + "- 'X' for home screen" + ConsoleColor.RESET);
            System.out.print("Select a command: ");
            userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("C") && (shoppingCart.size() > 0)) {
                checkout();
                isValidOption = true;
            } else if (userInput.equalsIgnoreCase("X")) {
                homeScreen();
                isValidOption = true;
            } else if (userInput.equalsIgnoreCase("C") && shoppingCart.size() == 0) {
                System.out.println(ConsoleColor.YELLOW + "Unable to checkout! " +
                        "The shopping cart is empty." + ConsoleColor.RESET);
            } else {
                System.out.println(ConsoleColor.RED + "Invalid command! Please try again." + ConsoleColor.RESET);
            }
        } while (!isValidOption);

    }

    // TODO - add user input validation
// TODO - add try and catch
    private void checkout() {

        double sum = 0.00; // declare the variable that would store the total
        double cashAmount; // stores the value of the cash amount enter by the user
        double balance = 0.00; // stores t

        sum = shoppingCartContent("checkout", sum);

        System.out.printf("%7s: %.2f\n", "TOTAL", sum); // display total
        System.out.println("--------------------------------------------------------------");

        System.out.print("Please enter the cash amount: ");

        try // try-catch clause to catch any invalid input
        {
            cashAmount = scanner.nextDouble(); //store the cash amount of the user
            balance = cashAmount - sum;

            if (balance > 0.00) {

                System.out.printf("Your change is: %.2f\n", balance);

                shoppingCartContent("checkout", sum);
                shoppingCart.clear();

                homeScreen();

            } else {
                System.out.printf("Cash amount is insufficient\nTotal amount: %.2f", sum);
            }

        } catch (InputMismatchException ex) {
            System.out.println(ConsoleColor.RED + "Invalid input! Please enter a numeric value" + ConsoleColor.RESET);
            scanner.nextLine();
            checkout();
        }


    }


    public double shoppingCartContent(String contentFor, double sum) {

        String screenTitle = "";
        switch (contentFor) {

            case "shopping-cart":
                screenTitle = "\t\t\t\t\tSHOPPING CART";
                break;
            case "checkout":
                screenTitle = "\t\t\t\t\tONLINE STORE CHECK OUT";
                break;
        }

        System.out.println("===============================================================");
        System.out.println(screenTitle);
        System.out.println("===============================================================");
        System.out.println("------- ----------------------------------- ------ ----------");
        System.out.printf("|%-7s|%-35s|%-7s|%-8s|\n", "ID", "NAME", "PRICE", "QUANTITY");
        System.out.println("------- ----------------------------------- ------ ----------");

        for (Product product : distinct) {

            productQuantity = Collections.frequency(shoppingCart, product);

            // format and display the content of shopping cart
            System.out.printf("|%7s|%-35s|%7.2f|%8s|\n", product.getId(), product.getName(),
                    product.getPrice(), productQuantity);
            System.out.println("------- ----------------------------------- ------ ----------");

            if (contentFor.equalsIgnoreCase("checkout")) {
                sum += product.getPrice() * productQuantity;

            }

        }

        return sum;


    }
}


