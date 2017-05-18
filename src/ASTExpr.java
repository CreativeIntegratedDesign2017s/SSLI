import java.util.*;
import org.antlr.v4.runtime.*;

/* Derivations of ASTExpr
 * ASTPrimeExpr: Bool, INT, STR
 * ASTIdentExpr: Identifier
 * ASTUnary: Unary Expressions
 * ASTBinary: Binary Expressions
 * ASTSubscript: Subscript Expression
 * ASTSubstring: Substring Expression
 * ASTProcCall: Procedure Call Expression
 */
interface ASTExpr extends ASTNode { }

class ASTIdentExpr implements ASTExpr {
    Token token;

    ASTIdentExpr(Token token) { this.token = token; }

    public <T> T visit(ASTListener<T> al) { return al.visitIdentExpr(this); }
}

class ASTPrimeExpr implements ASTExpr {
    Token token;

    ASTPrimeExpr(Token token) { this.token = token; }

    public <T> T visit(ASTListener<T> al) { return al.visitPrimeExpr(this); }
}

class ASTUnary implements ASTExpr {
    Token op;
    ASTExpr oprnd;

    ASTUnary(Token op, ASTExpr oprnd) {
        this.op = op;
        this.oprnd = oprnd;
    }

    public <T> T visit(ASTListener<T> al) { return al.visitUnary(this); }
}

class ASTBinary implements ASTExpr {
    Token op;
    ASTExpr oprnd1;
    ASTExpr oprnd2;

    ASTBinary(Token op, ASTExpr oprnd1, ASTExpr oprnd2) {
        this.op = op;
        this.oprnd1 = oprnd1;
        this.oprnd2 = oprnd2;
    }

    public <T> T visit(ASTListener<T> al) { return al.visitBinary(this); }
}

class ASTSubscript implements ASTExpr {
    ASTExpr map;
    ASTExpr index;

    ASTSubscript(ASTExpr map, ASTExpr index) {
        this.map = map;
        this.index = index;
    }

    public <T> T visit(ASTListener<T> al) { return al.visitSubscript(this); }
}

class ASTSubstring implements ASTExpr {
    ASTExpr str;
    ASTExpr index1;
    ASTExpr index2;

    ASTSubstring(ASTExpr str, ASTExpr index1, ASTExpr index2) {
        this.str = str;
        this.index1 = index1;
        this.index2 = index2;
    }

    public <T> T visit(ASTListener<T> al) { return al.visitSubstring(this); }
}

class ASTProcCall implements ASTExpr {
    Token id;
    List<ASTExpr> param;

    ASTProcCall(Token id) {
        this.id = id;
        this.param = new ArrayList<>();
    }

    public <T> T visit(ASTListener<T> al) { return al.visitProcCall(this); }
}
