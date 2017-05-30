import java.util.*;
import org.antlr.v4.runtime.*;

interface ASTUnit extends ASTNode {
    /* ASTUnit Derivations */
    // ASTStmtUnit		Statement Unit
    // ASTProcUnit		Procedure, 'returnType' could be null
}

class ASTStmtUnit implements ASTUnit {
    ASTStmt stmt;

    ASTStmtUnit(ASTStmt stmt) {
        this.stmt = stmt;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitStmtUnit(this);
    }
}

class ASTProcUnit implements ASTUnit {
    static class ParaType {
        Token		var;
        Token		tid;
        Integer		dim;
        Boolean		ref;
    }

    Token returnType;
    Token pid;
    List<ParaType> type = new ArrayList<>();
    List<ASTStmt> stmt = new ArrayList<>();

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitProcUnit(this);
    }
}
