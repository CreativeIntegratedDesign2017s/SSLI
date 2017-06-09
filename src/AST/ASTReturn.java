package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTReturn extends ASTStmt {
    public ASTExpr val;

    ASTReturn(ParserRuleContext ctx, ASTExpr val) {
        super(ctx);
        this.val = val;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitReturn(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        if (val != null)
            iterFunc.accept(val);
    }
}
