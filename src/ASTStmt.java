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
    Token tid;
    List<Integer> arrSize;
    Token id;
    ASTExpr init;
}

class ASTAsgnStmt extends ASTStmt {
    ASTExpr lval;
    ASTExpr rval;
}

class ASTIfElse extends ASTStmt {
    ASTExpr cond;
    List<ASTStmt> thenStmt;
    List<ASTStmt> elseStmt;
}

class ASTDoWhile extends ASTStmt {
    ASTExpr cond;
    List<ASTStmt> stmt;
}

class ASTWhileDo extends ASTStmt {
    ASTExpr cond;
    List<ASTStmt> stmt;
}

class ASTReturn extends ASTStmt {
    ASTExpr expr;
}

class ASTNested extends ASTStmt {
    List<ASTStmt> stmt;
}
