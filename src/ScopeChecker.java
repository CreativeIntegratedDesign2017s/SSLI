import AST.*;

import java.util.ArrayList;
import java.util.List;

public class ScopeChecker extends ASTListener<Void> {
    private SymbolTable symTable;

    ScopeChecker(SymbolTable symTable) {
        this.symTable = symTable;
    }

    @Override
    public Void visitDecl(ASTDecl ctx) {
        SymbolTable.VarSymbol v = symTable.declareVariable(ctx.var.getText(),
                new SymbolTable.ValueExpr(ctx.type.tid.getText(), ctx.type.size));
        symTable.putSymbol(ctx, v);
        return visitChildren(ctx);
    }

    @Override
    public Void visitVariable(ASTVariable ctx) {
        SymbolTable.Symbol s = symTable.getSymbol(ctx.token.getText());
        if (s == null)
            throw new RuleException(ctx, "No symbol has found with " + ctx.token.getText());
        symTable.putSymbol(ctx, s);
        return visitChildren(ctx);
    }

    @Override
    public Void visitProcUnit(ASTProcUnit ctx) {
        String rtypeString = ctx.returnType == null ? "void" : ctx.returnType.getText();
        SymbolTable.ValueExpr rType = new SymbolTable.ValueExpr(rtypeString);

        List<SymbolTable.ParameterExpr> paramTypes = new ArrayList<>();
        for (ASTProcUnit.ParaType ptype : ctx.type) {
            paramTypes.add(new SymbolTable.ParameterExpr(ptype.tid.getText(), ptype.dim, ptype.ref));
        }

        Function fDecl = symTable.declareFunction(ctx.pid.getText(), rType, paramTypes, false);
        symTable.putFunction(ctx, fDecl);
        symTable.enterNewScope();

        for (ASTProcUnit.ParaType ptype : ctx.type) {
            SymbolTable.VarSymbol v = symTable.declareVariable(ptype.var.getText(),
                    new SymbolTable.ParameterExpr(ptype.tid.getText(), ptype.dim, ptype.ref));
            symTable.putSymbol(ptype, v);
        }

        Void ret = visitChildren(ctx);

        symTable.leaveScope();
        return ret;
    }

    @Override
    public Void visitStmtList(ASTStmtList ctx) {
        symTable.enterNewScope();

        Void ret = visitChildren(ctx);

        symTable.leaveScope();
        return ret;
    }
}
