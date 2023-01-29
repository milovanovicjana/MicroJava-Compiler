// generated with ast extension for cup
// version 0.8
// 12/0/2023 13:2:2


package rs.ac.bg.etf.pp1.ast;

public class Condition_termlist extends Condition {

    private ConditionTermList ConditionTermList;

    public Condition_termlist (ConditionTermList ConditionTermList) {
        this.ConditionTermList=ConditionTermList;
        if(ConditionTermList!=null) ConditionTermList.setParent(this);
    }

    public ConditionTermList getConditionTermList() {
        return ConditionTermList;
    }

    public void setConditionTermList(ConditionTermList ConditionTermList) {
        this.ConditionTermList=ConditionTermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConditionTermList!=null) ConditionTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConditionTermList!=null) ConditionTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConditionTermList!=null) ConditionTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Condition_termlist(\n");

        if(ConditionTermList!=null)
            buffer.append(ConditionTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Condition_termlist]");
        return buffer.toString();
    }
}
