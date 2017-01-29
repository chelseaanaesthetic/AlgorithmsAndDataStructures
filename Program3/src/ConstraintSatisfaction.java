/**
 * ICS340.01 - Program 3
 *
 * This program takes 3 files selected by the user:  a domain file, a variables file,
 * and a constraints file. It uses the generalized arc consistency algorithm to prune
 * the domains of these variables based on the constraints to solve the constraint
 * satisfaction problem.
 *
 * @author Chelsea Hanson.
 */
package src;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.util.Iterator;
import java.util.StringTokenizer;


public class ConstraintSatisfaction {

    private static boolean possible = true;

    private static File variables = null;
    private static File constraints = null;
    private static File domain = null;

    private static File inputFile = null;
    private static BufferedReader inputStream;

    private static File outputFile = null;
    private static PrintWriter outputStream;

    private static CollectionList<String> domainSet;
    private static CollectionList<Variable> variableList;
    private static CollectionList<Constraint> constraintList;
    private static TDA tda;

    /**
     * This main method runs a generalized arc consistency algorithm to prune
     * domains of a constraint satisfaction problem.
     * @param args - default/not used
     */
    public static void main(String[] args){
        tda = TDA.instance();
        domainSet = new CollectionList<String>();
        variableList = new CollectionList<Variable>();
        constraintList = new CollectionList<Constraint>();

        readFiles();

        Arc arc;
        boolean changedDomain;
        while (tda.hasMoreArcs()) {
            arc = tda.nextArc();

            outputStream.println(arc.toString());
            tda.printTo(outputStream);
            outputStream.println();

            changedDomain = checkConstraints(arc);

            if (arc.getActiveVariable().getDomain().isEmpty()){
                outputStream.println("This constraint can not be satisfied!");
                break;
            }
            if (changedDomain) {
                findOtherArcsFor(arc);
                outputStream.println(arc.getActiveVariable().toString());
                tda.printTo(outputStream);
            }
            else if (!changedDomain){
                outputStream.println(arc.getActiveVariable().getName() + " is unchanged.");
            }

            outputStream.println("---------------------------------------------");
            outputStream.println();
        }
        printAllVariables();
        outputStream.close();
    }

