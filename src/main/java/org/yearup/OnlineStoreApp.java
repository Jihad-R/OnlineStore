package org.yearup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

public class OnlineStoreApp {

    private Scanner scanner = null;

    private HashMap<String, Product> products = new HashMap<>();

    private final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_RESET = "\u001B[0m";


    public void run() {

        loadInventory();
        homeScreen();
    }


    public void loadInventory() {

        try {
            FileInputStream fileInputStream = new FileInputStream("inventory.csv");
            String line = null;
            scanner = new Scanner(fileInputStream);

            while (scanner.hasNextLine()){

                line = scanner.nextLine();
                String[] productInfo = line.split("\\|");
                String id = productInfo[0];
                String name = productInfo[1];
                double price = Double.parseDouble(productInfo[2]);

                products.put(name,new Product(id,name,price));

            }

        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        }
        finally {
            scanner.close();
        }


    }

    public void homeScreen() {

        scanner = new Scanner(System.in);
        int option;
        boolean isValidOption = false;

        do {
            System.out.println("=========================");
            System.out.println("ONLINE STORE HOME SCREEN");
            System.out.println("========================");
            System.out.println("1 - Show Products");
            System.out.println("2 - Show Shopping Carts");
            System.out.println("0 - Exit");

            System.out.print("Please select a commands: ");

            try {
                option = scanner.nextInt();

                switch (option) {
                    case 0:
                        return;
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
            } finally {
                scanner.nextLine();
            }

        }
        while (!isValidOption);

    }

    private void displayShoppingCart() {
    }

    private void displayProduct() {
    }

}
