import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

abstract class ASTNode {
    private String raw;
    private int line;
    ASTNode(ParserRuleContext ctx) {
        raw = ctx.getText();
        line = ctx.getStart().getLine();
    }

    String getText() { return raw; }
    int getLine() { return line; }

    abstract <Type> Type visit(ASTListener<Type> al);
    abstract void foreachChild(java.util.function.Consumer<ASTNode> iterFunc);
}

class ASTPrgm extends ASTNode {
    ASTPrgm(ParserRuleContext ctx) {
        super(ctx);
    }

    List<ASTUnit> units = new ArrayList<>();

    @Override
    public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitPrgm(this);
    }

    @Override
    public void foreachChild(java.util.function.Consumer<ASTNode> iterFunc) {
        for (ASTNode n : units)
            iterFunc.accept(n);
    }
}
