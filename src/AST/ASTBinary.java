package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTBinary extends ASTExpr {
    public Token op;
    public ASTExpr oprnd1;
    public ASTExpr oprnd2;

    ASTBinary(ParserRuleContext ctx, Token op, ASTExpr oprnd1, ASTExpr oprnd2) {
        super(ctx);
        this.op = op;
        this.oprnd1 = oprnd1;
        this.oprnd2 = oprnd2;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitBinary(this);
    }

    @Override
    public void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(oprnd1);
        iterFunc.accept(oprnd2);
    }
}
