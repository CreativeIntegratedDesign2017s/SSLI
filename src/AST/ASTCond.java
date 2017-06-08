package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTCond extends ASTStmt {
    ASTCond(ParserRuleContext ctx) {
        super(ctx);
    }

    public ASTExpr cond;
    public ASTStmtList thenStmtList;
    public ASTStmtList elseStmtList;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitCond(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(cond);
        iterFunc.accept(thenStmtList);
        if (elseStmtList != null)
            iterFunc.accept(elseStmtList);
    }
}
