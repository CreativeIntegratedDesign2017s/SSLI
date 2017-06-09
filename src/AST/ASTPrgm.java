package AST;

import java.util.*;
import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTPrgm extends ASTNode {
    ASTPrgm(ParserRuleContext ctx) {
        super(ctx);
    }

    List<ASTUnit> units = new ArrayList<>();

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitPrgm(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        for (ASTNode n : units) iterFunc.accept(n);
    }
}
