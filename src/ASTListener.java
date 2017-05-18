import java.util.*;

public class ASTListener<T> {
    public T visit(ASTPrgm prgm) {
        for (ASTUnit unit : prgm.units)
            unit.visit(this);
        return null;
    }

    public T visitPrimeExpr(ASTPrimeExpr node) { return null; }
    public T visitIdentExpr(ASTIdentExpr node) { return null; }
    public T visitUnary(ASTUnary node) { return null; }
    public T visitBinary(ASTBinary node) { return null; }
    public T visitSubscript(ASTSubscript node) { return null; }
    public T visitSubstring(ASTSubstring node) { return null; }
    public T visitProcCall(ASTProcCall node) { return null; }

    public T visitExprStmt(ASTExprStmt node) { return null; }
    public T visitDeclStmt(ASTDeclStmt node) { return null; }
    public T visitAsgnStmt(ASTAsgnStmt node) { return null; }
    public T visitIfElse(ASTIfElse node) { return null; }
    public T visitDoWhile(ASTDoWhile node) { return null; }
    public T visitWhileDo(ASTWhileDo node) { return null; }
    public T visitReturn(ASTReturn node) { return null; }
    public T visitNested(ASTNested node) { return null; }

    public T visitStmtUnit(ASTStmtUnit node) { return null; }
    public T visitProcUnit(ASTProcUnit node) { return null; }
    public T visitImportUnit(ASTImportUnit node) { return null; }
}

class ASTtoSTR extends ASTListener<String> {

    public String visit(ASTPrgm prgm) {
        String code = "";
        for (ASTUnit unit : prgm.units)
            code += unit.visit(this);
        return code;
    }

    public String visitPrimeExpr(ASTPrimeExpr node) {
        return node.token.getText();
    }
    public String visitIdentExpr(ASTIdentExpr node) {
        return node.token.getText();
    }
    public String visitUnary(ASTUnary node) {
        return "(" + node.op.getText() + " " + node.oprnd.visit(this) + ")";
    }
    public String visitBinary(ASTBinary node) {
        return "(" + node.op.getText() + " " + node.oprnd1.visit(this) + " " + node.oprnd2.visit(this) + ")";
    }
    public String visitSubscript(ASTSubscript node) {
        return node.map.visit(this) + "[" + node.index.visit(this) + "]";
    }
    public String visitSubstring(ASTSubstring node) {
        return node.str.visit(this) + "[" + node.index1.visit(this) + ":" + node.index2.visit(this) + "]";
    }
    public String visitProcCall(ASTProcCall node) {
        String str = "(" + node.id.getText();
        for (ASTExpr p : node.param)
            str += " " + p.visit(this);
        return str + ")";
    }

    public String visitExprStmt(ASTExprStmt node) {
        return node.expr.visit(this);
    }
    public String visitDeclStmt(ASTDeclStmt node) {
        String str = "(decl " + node.id.getText() + ":" + node.tid.getText();
        for (Integer i : node.sizes)
            str += "[" + i.toString() + "]";
        if (node.init != null)
            str += " " + node.init.visit(this);
        str += ")";
        return str;
    }
    public String visitAsgnStmt(ASTAsgnStmt node) {
        return "(asgn " + node.lval.visit(this) + " " + node.visit(this) + ")";
    }
    public String visitIfElse(ASTIfElse node) {
        String code = "(cond " + node.cond.visit(this) + " {";
        for (ASTStmt stmt : node.thenStmt)
            code += " " + stmt.visit(this);
        code += " } {";
        for (ASTStmt stmt : node.elseStmt)
            code += " " + stmt.visit(this);
        code += " })";
        return code;
    }
    public String visitDoWhile(ASTDoWhile node) {
        String code = "(until " + node.cond.visit(this) + " {";
        for (ASTStmt st : node.stmt)
            code += " " + st.visit(this);
        code += " })";
        return code;
    }
    public String visitWhileDo(ASTWhileDo node) {
        String code = "(while " + node.cond.visit(this) + " {";
        for (ASTStmt st : node.stmt)
            code += " " + st.visit(this);
        code += " })";
        return code;
    }
    public String visitReturn(ASTReturn node) {
        return "(return " + node.expr.visit(this) + ")";
    }
    public String visitNested(ASTNested node) {
        String code = "{";
        for (ASTStmt st : node.stmt)
            code += " " + st.visit(this);
        code += " }";
        return code;
    }

    public String visitStmtUnit(ASTStmtUnit node) { return node.stmt.visit(this); }
    public String visitProcUnit(ASTProcUnit node) { return "proc " + node.procName.getText(); }
    public String visitImportUnit(ASTImportUnit node) { return "import " + node.file.getText(); }
}