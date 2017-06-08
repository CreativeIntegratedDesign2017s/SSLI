package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTConstant extends ASTExpr {
    public enum ConstantType {
        Boolean,
        Integer,
        String
    }

    public Token token;
    public ConstantType type;

    ASTConstant(ParserRuleContext ctx, Token token, ConstantType ct) {
        super(ctx);
        this.token = token;
        this.type = ct;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitConstant(this);
    }

    @Override
    public void foreachChild(Consumer<ASTNode> iterFunc) { }
}
