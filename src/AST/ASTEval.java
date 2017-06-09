package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTEval extends ASTStmt {
    public ASTExpr expr;

    ASTEval(ParserRuleContext ctx, ASTExpr expr) {
        super(ctx);
        this.expr = expr;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitEval(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(expr);
    }
}
