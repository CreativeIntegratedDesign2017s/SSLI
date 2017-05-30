import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;
import java.util.List;

public class ASTStmtList extends ASTNode {
    ASTStmtList(ParserRuleContext ctx) {
        super(ctx);
    }

    List<ASTStmt> list = new ArrayList<>();

    @Override
    public <Type>
    Type visit(ASTListener<Type> listener) {
        return listener.visitStmtList(this);
    }

    @Override public
    void foreachChild(java.util.function.Function<ASTNode, Void> iterFunc) {
        for(ASTNode n : list) {
            iterFunc.apply(n);
        }
    }
}
