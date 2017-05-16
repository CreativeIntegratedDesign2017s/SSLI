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
public class ASTExpr implements ASTNode { }

class ASTTerm extends ASTExpr {
    enum Sort { ID, BOOL, INT, STR }
    Sort sort;
    Token token;

    ASTTerm(Sort sort, Token token) {
        this.sort = sort;
        this.token = token;
    }

    public String toString() {
        return token.getText();
    }
}

class ASTUnary extends ASTExpr {
    Token op;
    ASTExpr oprnd;

    ASTUnary(Token op, ASTExpr oprnd) {
        this.op = op;
        this.oprnd = oprnd;
    }

    public String toString() {
        return "(" + op.getText() + " " + oprnd.toString() + ")";
    }
}

class ASTBinary extends ASTExpr {
    Token op;
    ASTExpr oprnd1;
    ASTExpr oprnd2;

    ASTBinary(Token op, ASTExpr oprnd1, ASTExpr oprnd2) {
        this.op = op;
        this.oprnd1 = oprnd1;
        this.oprnd2 = oprnd2;
    }

    public String toString() {
        return "(" + op.getText() + " " + oprnd1.toString() + " " + oprnd2.toString() + ")";
    }
}

class ASTSubscript extends ASTExpr {
    ASTExpr map;
    ASTExpr index;

    public String toString() {
        return map.toString() + "[" + index.toString() + "]";
    }
}

class ASTSubstring extends ASTExpr {
    ASTExpr str;
    ASTExpr index1;
    ASTExpr index2;

    public String toString() {
        return str.toString() + "[" + index1.toString() + ":" + index2.toString() + "]";
    }
}

class ASTProcCall extends ASTExpr {
    Token id;
    List<ASTExpr> param;

    public String toString() {
        String str = id.getText();
        for (ASTExpr p : param)
            str += p.toString();
        return str;
    }
}