    /**
     * This method gets the files needed to run a constraint satisfaction from the user.
     */
    private static void readFiles(){
        String testLine;
        char test;

        try {
            System.out.println("Select files to use as input.");
            // While there is still a null file...
            while (variables == null || constraints == null || domain == null) {
                // Opens a window to select a text file from the same directory this program runs from.
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files", "txt"));
                fileChooser.setCurrentDirectory(new File(".").getAbsoluteFile());
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    inputFile = fileChooser.getSelectedFile();
                    inputStream = new BufferedReader(new FileReader(inputFile));
                    testLine = inputStream.readLine();
                    test = testLine.charAt(0);

                    switch (test) {
                        case 'd':
                            domain = inputFile;
                            break;
                        case 'v':
                            variables = inputFile;
                            break;
                        default:
                            constraints = inputFile;
                            break;
                    }
                }
                inputStream.close();
            }

            writeDomain(new BufferedReader(new FileReader(domain)));
            writeVariables(new BufferedReader(new FileReader(variables)));
            writeConstraints(new BufferedReader(new FileReader(constraints)));

            outputFile = new File("ConstraintSatisfaction.txt");
            outputStream = new PrintWriter(outputFile);
        }
        catch (FileNotFoundException e){
            System.out.println("Problem opening files.");
        }
        catch (IOException e){
            System.out.println("Error reading from a file.");
        }
        catch (Exception e){
            // If any error occurs,
            e.printStackTrace();
        }
    }

    /**
     * Sets up the initial domain from the file selected by the user.
     * @param inputStream - a reader on the domain file
     * @throws IOException
     */
    private static void writeDomain(BufferedReader inputStream) throws IOException{
        String line;
        while ((line = inputStream.readLine()) != null) {
            domainSet.add(line.substring(1));
        }
    }

    /**
     * Creates variables with the names from the file selected by the user
     * and the initial domain built from a file
     * @param inputStream - a reader on the variables file
     * @throws IOException
     */
    public static void writeVariables(BufferedReader inputStream) throws IOException {
        String line;
        while ((line = inputStream.readLine()) != null) {
            Variable v = new Variable(line, domainSet);
            variableList.add(v);
        }
    }

    /**
     * Creates constraints from a file to manipulate the domains of the variables previously created
     * and breaks each into arcs and adds them to the TDA for further processing.
     * @param inputStream - a reader on the constraint file
     * @throws IOException
     */
    private static void writeConstraints(BufferedReader inputStream) throws IOException {
        String line;
        StringTokenizer tokens;
        Constraint constraint;

        while ((line = inputStream.readLine()) != null) {
            tokens = new StringTokenizer(line);

            int number = Integer.parseInt(tokens.nextToken());
            String variable = tokens.nextToken();
            Variable var1 = variableList.search(variable.substring(2, variable.length()-1));
            String operator = tokens.nextToken();
            String operand = tokens.nextToken();

            if (operand.startsWith("d(v")) {
                Variable var2 = variableList.search(operand.substring(2, operand.length()-1));

                constraint = new Constraint(number, var1, operator, var2);
                constraintList.add(constraint);
                tda.addToEnd(constraint.toArc(var1));
                tda.addToEnd(constraint.toArc(var2));
            }
            else {
                constraint = new Constraint(number, var1, operator, operand.substring(1));
                constraintList.add(constraint);
                tda.addToStart(constraint.toArc(var1));
            }
        }
    }

    /**
     * This method runs the logic for modifying domains.
     * @param arc - the arc to check
     * @return true if the domain of the active variable in the arc changes
     */
    private static boolean checkConstraints(Arc arc) {
        boolean changed = false;
        String operator = arc.getOperator();
        // domain of Variable = domain element
        if (operator.equals("=") && arc.getOperand() instanceof String) {
            if (arc.getActiveVariable().getDomain().contains((String)arc.getOperand())) {
                changed = arc.getActiveVariable().getDomain().reduceTo((String)arc.getOperand());
            }
            else {
                changed = arc.getActiveVariable().getDomain().dumpAll();
                possible = false;
            }
        }
        // domain of Variable = domain of Variable
        else if (operator.equals("=") && arc.getOperand() instanceof Variable) {
            String element;
            Iterator<String> activeDomain = (arc.getActiveVariable()).getDomain().iterator();
            while (activeDomain.hasNext()) {
                element = activeDomain.next();
                if (!((Variable)arc.getOperand()).getDomain().contains(element)) {
                    activeDomain.remove();
                    changed = true;
                }
            }
        }
        // domain of Variable != domain element
        else if (operator.equals("!=") && arc.getOperand() instanceof String) {
            if (arc.getActiveVariable().getDomain().contains((String)arc.getOperand())) {
                changed = arc.getActiveVariable().getDomain().pruneOut((String)arc.getOperand());
            }
        }
        // domain of Variable != domain of Variable
        else if (operator.equals("!=") && arc.getOperand() instanceof Variable) {
            if (((Variable)arc.getOperand()).getDomain().size() == 1) {
                Iterator<String> operandDomain = ((Variable)arc.getOperand()).getDomain().iterator();
                while (operandDomain.hasNext()) {
                    changed = arc.getActiveVariable().getDomain().pruneOut(operandDomain.next());
                }
            }

        }

        if (arc.getActiveVariable().getDomain().isEmpty()) {
            possible = false;
        }

        return changed;
    }

    /**
     * Checks the list of constraints for other constraints that involve the variable in the
     * selected arc and adds the arc from that constraint affecting the variable to the TDA
     * @param arc - the arc and variable involved that we want every other arc for
     */
    private static void findOtherArcsFor(Arc arc) {
        Iterator<Constraint> constraintIterator = constraintList.iterator();
        while (constraintIterator.hasNext()) {
            Constraint working = constraintIterator.next();
            if (working.contains(arc.getActiveVariable()) && !working.toArc(arc.getActiveVariable()).equals(arc)){
                tda.addToStart(working.toArc(arc.getActiveVariable()));
            }
        }
    }

    /**
     * Writes all the variables to the file.
     */
    public static void printAllVariables() {
        Iterator<Variable> iterator = variableList.iterator();
        while (iterator.hasNext()) {
            outputStream.println(iterator.next().toString());
        }
    }
}
