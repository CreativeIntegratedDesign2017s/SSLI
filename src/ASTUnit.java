import java.util.*;
import org.antlr.v4.runtime.*;

/* Derivations of ASTUnit
 * ASTStmtUnit: Statement Unit
 * ASTProcUnit: Definition of Procedure
 * ASTImportUnit: import "blah blah blah"
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
    class Param {
        Token type;
        boolean reference;
        int dimension;
        Token id;
    }

    Token procName;
    Token returnType;
    List<Param> param;
    List<ASTStmt> stmt;
}
