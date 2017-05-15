import java.util.*;
import org.antlr.v4.runtime.*;

public class ASTUnit {
    public void print() {}
}

class ASTStmtUnit extends ASTUnit {
    ASTStmt stmt;
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

class ASTImportUnit extends ASTUnit {
    Token file;
}
