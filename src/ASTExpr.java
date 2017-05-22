import java.util.*;
import org.antlr.v4.runtime.*;

interface ASTExpr extends ASTNode {
    /* ASTExpr Derivations */
    // ASTConstant			BOOL, INT, STR
    // ASTVariable			ID
    // ASTUnary				+, -, !
    // ASTBinary			+, -, *, /, ^, ...
    // ASTSubscript			arr[index]
    // ASTSubstring			str[index1 : index2]
    // ASTProcCall			pid(...)
}

class ASTConstant implements ASTExpr {
    Token token;

    ASTConstant(Token token) {
        this.token = token;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitConstant(this);
    }
}

class ASTVariable implements ASTExpr {
    Token token;

    ASTVariable(Token token) {
        this.token = token;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitVariable(this);
    }
}

class ASTUnary implements ASTExpr {
    Token op;
    ASTExpr oprnd;

    ASTUnary(Token op, ASTExpr oprnd) {
        this.op = op;
        this.oprnd = oprnd;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitUnary(this);
    }
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

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitBinary(this);
    }
}

class ASTSubscript implements ASTExpr {
    ASTExpr arr;
    ASTExpr index;

    ASTSubscript(ASTExpr arr, ASTExpr index) {
        this.arr = arr;
        this.index = index;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitSubscript(this);
    }
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

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitSubstring(this);
    }
}

class ASTProcCall implements ASTExpr {
    Token pid;
    List<ASTExpr> param;

    ASTProcCall(Token pid) {
        this.pid = pid;
        this.param = new ArrayList<>();
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitProcCall(this);
    }
}
