import java.util.*;
import org.antlr.v4.runtime.*;

/* Derivations of ASTExpr
 * ASTTerm: Terminal Expressions
 * ASTUnary: Unary Expressions
 * ASTBinary: Binary Expressions
 * ASTSubscript: Subscript Expression
 * ASTSubstring: Substring Expression
 * ASTProcCall: Procedure Call Expression
 */
public class ASTExpr {
    public void print() {};
}

class ASTTerm extends ASTExpr {
    enum Sort { ID, BOOL, INT, STR }
    Sort sort;
    Token token;
}

class ASTUnary extends ASTExpr {
    Token op;
    ASTExpr oprnd;
}

class ASTBinary extends ASTExpr {
    Token op;
    ASTExpr oprnd1;
    ASTExpr oprnd2;
}

class ASTSubscript extends ASTExpr {
    ASTExpr map;
    ASTExpr index;
}

class ASTSubstring extends ASTExpr {
    ASTExpr str;
    ASTExpr index1;
    ASTExpr index2;
}

class ASTProcCall extends ASTExpr {
    Token id;
    List<ASTExpr> param;
}
