package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTUnary extends ASTExpr {
    public Token op;
    public ASTExpr oprnd;

    ASTUnary(ParserRuleContext ctx, Token op, ASTExpr oprnd) {
        super(ctx);
        this.op = op;
        this.oprnd = oprnd;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitUnary(this);
    }

    @Override
    public void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(oprnd);
    }
}
