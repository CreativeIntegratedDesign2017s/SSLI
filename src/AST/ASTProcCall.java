package AST;

import java.util.*;
import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTProcCall extends ASTExpr {
    public Token pid;
    public List<ASTExpr> param;

    ASTProcCall(ParserRuleContext ctx, Token pid) {
        super(ctx);
        this.pid = pid;
        this.param = new ArrayList<>();
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitProcCall(this);
    }

    @Override
    public void foreachChild(Consumer<ASTNode> iterFunc) {
        for (ASTNode n : param)
            iterFunc.accept(n);
    }
}
