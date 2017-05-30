import java.util.*;
import org.antlr.v4.runtime.*;

interface ASTStmt extends ASTNode {
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

class ASTEval implements ASTStmt {
    ASTExpr expr;

    ASTEval(ASTExpr expr) {
        this.expr = expr;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitEval(this);
    }
}

class ASTDecl implements ASTStmt {
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
}

class ASTAsgn implements ASTStmt {
    ASTExpr lval;
    ASTExpr rval;

    ASTAsgn(ASTExpr lval, ASTExpr rval) {
        this.lval = lval;
        this.rval = rval;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitAsgn(this);
    }
}

class ASTCond implements ASTStmt {
    ASTExpr cond;
    List<ASTStmt> thenStmt = new ArrayList<>();
    List<ASTStmt> elseStmt = new ArrayList<>();

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitCond(this);
    }
}

class ASTUntil implements ASTStmt {
    ASTExpr cond;
    List<ASTStmt> loop = new ArrayList<>();

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitUntil(this);
    }
}

class ASTWhile implements ASTStmt {
    ASTExpr cond;
    List<ASTStmt> loop = new ArrayList<>();

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitWhile(this);
    }
}

class ASTReturn implements ASTStmt {
    ASTExpr val;

    ASTReturn(ASTExpr val) {
        this.val = val;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitReturn(this);
    }
}

class ASTNested implements ASTStmt {
    List<ASTStmt> stmt = new ArrayList<>();

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitNested(this);
    }
}
