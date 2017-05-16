import java.util.*;
import org.antlr.v4.runtime.*;

/* Derivations of ASTUnit
 * ASTStmtUnit: Statement Unit
 * ASTImportUnit: import "blah blah blah"
 * ASTProcUnit: Definition of Procedure
 */
public class ASTUnit implements ASTNode { }

class ASTStmtUnit extends ASTUnit {
    ASTStmt stmt;
    ASTStmtUnit(ASTStmt stmt) { this.stmt = stmt; }
    public String toString() { return stmt.toString(); }
}

class ASTImportUnit extends ASTUnit {
    Token file;
    ASTImportUnit(Token file) { this.file = file; }
    public String toString() { return "import " + file.getText(); }
}

class ASTProcUnit extends ASTUnit {
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
    public String toString() {
        return "'toString()' for proc unit is not developed yet.";
    }
}
