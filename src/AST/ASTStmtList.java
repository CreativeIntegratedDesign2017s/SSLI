package AST;

import java.util.*;
import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTStmtList extends ASTNode {
    ASTStmtList(ParserRuleContext ctx) {
        super(ctx);
    }

    public List<ASTStmt> list = new ArrayList<>();

    @Override public <Type>
    Type visit(ASTListener<Type> listener) {
        return listener.visitStmtList(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        for(ASTNode n : list) iterFunc.accept(n);
    }
}
