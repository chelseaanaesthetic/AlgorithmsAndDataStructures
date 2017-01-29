package src;

import java.io.PrintWriter;
import java.util.LinkedList;

/**
 * This class serves as the TDA or To Do Arcs structure and allows
 * for adding and removing arcs as needed.
 */
public class TDA implements TDAadapter{
    private static TDA tda;
    private LinkedList<Arc> toDoArcs;

    private TDA() {
        toDoArcs = new LinkedList<>();
    }

    /**
     * Singleton
     * @return either the current tda if it exists else creates and returns a new one
     */
    public static TDA instance() {
        if (tda == null) {
            tda = new TDA();
        }
        return tda;
    }

    /**
     * @return the number of arcs in the TDA
     */
    public int size() {
        return toDoArcs.size();
    }

    /**
     * @return true if there are arcs in the TDA
     */
    public boolean hasMoreArcs() {
        return (toDoArcs.size() > 0);
    }

    /**
     * @return the first arc in the TDA
     */
    public Arc nextArc() {
        return toDoArcs.pollFirst();
    }

    /**
     * @param arc
     * @return true if the TDA already contains the specified arc
     */
    public boolean contains(Arc arc){
        for (Arc a : toDoArcs) {
            if (arc.equals(a)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Places the specified arc at the beginning of the TDA
     * @param arc
     */
    public void addToStart(Arc arc) {
        toDoArcs.addFirst(arc);
    }

    /**
     * Places the specified arc at the end of the TDA
     * @param arc
     */
    public void addToEnd(Arc arc) {
        toDoArcs.addLast(arc);
    }

    /**
     * Writes the list of TDA arcs to a file
     * @param outputStream
     */
    public void printTo(PrintWriter outputStream) {
        String tdaList = "";
        for (Arc a : toDoArcs) {
            tdaList += (a.getConstraint().getNumber() + ", ");
        }
        if (tdaList.length() > 2) {
            outputStream.println(tdaList.substring(0, tdaList.length()-2));
        }
    }
}
