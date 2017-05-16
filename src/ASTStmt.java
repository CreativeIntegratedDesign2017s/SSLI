import java.util.*;
import org.antlr.v4.runtime.*;

/* Derivations of ASTStmt
 * ASTExprStmt: ProcCall Statement
 * ASTDeclStmt: Declaration Statement
 * ASTAsgnStmt: Assignment Statement
 * ASTIfElse: If-Else Statement
 * ASTDoWhile: Do-While Statement
 * ASTWhileDo: While-Do Statement
 * ASTReturn: Return Statement
 * ASTNested: Nested Statement
 */
public class ASTStmt implements ASTNode { }

class ASTExprStmt extends ASTStmt {
    ASTExpr expr;
    ASTExprStmt(ASTExpr expr) { this.expr = expr; }
    public String toString() { return expr.toString(); }
}

class ASTDeclStmt extends ASTStmt {
    Token id;
    Token tid;
    List<Integer> size = new ArrayList<>();
    ASTExpr init;

    public String toString() {
        String str = "(decl " + id.getText() + ":" + tid.getText();
        System.out.println(size.size());
        for (Integer i : size)
            str += "[" + i.toString() + "]";
        if (init != null)
            str += " " + init.toString();
        str += ")";
        return str;
    }
}

class ASTAsgnStmt extends ASTStmt {
    ASTExpr lval;
    ASTExpr rval;

    ASTAsgnStmt(ASTExpr lval, ASTExpr rval) {
        this.lval = lval;
        this.rval = rval;
    }

    public String toString() {
        return "(asgn " + lval.toString() + " " + rval.toString() + ")";
    }
}

class ASTIfElse extends ASTStmt {
    ASTExpr cond;
    List<ASTStmt> thenStmt;
    List<ASTStmt> elseStmt;

    ASTIfElse(ASTExpr cond) {
        this.cond = cond;
        thenStmt = new ArrayList<>();
        elseStmt = new ArrayList<>();
    }

    public String toString() {
        String code = "(cond " + cond.toString() + " {";
        for (ASTStmt stmt : thenStmt)
            code += " " + stmt.toString();
        code += " } {";
        for (ASTStmt stmt : elseStmt)
            code += " " + stmt.toString();
        code += " })";
        return code;
    }
}

class ASTDoWhile extends ASTStmt {
    ASTExpr cond;
    List<ASTStmt> stmt;

    ASTDoWhile(ASTExpr cond) {
        this.cond = cond;
        stmt = new ArrayList<>();
    }

    public String toString() {
        String code = "(until " + cond.toString() + " {";
        for (ASTStmt st : stmt)
            code += " " + st.toString();
        code += " })";
        return code;
    }
}

class ASTWhileDo extends ASTStmt {
    ASTExpr cond;
    List<ASTStmt> stmt;

    ASTWhileDo(ASTExpr cond) {
        this.cond = cond;
        stmt = new ArrayList<>();
    }

    public String toString() {
        String code = "(while " + cond.toString() + " {";
        for (ASTStmt st : stmt)
            code += " " + st.toString();
        code += " })";
        return code;
    }
}

class ASTReturn extends ASTStmt {
    ASTExpr expr;
    ASTReturn(ASTExpr expr) { this.expr = expr; }
    public String toString() { return "(return " + expr.toString() + ")"; }
}

class ASTNested extends ASTStmt {
    List<ASTStmt> stmt;

    ASTNested() { stmt = new ArrayList<>(); }

    public String toString() {
        String code = "{";
        for (ASTStmt st : stmt)
            code += " " + st.toString();
        code += " }";
        return code;
    }
}
