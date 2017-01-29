package src;

/**
 * This class deals with the functionality of a variable.
 */
public class Variable implements Comparable<Variable> {

    private String name;
    private Domain domain;
    
    Variable(String name, CollectionList<String> domain){
        this.name = name;
        this.domain = new Domain(domain.iterator());
    }

    /**
     * @return the name of the variable
     */
    public String getName() {
        return name;
    }

    /**
     * @return the domain of the variable
     */
    public Domain getDomain() {
        return domain;
    }

    /**
     * @param v - the variable to compare with
     * @return 0 for equality
     *         1 for the same name
     *         -1 for not similar
     */
    @Override
    public int compareTo(Variable v){
        if (name.equals(v.getName())) {
            if (domain.equals(v.getDomain())) {
                return 0;
            }
            return 1;
        }
        return -1;
    }

    /**
     * Checks for equality
     * @param object - the thing being compared with
     * @return true if the two are the same
     */
    @Override
    public boolean equals (Object object){
        if (object instanceof String) {
            return (name.equals(object));
        }
        else if(object instanceof Variable) {
            return (this.compareTo((Variable)object) == 0);
        }
        return false;
    }

    /**
     * A formatted representation of a variable
     * @return the string representation
     */
    public String toString(){
        return name + ": " + domain.toString();
    }
}