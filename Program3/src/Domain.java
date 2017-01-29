package src;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class deals with the functionality of the domain and allows
 * removal of a specified element, reducing down to a single specified
 * element, and clearing out the domain.
 */
public class Domain {
    private List domain;

    /**
     * Creates a new domain object based on the input list
     */
    public Domain(Iterator<String> masterDomain) {
        domain = new LinkedList<String>();
        while (masterDomain.hasNext()) {
            domain.add(masterDomain.next());
        }
    }

    /**
     * Checks if the String in question is in the domain
     * @param s - the string in question
     * @return true if s is in the domain
     */
    public boolean contains(String s) {
        return domain.contains(s);
    }

    /**
     * Removes the specified element from the domain
     * @param s - the element to remove
     */
    public boolean pruneOut(String s) {
        boolean changed = false;
        if (this.contains(s)) {
            domain.remove(s);
            changed = true;
        }
        return changed;
    }

    /**
     * Removes everything but the specified element from the domain
     * @param element - the item to end with
     */
    public boolean reduceTo(String element) {
        boolean changed = false;
        if (domain.contains(element) && domain.size() == 0) {
            return changed;
        }
        else if (domain.contains(element)) {
            dumpAll();
            domain.add(element);
            changed = true;
        }
        else {
            dumpAll();
            changed = true;
        }
        return changed;
    }

    /**
     * Clears the domain
     */
    public boolean dumpAll() {
        boolean changed = false;
        if (!this.isEmpty()) {
            changed = true;
        }
        domain.clear();
        return changed;
    }

    /**
     *
     * @return an iterator over the domain
     */
    public Iterator<String> iterator() {
        return domain.iterator();
    }

    /**
     * Returns the size of the domain
     * @return the number of elements in the domain
     */
    public int size() {
        return domain.size();
    }

    /**
     * Checks for equality with the specified object
     * @param object - the thing in question
     * @return true of the object is the same as this domain
     */
    public boolean equals(Object object) {
        if (object instanceof String) {
            return (domain.size() == 0 && domain.get(0) == object);
        }
        if (object instanceof Domain) {
            Domain test = (Domain)object;

            Iterator<String> iterator1 = this.iterator();
            Iterator<String> iterator2 = test.iterator();
            while (iterator1.hasNext()) {
                if (iterator2.hasNext() && iterator1.next().compareTo(iterator2.next()) != 0) {
                    return false;
                }
            }
            return (!iterator2.hasNext());
        }
        return false;
    }

    /**
     * Printable view of the domain
     * @return string representation of the domain
     */
    public String toString() {
        String set = "{";
        for (Object element : domain){
            set += element + ", ";
        }
        set += "}";
        return set.replace(", }", "}");
    }

    /**
     * Checks if there are any elements left in the domain
     * @return true if the domain is empty
     */
    public boolean isEmpty(){
        return (domain.size() == 0);
    }
}
