// generated with ast extension for cup
// version 0.8
// 12/0/2023 13:2:2


package rs.ac.bg.etf.pp1.ast;

public class ActParsList_ap extends ActParsList {

    private ActPar ActPar;
    private ActParMore ActParMore;

    public ActParsList_ap (ActPar ActPar, ActParMore ActParMore) {
        this.ActPar=ActPar;
        if(ActPar!=null) ActPar.setParent(this);
        this.ActParMore=ActParMore;
        if(ActParMore!=null) ActParMore.setParent(this);
    }

    public ActPar getActPar() {
        return ActPar;
    }

    public void setActPar(ActPar ActPar) {
        this.ActPar=ActPar;
    }

    public ActParMore getActParMore() {
        return ActParMore;
    }

    public void setActParMore(ActParMore ActParMore) {
        this.ActParMore=ActParMore;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ActPar!=null) ActPar.accept(visitor);
        if(ActParMore!=null) ActParMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ActPar!=null) ActPar.traverseTopDown(visitor);
        if(ActParMore!=null) ActParMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ActPar!=null) ActPar.traverseBottomUp(visitor);
        if(ActParMore!=null) ActParMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ActParsList_ap(\n");

        if(ActPar!=null)
            buffer.append(ActPar.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ActParMore!=null)
            buffer.append(ActParMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ActParsList_ap]");
        return buffer.toString();
    }
}
