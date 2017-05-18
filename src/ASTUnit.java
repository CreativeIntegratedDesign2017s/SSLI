import java.util.*;
import org.antlr.v4.runtime.*;

/* Derivations of ASTUnit
 * ASTStmtUnit: Statement Unit
 * ASTImportUnit: import "blah blah blah"
 * ASTProcUnit: Definition of Procedure
 */
interface ASTUnit extends ASTNode { }

class ASTStmtUnit implements ASTUnit {
    ASTStmt stmt;
    ASTStmtUnit(ASTStmt stmt) { this.stmt = stmt; }
    public <T> T visit(ASTListener<T> al) { return al.visitStmtUnit(this); }
}

class ASTProcUnit implements ASTUnit {
    static class ParamType {
        Token id;
        Token tid;
        boolean reference;
        int dimension;
    }

    Token procName;
    Token returnType;
    List<ParamType> paramType = new ArrayList<>();
    List<ASTStmt> stmt = new ArrayList<>();
    public <T> T visit(ASTListener<T> al) { return al.visitProcUnit(this); }
}

class ASTImportUnit implements ASTUnit {
    Token file;
    ASTImportUnit(Token file) { this.file = file; }
    public <T> T visit(ASTListener<T> al) { return al.visitImportUnit(this); }
}
