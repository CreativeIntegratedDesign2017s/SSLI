package AST;

import java.util.*;
import java.util.function.*;
import org.antlr.v4.runtime.*;

public class ASTProcUnit extends ASTUnit {

    ASTProcUnit(ParserRuleContext ctx) {
        super(ctx);
    }

    public static class ParaType {
        public Token		var;
        public Token		tid;
        public Integer		dim;
        public Boolean		ref;
    }

    public Token returnType;
    public Token pid;
    public List<ParaType> type = new ArrayList<>();
    public ASTStmtList stmtList;

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitProcUnit(this);
    }

    @Override public
    void foreachChild(Consumer<ASTNode> iterFunc) {
        iterFunc.accept(stmtList);
    }
}
