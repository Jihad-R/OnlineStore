package org.yearup;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class OnlineStoreApp {

    private Scanner scanner = null;

    private HashMap<String, Product> products = new HashMap<>(); // declare the products hashmap
    private ArrayList<Product> shoppingCart = new ArrayList<>(); // declare the shopping cart

    private Set<Product> distinct; // delcare a set to store all the distinct products
    String userInput; // stores the id of the product to be added to the cart
    private boolean isValidOption = false; // checks if the option selected is valid
    double sum = 0.00; // declare the variable that would store the total


    public void run() //
    {
        loadInventory(); // load inventory
        homeScreen(); // display home screen
    }


    // Adds all the products in .csv file into a Hashmap
    public void loadInventory()
    {
        try // try and catch clause to catch any exception regarding the .csv file
        {
            FileInputStream fileInputStream = new FileInputStream("inventory.csv"); // open file stream
            String line;
            scanner = new Scanner(fileInputStream); // scan the input stream

            while (scanner.hasNextLine()) // loop if there is a next line in the .csv file
            {
                line = scanner.nextLine(); // store the content of the next line in the csv file

                String[] productInfo = line.split("\\|"); // split the string on the '|'
                String id = productInfo[0];
                String name = productInfo[1];
                double price = Double.parseDouble(productInfo[2]);

                products.put(id, new Product(id, name, price)); // add a new product to the Hashmap
            }

        }

        catch (FileNotFoundException ex) // catch exception if the file is not found
        {
            System.out.println(ConsoleColor.RED + "\nFile not found!\n" + ConsoleColor.RESET);
        }

        catch (Exception ex) // catch other exceptions
        {
            System.out.println(ConsoleColor.RED + "\nSomething went wrong!\n" + ConsoleColor.RESET);
        }

        finally
        {
            scanner.close();
        }


    }

    //Displays home screen UI
    public void homeScreen() //
    {
        scanner = new Scanner(System.in); // scan input stream from the user's keyboard
        int option; // stores the value of the option selected

        do // do-while loop that loops until a valid option is selected
        {
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

                switch (option) // switch cases to validate user input
                {
                    case 0 -> System.exit(1);
                    case 1 ->
                    {
                        scanner.nextLine();
                        displayProduct();
                        isValidOption = true;
                    }
                    case 2 ->
                    {
                        scanner.nextLine();
                        displayShoppingCart();
                        isValidOption = true;

                    }
                    default ->
                    {
                        System.out.println(ConsoleColor.RED + "The command selected is invalid!"
                                + ConsoleColor.RESET); // error message
                        isValidOption = false;
                    }
                }
            }

            catch (InputMismatchException ex)
            {
                System.out.println(ConsoleColor.RED + "Invalid Input! Valid inputs are integers between 0 - 2."
                        + ConsoleColor.RESET); // error message

                homeScreen();
            }
        }
        while (!isValidOption);

    }

    // Displays product screen UI
    private void displayProduct()
    {
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

        do // do-while loop that loops until an input is valid
        {
            System.out.println("Would you like to add a product to your shopping cart?");
            System.out.println("- Enter the ID of the product to add to cart");
            System.out.println("- Press 'X' for home screen");

            System.out.print("Product ID: ");
            userInput = scanner.nextLine().toUpperCase().trim();

            if (products.containsKey(userInput))
            {
                isValidOption = true;
                shoppingCart.add(products.get(userInput)); // add product to the array list

                System.out.printf("%s'%s' has been added successfully to the shopping cart.%s\n"
                        , ConsoleColor.BLUE, userInput, ConsoleColor.RESET); // success message

                homeScreen(); // navigate to the home screen
            }

            else if(!(userInput.equalsIgnoreCase("x")))
            {
                System.out.println(ConsoleColor.RED + "Invalid Input! Please try again."
                        + ConsoleColor.RESET); // error message
            }

            if (userInput.equalsIgnoreCase("X"))//
            {
                homeScreen(); // navigate to the home screen
            }
        }
        while (!isValidOption);


    }


    // Displays shopping cart UI
    private void displayShoppingCart()
    {
        distinct = new HashSet<>(shoppingCart); // store the unique instances of the arraylist
        isValidOption = false;

        sum = 0.00; // re-initialize the sum
        sum = shoppingCartContent("shopping-cart", sum); // display shopping cart content

        System.out.printf("%7s: %.2f\n", "TOTAL", sum); // display total
        System.out.println("--------------------------------------------------------------");

        do // do-while loop that loops until a valid option is selected
        {
            System.out.println("Would you like to" + ConsoleColor.BLUE_BRIGHT + " check out "
                    + ConsoleColor.RESET + "?");
            System.out.println(ConsoleColor.GREEN + "- 'C' to check out" + ConsoleColor.RESET);
            System.out.println(ConsoleColor.RED_BOLD + "- 'X' for home screen" + ConsoleColor.RESET);

            System.out.print("Select a command: ");
            userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("C") && (shoppingCart.size() > 0))
            {
                isValidOption = true;
                checkout(); // navigate to the checkout screen
            }

            else if (userInput.equalsIgnoreCase("X"))
            {
                isValidOption = true;
                homeScreen(); // navigate to the home screen
            }

            else if (userInput.equalsIgnoreCase("C") && shoppingCart.size() == 0)
            {
                System.out.println(ConsoleColor.YELLOW + "Unable to checkout! " +
                        "The shopping cart is empty." + ConsoleColor.RESET); // warning message
            }

            else
            {
                System.out.println(ConsoleColor.RED + "Invalid command! Please try again."
                        + ConsoleColor.RESET); // error message
            }
        }
        while (!isValidOption);
    }

    // Displays checkout screen
    private void checkout()
    {
        double cashAmount; // stores the value of the cash amount enter by the user
        double balance; // stores the value of the change

        sum = 0.00;
        sum = shoppingCartContent("checkout", sum);

        System.out.printf("%7s: %.2f\n", "TOTAL", sum); // display total
        System.out.println("--------------------------------------------------------------");

        System.out.print("Please enter the cash amount: ");

        try // try-catch clause to catch any invalid input
        {
            cashAmount = scanner.nextDouble(); //store the cash amount of the user
            balance = cashAmount - sum; // calculate change

            if (balance > 0.00)
            {
                shoppingCartContent("soldItems", sum);
                System.out.printf("%sYour change is: $%.2f%s\n",
                        ConsoleColor.BLUE, balance, ConsoleColor.RESET); //display change
                shoppingCart.clear();

                homeScreen();
            }
            else
            {
                System.out.printf("%sCash amount is insufficient.\n%sTotal amount: %.2f\n",
                        ConsoleColor.YELLOW, ConsoleColor.RESET, sum); // warning message
                homeScreen();
            }

        }

        catch (InputMismatchException ex)// catches exception related to the format/data type of the input
        {
            System.out.println(ConsoleColor.RED + "Invalid input! Please enter a numeric value." + ConsoleColor.RESET);
            scanner.nextLine();
            checkout();
        }

        catch (Exception ex) // catches any unexpected exception
        {
            System.out.println(ConsoleColor.RED + "Something went wrong!" + ConsoleColor.RESET);
            scanner.nextLine();
            checkout();
        }

    }

    public double shoppingCartContent(String contentFor, double sum) //
    {
        String screenTitle = switch (contentFor) // switch cases to decide the screen title
                {
                    case "shopping-cart" -> "\t\t\t\t\tSHOPPING CART";
                    case "checkout" -> "\t\t\t\t\tONLINE STORE CHECK OUT";
                    case "soldItems" -> "\t\t\t\t\tITEMS SOLD";
                    default -> "";
                };

        System.out.println("===============================================================");
        System.out.println(screenTitle);
        System.out.println("===============================================================");
        System.out.println("------- ----------------------------------- ------ ----------");
        System.out.printf("|%-7s|%-35s|%-7s|%-8s|\n", "ID", "NAME", "PRICE", "QUANTITY");
        System.out.println("------- ----------------------------------- ------ ----------");

        for (Product product : distinct) //
        {

            // stores the quantity of the products in the shopping cart
            int productQuantity = Collections.frequency(shoppingCart, product);

            // format and display the content of shopping cart
            System.out.printf("|%7s|%-35s|%7.2f|%8s|\n", product.getId(), product.getName(),
                    product.getPrice(), productQuantity);
            System.out.println("------- ----------------------------------- ------ ----------");

            sum += product.getPrice() * productQuantity; // calculate the total amount to be paid

        }
        return sum;
    }
}


