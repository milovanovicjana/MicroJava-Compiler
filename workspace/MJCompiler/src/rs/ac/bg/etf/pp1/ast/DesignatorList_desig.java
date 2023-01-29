// generated with ast extension for cup
// version 0.8
// 12/0/2023 13:2:2


package rs.ac.bg.etf.pp1.ast;

public class DesignatorList_desig extends DesignatorList {

    private Desig Desig;

    public DesignatorList_desig (Desig Desig) {
        this.Desig=Desig;
        if(Desig!=null) Desig.setParent(this);
    }

    public Desig getDesig() {
        return Desig;
    }

    public void setDesig(Desig Desig) {
        this.Desig=Desig;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Desig!=null) Desig.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Desig!=null) Desig.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Desig!=null) Desig.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorList_desig(\n");

        if(Desig!=null)
            buffer.append(Desig.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorList_desig]");
        return buffer.toString();
    }
}
