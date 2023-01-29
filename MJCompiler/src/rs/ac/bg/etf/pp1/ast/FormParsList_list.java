// generated with ast extension for cup
// version 0.8
// 12/0/2023 13:2:2


package rs.ac.bg.etf.pp1.ast;

public class FormParsList_list extends FormParsList {

    private FormParam FormParam;
    private FormParsMore FormParsMore;

    public FormParsList_list (FormParam FormParam, FormParsMore FormParsMore) {
        this.FormParam=FormParam;
        if(FormParam!=null) FormParam.setParent(this);
        this.FormParsMore=FormParsMore;
        if(FormParsMore!=null) FormParsMore.setParent(this);
    }

    public FormParam getFormParam() {
        return FormParam;
    }

    public void setFormParam(FormParam FormParam) {
        this.FormParam=FormParam;
    }

    public FormParsMore getFormParsMore() {
        return FormParsMore;
    }

    public void setFormParsMore(FormParsMore FormParsMore) {
        this.FormParsMore=FormParsMore;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(FormParam!=null) FormParam.accept(visitor);
        if(FormParsMore!=null) FormParsMore.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FormParam!=null) FormParam.traverseTopDown(visitor);
        if(FormParsMore!=null) FormParsMore.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FormParam!=null) FormParam.traverseBottomUp(visitor);
        if(FormParsMore!=null) FormParsMore.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormParsList_list(\n");

        if(FormParam!=null)
            buffer.append(FormParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FormParsMore!=null)
            buffer.append(FormParsMore.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormParsList_list]");
        return buffer.toString();
    }
}
