// generated with ast extension for cup
// version 0.8
// 12/0/2023 13:2:2


package rs.ac.bg.etf.pp1.ast;

public class Statement_while extends Statement {

    private WhileLoopStart WhileLoopStart;
    private WhileCondition WhileCondition;
    private Statement Statement;
    private WhileEnd WhileEnd;

    public Statement_while (WhileLoopStart WhileLoopStart, WhileCondition WhileCondition, Statement Statement, WhileEnd WhileEnd) {
        this.WhileLoopStart=WhileLoopStart;
        if(WhileLoopStart!=null) WhileLoopStart.setParent(this);
        this.WhileCondition=WhileCondition;
        if(WhileCondition!=null) WhileCondition.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.WhileEnd=WhileEnd;
        if(WhileEnd!=null) WhileEnd.setParent(this);
    }

    public WhileLoopStart getWhileLoopStart() {
        return WhileLoopStart;
    }

    public void setWhileLoopStart(WhileLoopStart WhileLoopStart) {
        this.WhileLoopStart=WhileLoopStart;
    }

    public WhileCondition getWhileCondition() {
        return WhileCondition;
    }

    public void setWhileCondition(WhileCondition WhileCondition) {
        this.WhileCondition=WhileCondition;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public WhileEnd getWhileEnd() {
        return WhileEnd;
    }

    public void setWhileEnd(WhileEnd WhileEnd) {
        this.WhileEnd=WhileEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(WhileLoopStart!=null) WhileLoopStart.accept(visitor);
        if(WhileCondition!=null) WhileCondition.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(WhileEnd!=null) WhileEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(WhileLoopStart!=null) WhileLoopStart.traverseTopDown(visitor);
        if(WhileCondition!=null) WhileCondition.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(WhileEnd!=null) WhileEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(WhileLoopStart!=null) WhileLoopStart.traverseBottomUp(visitor);
        if(WhileCondition!=null) WhileCondition.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(WhileEnd!=null) WhileEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Statement_while(\n");

        if(WhileLoopStart!=null)
            buffer.append(WhileLoopStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileCondition!=null)
            buffer.append(WhileCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(WhileEnd!=null)
            buffer.append(WhileEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Statement_while]");
        return buffer.toString();
    }
}
