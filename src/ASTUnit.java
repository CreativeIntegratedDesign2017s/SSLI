import java.util.*;
import org.antlr.v4.runtime.*;

abstract class ASTUnit extends ASTNode {
    ASTUnit(ParserRuleContext ctx) {
        super(ctx);
    }
    /* ASTUnit Derivations */
    // ASTStmtUnit		Statement Unit
    // ASTProcUnit		Procedure, 'returnType' could be null
}

class ASTStmtUnit extends ASTUnit {
    ASTStmt stmt;

    ASTStmtUnit(ParserRuleContext ctx, ASTStmt stmt) {
        super(ctx);
        this.stmt = stmt;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitStmtUnit(this);
    }
    @Override public
    void foreachChild(java.util.function.Consumer<ASTNode> iterFunc) {
        iterFunc.accept(stmt);
    }
}

class ASTProcUnit extends ASTUnit {
    ASTProcUnit(ParserRuleContext ctx) {
        super(ctx);
    }
    static class ParaType {
        Token		var;
        Token		tid;
        Integer		dim;
        Boolean		ref;
    }

    Token pid;
    List<ParaType> type = new ArrayList<>();
    ASTStmtList stmtList;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitProcUnit(this);
    }
    @Override public
    void foreachChild(java.util.function.Consumer<ASTNode> iterFunc) {
        iterFunc.accept(stmtList);
    }
}
