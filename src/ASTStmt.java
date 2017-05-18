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
interface ASTStmt extends ASTNode { }

class ASTExprStmt implements ASTStmt {
    ASTExpr expr;
    ASTExprStmt(ASTExpr expr) { this.expr = expr; }
    public <T> T visit(ASTListener<T> al) { return al.visitExprStmt(this);}
}

class ASTDeclStmt implements ASTStmt {
    Token id;
    Token tid;
    List<Integer> sizes = new ArrayList<>();
    ASTExpr init;

    public <T> T visit(ASTListener<T> al) { return al.visitDeclStmt(this); }
}

class ASTAsgnStmt implements ASTStmt {
    ASTExpr lval;
    ASTExpr rval;

    ASTAsgnStmt(ASTExpr lval, ASTExpr rval) {
        this.lval = lval;
        this.rval = rval;
    }

    public <T> T visit(ASTListener<T> al) { return al.visitAsgnStmt(this); }
}

class ASTIfElse implements ASTStmt {
    ASTExpr cond;
    List<ASTStmt> thenStmt;
    List<ASTStmt> elseStmt;

    ASTIfElse(ASTExpr cond) {
        this.cond = cond;
        thenStmt = new ArrayList<>();
        elseStmt = new ArrayList<>();
    }

    public <T> T visit(ASTListener<T> al) { return al.visitIfElse(this); }
}

class ASTDoWhile implements ASTStmt {
    ASTExpr cond;
    List<ASTStmt> stmt;

    ASTDoWhile(ASTExpr cond) {
        this.cond = cond;
        stmt = new ArrayList<>();
    }

    public <T> T visit(ASTListener<T> al) { return al.visitDoWhile(this); }
}

class ASTWhileDo implements ASTStmt {
    ASTExpr cond;
    List<ASTStmt> stmt;

    ASTWhileDo(ASTExpr cond) {
        this.cond = cond;
        stmt = new ArrayList<>();
    }

    public <T> T visit(ASTListener<T> al) { return al.visitWhileDo(this); }
}

class ASTReturn implements ASTStmt {
    ASTExpr expr;
    ASTReturn(ASTExpr expr) { this.expr = expr; }
    public <T> T visit(ASTListener<T> al) { return al.visitReturn(this); }
}

class ASTNested implements ASTStmt {
    List<ASTStmt> stmt;

    ASTNested() { stmt = new ArrayList<>(); }

    public <T> T visit(ASTListener<T> al) { return al.visitNested(this); }
}
