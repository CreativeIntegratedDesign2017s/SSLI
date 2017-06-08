import java.util.*;
import org.antlr.v4.runtime.*;

abstract class ASTExpr extends ASTNode {
    ASTExpr(ParserRuleContext ctx) {
        super(ctx);
    }
    /* ASTExpr Derivations */
    // ASTConstant			BOOL, INT, STR
    // ASTVariable			ID
    // ASTUnary				+, -, !
    // ASTBinary			+, -, *, /, ^, ...
    // ASTSubscript			arr[index]
    // ASTSubstring			str[index1 : index2]
    // ASTProcCall			pid(...)
}

class ASTConstant extends ASTExpr {
    public enum ConstantType {
        Integer,
        Bool,
        String
    }
    Token token;
    ConstantType type;

    ASTConstant(ParserRuleContext ctx, Token token, ConstantType ct) {
        super(ctx);
        this.token = token;
        this.type = ct;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitConstant(this);
    }
    @Override
    public void foreachChild(java.util.function.Consumer<ASTNode> iterFunc) {
    }
}

class ASTVariable extends ASTExpr {
    Token token;

    ASTVariable(ParserRuleContext ctx, Token token) {
        super(ctx);
        this.token = token;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitVariable(this);
    }
    @Override
    public void foreachChild(java.util.function.Consumer<ASTNode> iterFunc) {
    }
}

class ASTUnary extends ASTExpr {
    Token op;
    ASTExpr oprnd;

    ASTUnary(ParserRuleContext ctx, Token op, ASTExpr oprnd) {
        super(ctx);
        this.op = op;
        this.oprnd = oprnd;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitUnary(this);
    }
    @Override
    public void foreachChild(java.util.function.Consumer<ASTNode> iterFunc) {
        iterFunc.accept(oprnd);
    }
}

class ASTBinary extends ASTExpr {
    Token op;
    ASTExpr oprnd1;
    ASTExpr oprnd2;

    ASTBinary(ParserRuleContext ctx, Token op, ASTExpr oprnd1, ASTExpr oprnd2) {
        super(ctx);
        this.op = op;
        this.oprnd1 = oprnd1;
        this.oprnd2 = oprnd2;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitBinary(this);
    }
    @Override
    public void foreachChild(java.util.function.Consumer<ASTNode> iterFunc) {
        iterFunc.accept(oprnd1);
        iterFunc.accept(oprnd2);
    }
}

class ASTSubscript extends ASTExpr {
    ASTExpr arr;
    ASTExpr index;

    ASTSubscript(ParserRuleContext ctx, ASTExpr arr, ASTExpr index) {
        super(ctx);
        this.arr = arr;
        this.index = index;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitSubscript(this);
    }@Override
    public void foreachChild(java.util.function.Consumer<ASTNode> iterFunc) {
        iterFunc.accept(arr);
        iterFunc.accept(index);
    }

}

class ASTSubstring extends ASTExpr {
    ASTExpr str;
    ASTExpr index1;
    ASTExpr index2;

    ASTSubstring(ParserRuleContext ctx, ASTExpr str, ASTExpr index1, ASTExpr index2) {
        super(ctx);
        this.str = str;
        this.index1 = index1;
        this.index2 = index2;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitSubstring(this);
    }
    @Override
    public void foreachChild(java.util.function.Consumer<ASTNode> iterFunc) {
        iterFunc.accept(str);
        iterFunc.accept(index1);
        iterFunc.accept(index2);
    }
}

class ASTProcCall extends ASTExpr {
    Token pid;
    List<ASTExpr> param;

    ASTProcCall(ParserRuleContext ctx, Token pid) {
        super(ctx);
        this.pid = pid;
        this.param = new ArrayList<>();
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitProcCall(this);
    }
    @Override
    public void foreachChild(java.util.function.Consumer<ASTNode> iterFunc) {
        for (ASTNode n : param)
            iterFunc.accept(n);
    }
}