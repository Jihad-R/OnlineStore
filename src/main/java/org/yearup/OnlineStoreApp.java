package org.yearup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class OnlineStoreApp {

    private Scanner scanner = null;

    private HashMap<String, Product> products = new HashMap<>(); // declare the products hashmap
    private ArrayList<Product> shoppingCart = new ArrayList<>(); // declare the shopping cart

    private final String ANSI_RED = "\u001B[31m";
    private final String ANSI_RESET = "\u001B[0m";
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
            System.out.println(ANSI_RED + "File not found!" + ANSI_RESET);
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
            System.out.println("0 - Exit");

            System.out.print("Please select a commands: ");

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
                        displayShoppingCart();
                        isValidOption = true;
                        break;

                    }
                    default:
                        System.out.println(ANSI_RED + "The command selected is invalid!" + ANSI_RESET);
                        isValidOption = false;
                }
            } catch (InputMismatchException ex) {
                System.out.println(ANSI_RED + "Invalid! Valid inputs are integers between 0 - 2" + ANSI_RESET);
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
                System.out.println(ANSI_RED + "Invalid Input" + ANSI_RESET);
            }

            if (userInput.equalsIgnoreCase("X")) {
                homeScreen();
            }

        } while (!isValidOption);


    }

    private void displayShoppingCart() {

        double sum = 0.00;
        distinct = new HashSet<>(shoppingCart);
        int productQuantity;

        System.out.println("------- ----------------------------------- ------ ----------");
        System.out.printf("|%-7s|%-35s|%-7s|%-8s|\n", "ID", "NAME", "PRICE", "QUANTITY");
        System.out.println("------- ----------------------------------- ------ ----------\n");

        for (Product product : distinct) {
            
            // count the frequency of product in shopping cart
            productQuantity = Collections.frequency(shoppingCart, product); 
            // format and display the content of shopping cart
            System.out.printf("|%7s|%-35s|%7.2f|%8s|\n", product.getId(), product.getName(), 
                    product.getPrice(), productQuantity);
            System.out.println("------- ----------------------------------- ------ ----------");
        }

        System.out.println("Would you like to"+ANSI_RED+" check out "+ANSI_RESET+"?");
        System.out.println("- 'C' to check out");
        System.out.println("- 'X' for home screen");
        System.out.print("Select a command: ");
        scanner.nextLine();
        userInput = scanner.nextLine();
        
        if (userInput.equalsIgnoreCase("C")){
            checkout();
        }
        else if (userInput.equalsIgnoreCase("X")){
            homeScreen();
        }
        
    }

    private void checkout() {

        for(Product product: distinct){

            productQuantity = Collections.frequency(shoppingCart,product);


        }

    }


}
