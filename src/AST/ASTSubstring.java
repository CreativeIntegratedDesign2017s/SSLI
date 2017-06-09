package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTSubstring extends ASTExpr {
    public ASTExpr str;
    public ASTExpr index1;
    public ASTExpr index2;

    ASTSubstring(ParserRuleContext ctx, ASTExpr str, ASTExpr index1, ASTExpr index2) {
        super(ctx);
        this.str = str;
        this.index1 = index1;
        this.index2 = index2;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitSubstring(this);
    }

    @Override
    public void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(str);
        iterFunc.accept(index1);
        iterFunc.accept(index2);
    }
}
