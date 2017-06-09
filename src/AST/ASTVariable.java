package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTVariable extends ASTExpr {
    public Token token;

    ASTVariable(ParserRuleContext ctx, Token token) {
        super(ctx);
        this.token = token;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitVariable(this);
    }

    @Override
    public void foreachChild(Consumer<ASTNode> iterFunc) { }
}
