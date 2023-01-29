// generated with ast extension for cup
// version 0.8
// 12/0/2023 13:2:2


package rs.ac.bg.etf.pp1.ast;

public class Statement_foreach extends Statement {

    private DesignatorForeach DesignatorForeach;
    private ForeachLoopStart ForeachLoopStart;
    private ForeachIdentName ForeachIdentName;
    private Statement Statement;
    private EndForeach EndForeach;

    public Statement_foreach (DesignatorForeach DesignatorForeach, ForeachLoopStart ForeachLoopStart, ForeachIdentName ForeachIdentName, Statement Statement, EndForeach EndForeach) {
        this.DesignatorForeach=DesignatorForeach;
        if(DesignatorForeach!=null) DesignatorForeach.setParent(this);
        this.ForeachLoopStart=ForeachLoopStart;
        if(ForeachLoopStart!=null) ForeachLoopStart.setParent(this);
        this.ForeachIdentName=ForeachIdentName;
        if(ForeachIdentName!=null) ForeachIdentName.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.EndForeach=EndForeach;
        if(EndForeach!=null) EndForeach.setParent(this);
    }

    public DesignatorForeach getDesignatorForeach() {
        return DesignatorForeach;
    }

    public void setDesignatorForeach(DesignatorForeach DesignatorForeach) {
        this.DesignatorForeach=DesignatorForeach;
    }

    public ForeachLoopStart getForeachLoopStart() {
        return ForeachLoopStart;
    }

    public void setForeachLoopStart(ForeachLoopStart ForeachLoopStart) {
        this.ForeachLoopStart=ForeachLoopStart;
    }

    public ForeachIdentName getForeachIdentName() {
        return ForeachIdentName;
    }

    public void setForeachIdentName(ForeachIdentName ForeachIdentName) {
        this.ForeachIdentName=ForeachIdentName;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public EndForeach getEndForeach() {
        return EndForeach;
    }

    public void setEndForeach(EndForeach EndForeach) {
        this.EndForeach=EndForeach;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignatorForeach!=null) DesignatorForeach.accept(visitor);
        if(ForeachLoopStart!=null) ForeachLoopStart.accept(visitor);
        if(ForeachIdentName!=null) ForeachIdentName.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(EndForeach!=null) EndForeach.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignatorForeach!=null) DesignatorForeach.traverseTopDown(visitor);
        if(ForeachLoopStart!=null) ForeachLoopStart.traverseTopDown(visitor);
        if(ForeachIdentName!=null) ForeachIdentName.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(EndForeach!=null) EndForeach.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignatorForeach!=null) DesignatorForeach.traverseBottomUp(visitor);
        if(ForeachLoopStart!=null) ForeachLoopStart.traverseBottomUp(visitor);
        if(ForeachIdentName!=null) ForeachIdentName.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(EndForeach!=null) EndForeach.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Statement_foreach(\n");

        if(DesignatorForeach!=null)
            buffer.append(DesignatorForeach.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForeachLoopStart!=null)
            buffer.append(ForeachLoopStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ForeachIdentName!=null)
            buffer.append(ForeachIdentName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(EndForeach!=null)
            buffer.append(EndForeach.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Statement_foreach]");
        return buffer.toString();
    }
}
