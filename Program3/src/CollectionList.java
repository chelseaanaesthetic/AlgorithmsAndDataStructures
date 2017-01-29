package src;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Serves as a collection for a list of objects with a generic type.
 * Used for another course here at Metro, but handy for this program.
 *
 * @author Chelsea Hanson
 * @param <E> -- The type of objects to make a list of
 */
public class CollectionList<E extends Comparable<E>> {

    private List<E> list;

    /**
     * Instantiates an empty generic type linked list
     */
    public CollectionList() {
        list = new LinkedList<E>();
    }

    /**
     * Adds the element to the collection list
     * @param -- element to add into this collection
     */
    public void add(E element) {
        list.add(element);
    }

    /**
     * Removes the element from the collection list
     * @param -- element to remove from this collection
     */
    public void remove(E element) {
        for(int i = 0; i<list.size();i++) {
            if(element.equals(list.get(i))) {
                list.remove(i);
                break;
            }
        }
    }

    /**
     * Searches the collection list for a specific element
     * @param -- element to search for
     * @return -- found element or null if element is not found
     */
    public E search(E element) {
        for(int i = 0; i<list.size();i++) {
            if(element.equals(list.get(i))) {
                return list.get(i);
            }
        }
        return null;
    }

    public E search(String element) {
        for (E item : list) {
            if (item.equals(element)){
                return item;
            }
        }
        return null;
    }

    /**
     * Returns the iterator of the specific object in the collection list
     * @return -- Iterator of this collection
     */
    public Iterator<E> iterator() {
        return list.iterator();
    }

    /**
     * Returns the element at a specific index
     * @param -- index of element in this collection
     * @return -- returns element at the index
     */
    public E get(int index) {
        return list.get(index);
    }

    /**
     * Returns the size of the collection list
     * @return -- size of this collection
     */
    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        if (size() == 0){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "Items In Collection: " + list.size();
    }
}
