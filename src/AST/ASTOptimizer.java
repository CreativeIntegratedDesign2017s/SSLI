package AST;

import ANTLR.*;

public class ASTOptimizer extends ASTListener<ASTNode> {
    public ASTNode visitBinary(ASTBinary node) {
        if (node.op.getType() == SimpleLexer.DIV) {
            if (node.oprnd2.getClass() == ASTConstant.class) {
                ASTConstant opd = (ASTConstant) node.oprnd2;
                if (opd.token.getType() == SimpleLexer.INT) {
                    if (opd.token.getText().equals("0")) {
                        throw new RuntimeException("DIVIDE BY ZERO");
                    }
                }
            }
        }
        return visitChildren(node);
    }
}
