package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTNested extends ASTStmt {
    ASTNested(ParserRuleContext ctx) {
        super(ctx);
    }

    public ASTStmtList stmtList;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitNested(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(stmtList);
    }
}
