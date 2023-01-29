// generated with ast extension for cup
// version 0.8
// 12/0/2023 13:2:2


package rs.ac.bg.etf.pp1.ast;

public class MoreDesig_comma extends MoreDesig {

    private Desig Desig;
    private MoreDesig MoreDesig;

    public MoreDesig_comma (Desig Desig, MoreDesig MoreDesig) {
        this.Desig=Desig;
        if(Desig!=null) Desig.setParent(this);
        this.MoreDesig=MoreDesig;
        if(MoreDesig!=null) MoreDesig.setParent(this);
    }

    public Desig getDesig() {
        return Desig;
    }

    public void setDesig(Desig Desig) {
        this.Desig=Desig;
    }

    public MoreDesig getMoreDesig() {
        return MoreDesig;
    }

    public void setMoreDesig(MoreDesig MoreDesig) {
        this.MoreDesig=MoreDesig;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Desig!=null) Desig.accept(visitor);
        if(MoreDesig!=null) MoreDesig.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Desig!=null) Desig.traverseTopDown(visitor);
        if(MoreDesig!=null) MoreDesig.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Desig!=null) Desig.traverseBottomUp(visitor);
        if(MoreDesig!=null) MoreDesig.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MoreDesig_comma(\n");

        if(Desig!=null)
            buffer.append(Desig.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MoreDesig!=null)
            buffer.append(MoreDesig.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MoreDesig_comma]");
        return buffer.toString();
    }
}
