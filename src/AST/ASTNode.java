package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public abstract class ASTNode {
    private String raw;
    private int line;

    public ASTNode(ParserRuleContext ctx) {
        raw = ctx.getText();
        line = ctx.getStart().getLine();
    }

    public String getText() { return raw; }
    public int getLine() { return line; }

    public abstract <Type> Type visit(ASTListener<Type> al);
    public abstract void foreachChild(Consumer<ASTNode> iterFunc);
}
