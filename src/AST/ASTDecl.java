package AST;

import java.util.*;
import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTDecl extends ASTStmt {
    ASTDecl(ParserRuleContext ctx) {
        super(ctx);
    }

    public static class DeclType {
        public Token tid;
        public List<Integer> size = new ArrayList<>();
    }

    public DeclType type = new DeclType();
    public Token var;
    public ASTExpr init;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitDecl(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        if (init != null)
            iterFunc.accept(init);
    }
}
