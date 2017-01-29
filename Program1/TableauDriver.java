/**
 * ICS 340.01 - Program 1
 * @author: Chelsea Hanson
 *
 * Purpose: This class drives the user interface to manipulate and
 * play with the YoungTableau3D class.
 */

import java.util.Scanner;

public class TableauDriver{
    
    static Scanner keyboard = new Scanner(System.in);

    /**
     * Runs a loop with a menu for the user to manipulate the tableau.
     */
    public static void main(String[] args){
        YoungTableau3D tableau;

        System.out.println("***********************************");
        System.out.println("* 3D Young Tableau:               *");
        System.out.println("***********************************");
        
        System.out.print("Enter the number of rows: ");
        int r = keyboard.nextInt();
        
        System.out.print("Enter the number of columns: ");
        int c = keyboard.nextInt();
        
        System.out.print("Enter the number of panes: ");
        int p = keyboard.nextInt();
        
        tableau = new YoungTableau3D(r, c, p);
        
        do{
            printMenu();
            int userInput = keyboard.nextInt();
            switch (userInput){
                case 0: System.exit(0);
                        break;
                case 1: createNew(tableau);
                        break;
                case 2: insertVal(tableau);
                        break;
                case 3: System.out.println("The maximum value is: " + tableau.extractMax());
                        break;
                case 4: tableau.printTableau();
                        break;
                default: System.out.println("\"" + userInput + "\" is an invalid menu choice\n");
                        break;
            }
        } while (true);
    }

    /**
     * Displays the menu and prompts the user to pick an action.
     */
    public static void printMenu(){
        System.out.println("");
        System.out.println("***********************************");
        System.out.println("* 0 - Exit Demonstration          *");
        System.out.println("* 1 - Create New 3D Young Tableau *");
        System.out.println("* 2 - Insert A Value              *");
        System.out.println("* 3 - Extract The Max Value       *");
        System.out.println("* 4 - Print 3D Young Tableau      *");
        System.out.println("***********************************");
        System.out.print("Choose a menu item: ");
    }

    /**
     * Creates a new 3D Young Tableau from user input for size.
     * @param tableau replace the current tableau with a new one.
     */
    public static void createNew(YoungTableau3D tableau){
        
        System.out.print("Enter the number of rows: ");
        int r = keyboard.nextInt();
        
        System.out.print("Enter the number of columns: ");
        int c = keyboard.nextInt();
        
        System.out.print("Enter the number of panes: ");
        int p = keyboard.nextInt();
        
        tableau = new YoungTableau3D(r, c, p);
    }

    /**
     * Adds a user defined number to the tableau.
     * @param tableau the 3D tableau to add a value to
     */
    public static void insertVal(YoungTableau3D tableau) {
        
        System.out.print("Enter a value to insert: ");
        int value = keyboard.nextInt();
        
        tableau.insertValue(value);
    }
}