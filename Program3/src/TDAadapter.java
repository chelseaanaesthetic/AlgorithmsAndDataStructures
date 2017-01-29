package src;

import java.io.PrintWriter;

/**
 * This interface outlines what will be the TDA structure.
 */
public interface TDAadapter {
    public Arc nextArc();
    public boolean hasMoreArcs();
    public void addToStart(Arc arc);
    public void addToEnd(Arc arc);
    public boolean contains(Arc arc);
    public void printTo(PrintWriter outputStream);
}
