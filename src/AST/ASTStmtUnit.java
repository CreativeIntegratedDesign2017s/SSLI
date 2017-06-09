package AST;

import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTStmtUnit extends ASTUnit {
    ASTStmt stmt;

    ASTStmtUnit(ParserRuleContext ctx, ASTStmt stmt) {
        super(ctx);
        this.stmt = stmt;
    }

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitStmtUnit(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(stmt);
    }
}
