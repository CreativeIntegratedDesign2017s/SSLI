import java.util.*;
import org.antlr.v4.runtime.*;

abstract class ASTStmt extends ASTNode {
    public ASTStmt(ParserRuleContext ctx) {
        super(ctx);
    }
    /* ASTStmt Derivations */
    // ASTEval				Expression, 'expr' could be null
    // ASTDecl				Declaration, 'init' could be null
    // ASTAsgn				Assignment
    // ASTCond				If-Then-Else
    // ASTUntil				Do-While Loop
    // ASTWhile				While-Do Loop
    // ASTReturn			Return Statement, 'val' could be null
    // ASTNested			Nested Statement
}

class ASTEval extends ASTStmt {
    ASTExpr expr;

    ASTEval(ParserRuleContext ctx, ASTExpr expr) {
        super(ctx);
        this.expr = expr;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitEval(this);
    }
    
    @Override public
    void foreachChild(java.util.function.Function<ASTNode, Void> iterFunc) {
        iterFunc.apply(expr);
    }
}

class ASTDecl extends ASTStmt {
    ASTDecl(ParserRuleContext ctx) {
        super(ctx);
    }
    static class DeclType {
        Token tid;
        List<Integer> size = new ArrayList<>();
    }

    DeclType type = new DeclType();
    Token var;
    ASTExpr init;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitDecl(this);
    }

    @Override public
    void foreachChild(java.util.function.Function<ASTNode, Void> iterFunc) {
        if (init != null)
            iterFunc.apply(init);
    }
}

class ASTAsgn extends ASTStmt {
    ASTExpr lval;
    ASTExpr rval;

    ASTAsgn(ParserRuleContext ctx, ASTExpr lval, ASTExpr rval) {
        super(ctx);
        this.lval = lval;
        this.rval = rval;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitAsgn(this);
    }

    @Override public
    void foreachChild(java.util.function.Function<ASTNode, Void> iterFunc) {
        iterFunc.apply(lval);
        iterFunc.apply(rval);
    }
}

class ASTCond extends ASTStmt {
    ASTCond(ParserRuleContext ctx) {
        super(ctx);
    }
    ASTExpr cond;
    ASTStmtList thenStmtList;
    ASTStmtList elseStmtList;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitCond(this);
    }

    @Override public
    void foreachChild(java.util.function.Function<ASTNode, Void> iterFunc) {
        iterFunc.apply(cond);
        iterFunc.apply(thenStmtList);
        if (elseStmtList != null)
            iterFunc.apply(elseStmtList);
    }
}

class ASTUntil extends ASTStmt {
    ASTUntil(ParserRuleContext ctx) {
        super(ctx);
    }
    ASTExpr cond;
    ASTStmtList loop;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitUntil(this);
    }
    @Override public
    void foreachChild(java.util.function.Function<ASTNode, Void> iterFunc) {
        iterFunc.apply(cond);
        iterFunc.apply(loop);
    }
}

class ASTWhile extends ASTStmt {
    ASTWhile(ParserRuleContext ctx) {
        super(ctx);
    }
    ASTExpr cond;
    ASTStmtList loop;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitWhile(this);
    }
    @Override public
    void foreachChild(java.util.function.Function<ASTNode, Void> iterFunc) {
        iterFunc.apply(cond);
        iterFunc.apply(loop);
    }
}

class ASTReturn extends ASTStmt {
    ASTExpr val;

    ASTReturn(ParserRuleContext ctx, ASTExpr val) {
        super(ctx);
        this.val = val;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitReturn(this);
    }
    @Override public
    void foreachChild(java.util.function.Function<ASTNode, Void> iterFunc) {
        if (val != null)
            iterFunc.apply(val);
    }
}

class ASTNested extends ASTStmt {
    ASTNested(ParserRuleContext ctx) {
        super(ctx);
    }
    ASTStmtList stmtList;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitNested(this);
    }
    @Override public
    void foreachChild(java.util.function.Function<ASTNode, Void> iterFunc) {
        iterFunc.apply(stmtList);
    }
}
