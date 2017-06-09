package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTUntil extends ASTStmt {
    ASTUntil(ParserRuleContext ctx) {
        super(ctx);
    }

    public ASTExpr cond;
    public ASTStmtList loop;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitUntil(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(cond);
        iterFunc.accept(loop);
    }
}
