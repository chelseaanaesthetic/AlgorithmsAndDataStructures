package src;

/**
 * Allows the creation and manipulation of Constraints.
 * Created by Chelsea on 11/11/2015.
 */
public class Constraint implements Comparable<Constraint> {
    private int number;
    private Variable operand1;
    private String operator;
    private Object operand2;

    public Constraint(int n, Variable v, String s, Object o) {
        this.number = n;
        this.operand1 = v;
        this.operator = s;
        this.operand2 = o;
    }

    public int getNumber(){
        return number;
    }

    /**
     * Checks if the constraint deals with the specified variable
     * @param var - the variable in question
     * @return true if the variable is part of the constraint
     */
    public boolean contains(Variable var) {
        return(operand1.equals(var) || (operand2 instanceof Variable && operand2.equals(var)));
    }

    /**
     * Creates an arc with the specified variable as the active variable
     * @param var - the variable to become the active variable
     * @return an arc build from these specifications
     */
    public Arc toArc(Variable var) {
        if (operand1.equals(var) && operand2 instanceof String) {
            return new Arc(this, var, operator, (String)operand2);
        }
        else if (operand1.equals(var) && operand2 instanceof Variable) {
            return new Arc(this, var, operator, (Variable)operand2);
        }
        else if (operand2.equals(var)){
            return new Arc(this, (Variable)operand2, operator, operand1);
        }
        return null;
    }

    /**
     * Compares a constraint with this constraint
     * @param c - the constraint in question
     * @return 0 if the two are equal
     *         1 if the the two share a variable
     *         -1 if they are not similar
     */
    public int compareTo(Constraint c) {
        if (this.getNumber() == c.getNumber()) {
            return 0;
        }
        else if (c.contains(operand1) || (operand2 instanceof Variable && c.contains((Variable)operand2))) {
            return 1;
        }
        return -1;
    }

    @Override
    public boolean equals (Object object) {
        if(object instanceof Constraint) {
            Constraint inputConstraint = (Constraint)object;
            return (this.compareTo(inputConstraint) == 0);
        }
        return false;
    }

    public String toString(){
        if (operand2 instanceof Variable) {
            return number + " d(" + operand1.getName() + ") " +
                    operator + " d(" + ((Variable) operand2).getName() + ")";
        }
        else {
            return number + " d(" + operand1.getName() + ") " +
                    operator + " d" + operand2;
        }
    }
}
