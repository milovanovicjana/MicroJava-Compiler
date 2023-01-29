// generated with ast extension for cup
// version 0.8
// 12/0/2023 13:2:2


package rs.ac.bg.etf.pp1.ast;

public class MethodDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private MethodRetAndName MethodRetAndName;
    private FormParsList FormParsList;
    private VarDeclListMethod VarDeclListMethod;
    private StatementList StatementList;

    public MethodDecl (MethodRetAndName MethodRetAndName, FormParsList FormParsList, VarDeclListMethod VarDeclListMethod, StatementList StatementList) {
        this.MethodRetAndName=MethodRetAndName;
        if(MethodRetAndName!=null) MethodRetAndName.setParent(this);
        this.FormParsList=FormParsList;
        if(FormParsList!=null) FormParsList.setParent(this);
        this.VarDeclListMethod=VarDeclListMethod;
        if(VarDeclListMethod!=null) VarDeclListMethod.setParent(this);
        this.StatementList=StatementList;
        if(StatementList!=null) StatementList.setParent(this);
    }

    public MethodRetAndName getMethodRetAndName() {
        return MethodRetAndName;
    }

    public void setMethodRetAndName(MethodRetAndName MethodRetAndName) {
        this.MethodRetAndName=MethodRetAndName;
    }

    public FormParsList getFormParsList() {
        return FormParsList;
    }

    public void setFormParsList(FormParsList FormParsList) {
        this.FormParsList=FormParsList;
    }

    public VarDeclListMethod getVarDeclListMethod() {
        return VarDeclListMethod;
    }

    public void setVarDeclListMethod(VarDeclListMethod VarDeclListMethod) {
        this.VarDeclListMethod=VarDeclListMethod;
    }

    public StatementList getStatementList() {
        return StatementList;
    }

    public void setStatementList(StatementList StatementList) {
        this.StatementList=StatementList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodRetAndName!=null) MethodRetAndName.accept(visitor);
        if(FormParsList!=null) FormParsList.accept(visitor);
        if(VarDeclListMethod!=null) VarDeclListMethod.accept(visitor);
        if(StatementList!=null) StatementList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodRetAndName!=null) MethodRetAndName.traverseTopDown(visitor);
        if(FormParsList!=null) FormParsList.traverseTopDown(visitor);
        if(VarDeclListMethod!=null) VarDeclListMethod.traverseTopDown(visitor);
        if(StatementList!=null) StatementList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodRetAndName!=null) MethodRetAndName.traverseBottomUp(visitor);
        if(FormParsList!=null) FormParsList.traverseBottomUp(visitor);
        if(VarDeclListMethod!=null) VarDeclListMethod.traverseBottomUp(visitor);
        if(StatementList!=null) StatementList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDecl(\n");

        if(MethodRetAndName!=null)
            buffer.append(MethodRetAndName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsList!=null)
            buffer.append(FormParsList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclListMethod!=null)
            buffer.append(VarDeclListMethod.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(StatementList!=null)
            buffer.append(StatementList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDecl]");
        return buffer.toString();
    }
}
