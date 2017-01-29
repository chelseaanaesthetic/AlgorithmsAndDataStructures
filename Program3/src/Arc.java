package src;

/**
 * This class deals with the functionality and storage of an arc.
 */
public class Arc implements Comparable<Arc> {
    private Constraint constraint;
    private Variable activeVariable;
    private String operator;
    private Object operand;

    public Arc(Constraint constraint, Variable activeVariable, String operator, Variable passiveVariable) {
        this.constraint = constraint;
        this.activeVariable = activeVariable;
        this.operator = operator;
        this.operand = passiveVariable;
    }

    public Arc(Constraint constraint, Variable activeVariable, String operator, String operand) {
        this.constraint = constraint;
        this.activeVariable = activeVariable;
        this.operator = operator;
        this.operand = operand;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public Variable getActiveVariable() {
        return activeVariable;
    }

    public String getOperator() {
        return operator;
    }

    public Object getOperand() {
        return operand;
    }

    /**
     * This method compares an arc to another.
     * @param a - the arc being compared
     * @return 0 if the two arcs are the same
     *         1 if the constraint is the same, but the arc is different
     *         -1 if the two are not similar
     */
    @Override
    public int compareTo(Arc a) {
        if (this.activeVariable.equals(a.getActiveVariable()) && this.operator.equals(a.getOperator())
                && this.operand.equals(a.getOperand())) {
            return 0;
        }
        else if (this.getConstraint().equals(a.getConstraint())){
            return 1;
        }
        return -1;
    }

    @Override
    public boolean equals (Object object) {
        if (object instanceof Arc) {
            Arc inputArc = (Arc)object;
            return (this.compareTo(inputArc) == 0);
        }
        return false;
    }

    public String toString() {
        return ("<" + activeVariable.getName() + ", " + constraint.toString() + ">");
    }
}
