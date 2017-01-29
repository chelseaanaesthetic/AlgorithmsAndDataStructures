/**
 * ICS340.01 - Program 2
 *
 * This program inputs a file from the user containing cities and their
 * distances from each other. It proceeds to calculate a shortest path
 * to travel to all cities and return to the start, also known as the
 * Traveling Salesperson Problem.
 *
 * @author Chelsea Hanson.
 */

import java.io.*;
import java.util.ArrayDeque;
import java.util.StringTokenizer;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TravelingSalesperson {

    // file selected by the user
    public static File inputFile = null;
    public static File latLong = null;
    public static File outputFile = null;
    public static BufferedReader inputStream;
    public static PrintWriter outputStream;

    // max of 10 cities plus 1 for the letter
    public static String[] cityList;
    public static String[][] cities = new String[11][11];
    public static String[][] latAndLong = new String[11][3];
    public static int actualMax;
    public static String[][] memoized;
    public static int memoIndexCurrent = 1;
    public static int memoIndexStart = 1;
    public static int infinity = Integer.MAX_VALUE;


    /**
     * The main method reads a file that the user chooses, and then
     * runs all 3 TSP algorithms on the cities from the file.
     *
     * @param args default for main method
     */
    public static void main(String args[]){
        System.out.println("\nChoose a file of cities...");
        readFile();

        try{
            greedyAlgorithm();
            bitonicTourAlgorithm();
            exactDynamicProgrammingAlgorithm();
        }
        catch (FileNotFoundException fnf){
            System.out.println("Error writing results file.");
        }
    }

    /**
     * This method lets a user choose a file. This file is then read into the
     * appropriate array for retrieval later.
     */
    private static void readFile(){
        try {
            // Opens a window to select a text file from the same directory this program runs from.
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
            fileChooser.setCurrentDirectory(new File(".").getAbsoluteFile());

            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                if (inputFile == null){
                    inputFile = fileChooser.getSelectedFile();
                    System.out.println("Input File: " + inputFile.getName());
                    inputStream = new BufferedReader(new FileReader(inputFile));
                    writeCities(inputStream);
                }
                else if (inputFile != null){
                    latLong = fileChooser.getSelectedFile();
                    System.out.println("Lat-Long File: " + latLong.getName());
                    inputStream = new BufferedReader(new FileReader(latLong));
                    writeLatLong(inputStream);
                }
            }

            inputStream.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Problem opening files.");
        }
        catch (IOException e){
            System.out.println("Error reading from \"" + inputFile +"\".");
        }
        catch (Exception e){
            // If any error occurs,
            e.printStackTrace();
        }
    }

    /**
     * This method copies the contents of a cities file into the cities array
     * @param inputStream - the file reader
     * @throws IOException
     */
    private static void writeCities(BufferedReader inputStream) throws IOException{
        String line;
        int i = 0;
        while ((line = inputStream.readLine()) != null){
            StringTokenizer tokens = new StringTokenizer(line);
            int j = 0;
            while (tokens.hasMoreTokens()){
                cities[i][j] = tokens.nextToken();
                cities[j][i] = cities[i][j];
                j++;
            }
            actualMax = j;
            i++;
        }

        cityList = untraveledArray(cities);
        initializeMemo();
    }

    /**
     * This method copies the contents of a lat-long file into the latAndLong array
     * @param inputStream - the file reader
     * @throws IOException
     */
    private static void writeLatLong(BufferedReader inputStream)throws IOException{
        String line;
        int i = 0;
        while ((line = inputStream.readLine()) != null){
            StringTokenizer tokens = new StringTokenizer(line);
            int j = 0;
            while (tokens.hasMoreTokens()){
                latAndLong[i][j] = tokens.nextToken();
                j++;
            }
            i++;
        }
    }

    /**
     * Creates and resets a list of all possible cities
     * @return a String array of all cities
     */
    private static String[] untraveledArray(String[][] array){
        String[] untraveled = new String[actualMax];
        untraveled[0] = "0";
        for (int i = 1; i < actualMax; i++){
            untraveled[i] = array[i][0];
        }
        return untraveled;
    }

    /**
     * This helper method sets up a memo table for faster execution of the
     * dynamic programming algorithm.
     */
    private static void initializeMemo(){
        // max val of (n choose k) for n = max 10 cities
        int col = 252;
        memoized = new String[cityList.length-1][col];

        for (int i = 0; i < actualMax-1; i++){
            memoized[i][0] = cityList[i+1];
        }
    }

    /**
     * This helper method gets the index for the city abbreviations
     * given by the user and looks at the table of distances to
     * returns the distance between the two input cities.
     *
     * @param row - a String representing a source city
     * @param col - a String representing a destination city
     * @return the distance between the two cities
     */
    private static int distanceBetween(String col, String row){
        return (Integer.parseInt(cities[indexFor(row)][indexFor(col)]));
    }

    /**
     * This helper method splits a String representation of a set
     * into an array of String city prefixes
     * @param set - a formatted String representation of a set
     * @return an array list of city prefixes from the formatted set
     */
    private static String[] getList(String set){
        String broken = set.substring(set.indexOf("{") + 1, set.indexOf("}"));
        String[] citiesList = broken.split("[{,}]+");
        return citiesList;
    }

    /**
     * This helper method converts an array of cities to a single string
     * of cities in set formatting
     * @param cities - array of cities to be put in set format
     * @return a formatted set string
     */
    private static String getSet(String[] cities){
        String set = "{" + cities[0];
        for (int i = 0; i < cities.length; i++){
            set += "," + cities[i];
        }
        set += "}";
        return set;
    }

    /**
     * This helper method converts an array of cities to a single string.
     * @param citiesList - an array of cities
     * @return a single string of cities
     */
    private static String getString(String[] citiesList){
        String citiesString = "";
        for (int i = 0; i < citiesList.length; i++){
            citiesString += citiesList[i];
        }
        return citiesString;
    }

    /**
     * This helper method checks a stored matrix of known shortest distances
     * between cities to cut calculation times and reduce duplicate calculations.
     * @param set - a list of cities
     * @return the distance and last city visited from the shortest distance for
     * that set saved in a memoized matrix;
     * Note: Odd indices represent distance, even indices represent last city visited
     */
    private static String[] checkMemo(String set){
        int index = 1;
        String shortest = "";
        int shortestDistance = infinity;

        // find column of subset options
        for (int k = memoIndexStart; k < memoIndexCurrent; k++){
            if (memoized[0][k].equals(set)){
                index = k;
                break;
            }
        }

        // find row with lowest cost
        for (int i = 1; i < cityList.length - 2; i++) {

            if (memoized[i][index] != null && Integer.parseInt(memoized[i][index]) < shortestDistance){
                shortest += memoized[i][index] + " " + memoized[i][0] + " ";
            }
        }
        String[] memo = shortest.split(" ");
        return memo;
    }

    /**
     * This helper method adds calculated information to a table for later use
     * @param set the set of cities visited
     * @param last the last city visited
     * @param distance the shortest distance to visit all in the set.
     */
    private static void addToMemo(String[] set, String last, int distance){
        String setToAdd = getString(set);
        int setIndex = memoIndexCurrent++;

        memoized[0][setIndex] = setToAdd;
        for (int i = 1; i < cityList.length - 1; i++){
            if (last.equals(memoized[i][0])){
                memoized[i][setIndex] = "" + distance;
            }
            else if (memoized[i][setIndex] == null){
                memoized[i][setIndex] = "" + infinity;
            }
        }
    }

    /**
     * This helper method finds the shortest distance to travel among all
     * the cities in the setList.
     * @param setList - an array of city prefixes to travel between
     * @return a list of string representations of the shortest distance
     */
    private static String[] checkDistances(String[] setList){
        int distance;
        int shortestDistance = infinity;
        String[] subset = new String[setList.length - 1];
        subset[0] = setList[0];
        String[] memo;
        String[] shortestSubpath = new String[setList.length];
        int resultsIndex = 0;

        for (int last = 1; last < setList.length; last++) {
            int temp = 1;
            for (int j = 1; j < setList.length; j++) {
                if (j != last) {
                    subset[temp++] = setList[j];
                }
            }

            memo = checkMemo(getString(subset));
            for (int k = 0; k < memo.length / 2; k++) {
                int memoDistance = Integer.parseInt(memo[k]);
                String memoLastCity = memo[k+1];

                distance = memoDistance + distanceBetween(memoLastCity, setList[last]);

                if (distance == shortestDistance) {
                    shortestSubpath[resultsIndex++] = (setList[last] + ") = " + distance);
                    addToMemo(setList, setList[last], distance);
                }
                else if (distance < shortestDistance) {
                    resultsIndex = 0;
                    shortestSubpath[resultsIndex++] = (memo[k+1] + ") = " + distance);
                    addToMemo(setList, setList[last], distance);
                }
            }
        }

        return shortestSubpath;
    }

    /**
     * This helper method converts a letter into the corresponding index
     * number for searching the cities matrix
     * @param letter - the city prefix to get index for
     * @return index number for specified letter prefix
     */
    private static int indexFor(String letter){
        int index = 0;
            for (int i = 1; i < actualMax; i++){
                if (cityList[i].equals(letter)){
                    index = i;
                }
            }
        return index;
    }

    /**
     * This method runs the Greedy Algorithm and writes the results
     * to a new file with the specified naming convention.
     */
    private static void greedyAlgorithm() throws FileNotFoundException{
        outputFile = saveFile("g_");
        outputStream = new PrintWriter(new FileOutputStream(outputFile));

        String[] citiesUntraveled = untraveledArray(cities);
        String citiesTraveled = cities[1][0];
        citiesUntraveled[1] = "0";
        int totalDistance = 0;

        int test = 0;
        int next = 0;
        int current = 1;
        int closestDistance;
        String closestName = "";

        while (citiesTraveled.length() < actualMax - 1){
            closestDistance = 9999;

            for (int i = 1; i < actualMax; i++) {
                test = Integer.parseInt(cities[i][current]);
                if (citiesUntraveled[i].equals("0")){
                    continue;
                }
                else if (test < closestDistance) {
                    closestDistance = Integer.parseInt(cities[current][i]);
                    closestName = cities[i][0];
                    next = i;
                }
            }

            citiesUntraveled[next] = "0";
            citiesTraveled += closestName;
            totalDistance += closestDistance;

            current = next;
            next = 0;
        }
        citiesTraveled += cities[0][1];
        totalDistance += Integer.parseInt(cities[current][1]);

        outputStream.println(citiesTraveled);
        outputStream.println("Distance = " + totalDistance);
        outputStream.close();
    }

    /**
     * This method runs the Bitonic Tour Algorithm and writes the
     * results to a new file with the specified naming convention.
     */
    private static void bitonicTourAlgorithm() throws FileNotFoundException{
        System.out.println("\nChoose the lat-long file...");
        readFile();
        outputFile = saveFile("bt_");
        outputStream = new PrintWriter(new FileOutputStream(outputFile));

        String[] untraveled = untraveledArray(latAndLong);
        String partial1 = "";
        int partialDis1 = 0;
        String partial1End = "";

        String partial2 = "";
        int partialDis2 = 0;
        String partial2End = "";

        int remaining = actualMax - 1;
        String[] test;
        int testVal;
        int maxLatitude = 0;
        String next = "City";
        int visited = 0;

        while (remaining-- > 0) {
            for (int k = 1; k < actualMax; k++) {
                test = latAndLong[k][1].split(",");
                testVal = Integer.parseInt(test[0]);
                if (testVal > maxLatitude && !(untraveled[k].equals("0"))) {
                    maxLatitude = testVal;
                    next = latAndLong[k][0];
                    visited = k;
                }
            }
            untraveled[visited] = "0";

            if (partial1.length() == 0) {
                partial1End = next;
                partial1 += partial1End;

                partial2End = next;
                partial2 += partial2End;

                outputStream.println("First city is " + next + ".");
                outputStream.println("-------");
            }
            else if (partial1.length() == 1) {
                outputStream.println("Next city is " + next + ".");
                outputStream.println("Only possible partial path is:");
                partialDis1 += distanceBetween(partial1End, next);

                partial1End = next;
                partial1 += partial1End;
                outputStream.println(partial1 + " & " + partial2 + ", partial distance = "
                        + (partialDis1 + partialDis2));
                outputStream.println("-------");
            }
            else {
                int temp;
                int tempDis1 = distanceBetween(partial1End, next);
                int tempDis2 = distanceBetween(partial2End, next);

                outputStream.println("Next city is " + next + ".  Possible partial paths are:");

                temp = partialDis1 + tempDis1 + partialDis2;
                outputStream.println(partial1 + next + " & " + partial2 +
                        ", partial distance = " + temp);

                temp = partialDis1 + partialDis2 + tempDis2;
                outputStream.println(partial1 + " & " + partial2 + next +
                        ", partial distance = " + temp);

                if (tempDis1 < tempDis2){
                    partialDis1 += tempDis1;
                    partial1End = next;
                    partial1 += partial1End;
                }
                else {
                    partialDis2 += tempDis2;
                    partial2End = next;
                    partial2 += partial2End;
                }

                outputStream.println("Optimal partial path is:  " + partial1 + " & " + partial2);
                outputStream.println("-------");
            }
            maxLatitude = 0;
        }
        outputStream.println("All cities are visited, remaining edge is from " +
            partial1End + " to " + partial2End + ".");

        partialDis1 += partialDis2;
        for (int m = partial2.length() - 1; m >= 0; m--){
            partial1 += partial2.charAt(m);
        }

        outputStream.println("Final path is " + partial1 + ".");
        outputStream.println("Total distance is " + partialDis1);
        outputStream.close();
    }

    /**
     * This method runs the Exact Dynamic Programming Algorithm and
     * writes the results to a new file with the specified naming
     * convention.
     */
    private static void exactDynamicProgrammingAlgorithm() throws FileNotFoundException{
        outputFile = saveFile("dp_");
        outputStream = new PrintWriter(new FileOutputStream(outputFile));

        int setSize = 1;
        String working;
        String[] citiesSet;
        int lastIndex;
        String newSet;
        String last;
        ArrayDeque<String> frontier = new ArrayDeque<String>();
        String shortestRoute = "";
        int shortestDistance = infinity;
        String[] shortestSubpath;

        frontier.add("{" + cityList[1] + "}");
        frontier.add("s = " + ++setSize);

        while (!frontier.isEmpty()) {
            working = frontier.remove();
            if (working.charAt(0) == '{') {
                citiesSet = getList(working);
                last = citiesSet[citiesSet.length - 1];
                lastIndex = indexFor(last);

                if (citiesSet.length == 1){
                    addToMemo(citiesSet, last, 0);
                }
                else if (citiesSet.length == 2){
                    outputStream.println("S = " + working);
                    int distance = distanceBetween(citiesSet[0], citiesSet[1]);
                    outputStream.println("C(" + working + "," + last + ") = " + distance);
                    outputStream.println("");
                    addToMemo(citiesSet, last, distance);
                }
                else if (citiesSet.length < actualMax - 1){
                    outputStream.println("S = " + working);
                    shortestSubpath = checkDistances(citiesSet);
                    for (String item : shortestSubpath){
                        outputStream.println("C(" + working + "," + item);
                    }
                    outputStream.println("");
                }
                else if (citiesSet.length < actualMax){
                    outputStream.println("");
                    outputStream.println("S = " + working);
                    shortestSubpath = checkDistances(citiesSet);
                    shortestRoute = shortestSubpath[0];
                    outputStream.println("C(" + working + ") = " + shortestDistance);
                }
                for (int k = lastIndex+1; k < actualMax; k++){
                    newSet = working.replace("}", ("," + cityList[k] + "}"));
                    frontier.add(newSet);
                    addToMemo(getList(newSet), cityList[k], infinity);
                }
            }
            else if (working.charAt(0) == 's' && setSize < actualMax - 1) {
                frontier.add("s = " + ++setSize);
                outputStream.println(working);
                outputStream.println("");
            }
            else {
                outputStream.println(working);
            }
        }

        outputStream.println("Shortest route:  " + shortestRoute);
        outputStream.close();
    }

    /**
     * This method creates a new File to fit naming specifications.
     * @param prefix - identifier to add to file name.
     * @return a properly named File to write to.
     */
    private static File saveFile(String prefix){
        // Creates a new File to naming specifications.
        String outputName = inputFile.getAbsolutePath().replaceAll
                            (inputFile.getName(), (prefix + inputFile.getName()));
        return new File(outputName);
    }
}