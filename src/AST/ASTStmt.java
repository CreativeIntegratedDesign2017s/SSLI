package AST;

import org.antlr.v4.runtime.*;

public abstract class ASTStmt extends ASTNode {
    public ASTStmt(ParserRuleContext ctx) {
        super(ctx);
    }
    /* AST.ASTStmt Derivations */
    // AST.ASTEval				Expression, 'expr' could be null
    // AST.ASTDecl				Declaration, 'init' could be null
    // AST.ASTAsgn				Assignment
    // AST.ASTCond				If-Then-Else
    // AST.ASTUntil				Do-While Loop
    // AST.ASTWhile				While-Do Loop
    // AST.ASTReturn			Return Statement, 'val' could be null
    // AST.ASTNested			Nested Statement
}
