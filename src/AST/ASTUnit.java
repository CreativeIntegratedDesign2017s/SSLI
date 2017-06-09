package AST;

import org.antlr.v4.runtime.*;

public abstract class ASTUnit extends ASTNode {
    ASTUnit(ParserRuleContext ctx) {
        super(ctx);
    }
    /* AST.ASTUnit Derivations */
    // AST.ASTStmtUnit		Statement Unit
    // AST.ASTProcUnit		Procedure, 'returnType' could be null
}
