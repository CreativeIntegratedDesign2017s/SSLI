package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTSubscript extends ASTExpr {
    public ASTExpr arr;
    public ASTExpr index;

    ASTSubscript(ParserRuleContext ctx, ASTExpr arr, ASTExpr index) {
        super(ctx);
        this.arr = arr;
        this.index = index;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitSubscript(this);
    }

    @Override
    public void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(arr);
        iterFunc.accept(index);
    }
}
