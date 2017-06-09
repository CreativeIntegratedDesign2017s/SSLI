package AST;

import org.antlr.v4.runtime.*;

public abstract class ASTExpr extends ASTNode {
    ASTExpr(ParserRuleContext ctx) {
        super(ctx);
    }
    /* AST.ASTExpr Derivations */
    // AST.ASTConstant			BOOL, INT, STR
    // AST.ASTVariable			ID
    // AST.ASTUnary				+, -, !
    // AST.ASTBinary			+, -, *, /, ^, ...
    // AST.ASTSubscript			arr[index]
    // AST.ASTSubstring			str[index1 : index2]
    // AST.ASTProcCall			pid(...)
}
