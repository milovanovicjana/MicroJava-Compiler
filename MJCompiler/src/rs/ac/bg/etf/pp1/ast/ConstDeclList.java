// generated with ast extension for cup
// version 0.8
// 12/0/2023 13:2:2


package rs.ac.bg.etf.pp1.ast;

public class ConstDeclList implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ConstDeclListType ConstDeclListType;
    private ConDecl ConDecl;
    private ConDeclMore ConDeclMore;

    public ConstDeclList (ConstDeclListType ConstDeclListType, ConDecl ConDecl, ConDeclMore ConDeclMore) {
        this.ConstDeclListType=ConstDeclListType;
        if(ConstDeclListType!=null) ConstDeclListType.setParent(this);
        this.ConDecl=ConDecl;
        if(ConDecl!=null) ConDecl.setParent(this);
        this.ConDeclMore=ConDeclMore;
        if(ConDeclMore!=null) ConDeclMore.setParent(this);
    }

    public ConstDeclListType getConstDeclListType() {
        return ConstDeclListType;
    }

    public void setConstDeclListType(ConstDeclListType ConstDeclListType) {
        this.ConstDeclListType=ConstDeclListType;
    }

    public ConDecl getConDecl() {
        return ConDecl;
    }

    public void setConDecl(ConDecl ConDecl) {
        this.ConDecl=ConDecl;
    }

    public ConDeclMore getConDeclMore() {
        return ConDeclMore;
    }

    public void setConDeclMore(ConDeclMore ConDeclMore) {
        this.ConDeclMore=ConDeclMore;
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
        if(ConstDeclListType!=null) ConstDeclListType.accept(visitor);
        if(ConDecl!=null) ConDecl.accept(visitor);
        if(ConDeclMore!=null) ConDeclMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclListType!=null) ConstDeclListType.traverseTopDown(visitor);
        if(ConDecl!=null) ConDecl.traverseTopDown(visitor);
        if(ConDeclMore!=null) ConDeclMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclListType!=null) ConstDeclListType.traverseBottomUp(visitor);
        if(ConDecl!=null) ConDecl.traverseBottomUp(visitor);
        if(ConDeclMore!=null) ConDeclMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDeclList(\n");

        if(ConstDeclListType!=null)
            buffer.append(ConstDeclListType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConDecl!=null)
            buffer.append(ConDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConDeclMore!=null)
            buffer.append(ConDeclMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDeclList]");
        return buffer.toString();
    }
}
