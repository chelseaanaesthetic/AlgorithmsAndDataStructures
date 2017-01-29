/**
 * ICS 340.01 - Program 1
 * @author: Chelsea Hanson
 *
 * Purpose: This class sets up a data structure to represent a 3D array
 * sorted in the style of a Young Tableau, and ways to manipulate it.
 *
 * This 3D Young Tableau is sorted based on the sum of the indices.
 * A larger sum indicates a smaller value. There is no order among
 * values with the same index sum.
 */
public class YoungTableau3D{
    
    int r;
    int c;
    int p;
    int[][][] tableau;
        
    public YoungTableau3D(int rows, int columns, int panes){
        r = rows;
        c = columns;
        p = panes;

        //create tableau and set all values to 0
        tableau = new int[r][c][p];
        clearAll();
    }

    /**
     * Adds a number to the 3D Young Tableau and rearranges it to maintain
     * the unique properties.
     *
     * @param number The value to be added to the tableau
     */
    public void insertValue(int number){
        if (tableau[r-1][c-1][p-1] > 0){
            System.out.println("This tableau is already full.\nTry extracting the max first.");
            return;
        }
        // If max is 0, no need to resort the tableau after insertion.
        if (tableau[0][0][0] == 0){
            tableau[0][0][0] = number;
        }
        // If the number is greater than max, drop max a level.
        else if (number > tableau[0][0][0]){
            int temp = tableau[0][0][0];
            tableau[0][0][0] = number;
            dropMin(temp, 1);
        }
        // If the number is smaller than max, drop the number a level.
        else {
            dropMin(number, 1);
        }
        // Displays the sorted 3D Young Tableau with the new value inserted.
        printTableau();
    }

    /**
     * Removes the maximum value from the tableau and rearranges it to maintain
     * the unique properties.
     *
     * @return The max value from the 3D Young Tableau.
     */
    public int extractMax(){
        int max = tableau[0][0][0];
        tableau[0][0][0] = liftMax(1);   
        return max;
    }

    /**
     * This method is used by the extractMax() method to replace the number
     * extracted with the next largest number in the tableau until the entire
     * tableau is fully sorted.
     *
     * @param indexSum the level of values compared to find the largest.
     * @return The largest value from the given level to move up a level.
     */
    public int liftMax(int indexSum){        
        int max = 0;
        int maxR = 0;
        int maxC = 0;
        int maxP = 0;

        int startK = 0;
        int startM = 0;
        int n = 0;

        // Limits outer for loop starting point to stay within array bounds.
        if((indexSum - c - p - 2) > 0){
            startK = indexSum - c - p - 2;
        }
        // Limits inner for loop starting point to stay within array bounds.
        if((indexSum - r - p - 2) > 0){
            startM = indexSum - r - p - 2;
        }

        // Iterates through all numbers with the given index sum to move
        // the max value up a level to the index sum 1 smaller.
        for(int k=startK; k <= indexSum && k <= r-1; k++){
            for(int m=startM; m <= indexSum-k && m <= c-1; m++){
                n = indexSum - (k+m);
                if (n < p && tableau[k][m][n] > max){
                    max = tableau[k][m][n];
                    maxR = k;
                    maxC = m;
                    maxP = n;
                }
            }
        }

        // If next level is out of array bounds, replace lifted value with 0.
        if (indexSum + 1 > (r + c + p - 3)){
            tableau[maxR][maxC][maxP] = 0;
        }
        // Otherwise check for the largest value in the level below (higher index sum).
        else{
            tableau[maxR][maxC][maxP] = liftMax(indexSum+1);
        }
        return max;
    }

    /**
     * This method is used by the insertValue() method to place the number
     * in the appropriate level (indexSum) and move the smallest number
     * in that level down a level (indexSum + 1) in the tableau until the
     * entire tableau is fully sorted.
     *
     * @param value The number to add to the specified level.
     * @param indexSum The level of values compared to find the smallest.
     */
    public void dropMin(int value, int indexSum){
        int min = value;
        int startK = 0;
        int startM = 0;
        int n = 0;
        
        if (min > 0){
            // Limits outer for loop starting point to stay within array bounds.
            if((indexSum - c - p - 2) > 0){
                startK = indexSum - c - p - 2;
            }
            // Limits inner for loop starting point to stay within array bounds.
            if((indexSum - r - p - 2) > 0){
                startM = indexSum - r - p - 2;
            }
            // Iterates through all values with the given index sum to move the
            // smallest value to a lower level (index sum + 1).
            for(int k=startK; k <= indexSum && k <= r-1; k++){
                for(int m=startM; m <= indexSum-k && m <= c-1; m++){
                    n = indexSum - (k + m);
                    if (n < p && tableau[k][m][n] < min){
                        int temp = tableau[k][m][n];
                        tableau[k][m][n] = min;
                        min = temp;
                    }
                }
            }
            // only move the minimum value if it isn't 0.
            if (min > 0){
                dropMin(min, indexSum+1);
            }
        }
    }

    /**
     * Sets every value in the 3D Young Tableau to 0.
     */
    public void clearAll(){
        for (int k = 0; k < p; k++){
            for (int i = 0; i < r; i++){
                for (int j = 0; j < c; j++){
                    tableau[i][j][k] = 0;
                }
            }
        }
    }

    /**
     * Displays the 3D Young Tableau pane by pane to the user.
     */
    public void printTableau(){
        System.out.println("");
        for (int k = 0; k < p; k++){
            System.out.println("Pane: " + (k + 1));
            for (int i = 0; i < r; i++){
                for (int j = 0; j < c; j++){
                    int num = tableau[i][j][k];
                    // Shows the value as a 2 digit String instead of an integer
                    // to pad single digit numbers with a 0.
                    String temp = "";
                    if (num < 10){
                        temp = "0" + num;
                    }
                    else {
                        temp = "" + num;
                    }
                    // Prints each value inside [] for clarity and uniformity.
                    System.out.print("[" + temp + "]");
                }
                // Starts a new row.
                System.out.println("");
            }
            // Draws a line the width of the tableau after each pane.
            for (int pad = 0; pad < c; pad++){
                System.out.print("----");
            }
            System.out.println("");
        }   
    }
}