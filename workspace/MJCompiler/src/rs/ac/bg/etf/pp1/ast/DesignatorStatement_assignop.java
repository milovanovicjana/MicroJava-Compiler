// generated with ast extension for cup
// version 0.8
// 12/0/2023 13:2:2


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatement_assignop extends DesignatorStatement {

    private DesignatorStatementAssignop DesignatorStatementAssignop;

    public DesignatorStatement_assignop (DesignatorStatementAssignop DesignatorStatementAssignop) {
        this.DesignatorStatementAssignop=DesignatorStatementAssignop;
        if(DesignatorStatementAssignop!=null) DesignatorStatementAssignop.setParent(this);
    }

    public DesignatorStatementAssignop getDesignatorStatementAssignop() {
        return DesignatorStatementAssignop;
    }

    public void setDesignatorStatementAssignop(DesignatorStatementAssignop DesignatorStatementAssignop) {
        this.DesignatorStatementAssignop=DesignatorStatementAssignop;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorStatementAssignop!=null) DesignatorStatementAssignop.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorStatementAssignop!=null) DesignatorStatementAssignop.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorStatementAssignop!=null) DesignatorStatementAssignop.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatement_assignop(\n");

        if(DesignatorStatementAssignop!=null)
            buffer.append(DesignatorStatementAssignop.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatement_assignop]");
        return buffer.toString();
    }
}
