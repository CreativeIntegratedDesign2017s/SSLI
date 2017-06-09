package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTAsgn extends ASTStmt {
    public ASTExpr lval;
    public ASTExpr rval;

    ASTAsgn(ParserRuleContext ctx, ASTExpr lval, ASTExpr rval) {
        super(ctx);
        this.lval = lval;
        this.rval = rval;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitAsgn(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(lval);
        iterFunc.accept(rval);
    }
}
