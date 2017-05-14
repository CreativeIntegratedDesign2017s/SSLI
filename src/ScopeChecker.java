import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;
import java.util.List;

public class ScopeChecker extends SimpleParserBaseListener {
    private SymbolTable symTable;
    private ParseTreeProperty<SymbolTable.Symbol> symbolHash = new ParseTreeProperty<>();

    ScopeChecker(SymbolTable symTable) {
        this.symTable = symTable;
    }

    @Override
    public void enterDeclare(SimpleParser.DeclareContext ctx) {
        SimpleParser.TypeContext typeCtx = ctx.type();
        SymbolTable.VarSymbol v = symTable.declareVariable(ctx.ID().getText(),
                new SymbolTable.ValueExpr(typeCtx.ID().getText(), (typeCtx.getChildCount() - 1) / 3));
        symTable.putSymbol(ctx, v);
    }

    @Override
    public void enterPara_list(SimpleParser.Para_listContext ctx) {
        for (int i = 0; i < (ctx.getChildCount() + 1) / 3; i++)
        {
            SimpleParser.PtypeContext typeCtx = ctx.ptype(i);
            SymbolTable.VarSymbol v = symTable.declareVariable(ctx.ID(i).getText(),
                    new SymbolTable.ValueExpr(typeCtx.ID().getText(), (typeCtx.getChildCount() - 1) / 2));
            symTable.putSymbol(typeCtx, v);
        }
    }

    @Override
    public void enterIdentifier(SimpleParser.IdentifierContext ctx) {
        SymbolTable.Symbol s = symTable.getSymbol(ctx.ID().getText());
        if (s == null)
            throw new RuleException(ctx,"No symbol has found with " + ctx.getText());
        symTable.putSymbol(ctx, s);
    }

    @Override
    public void enterProcedure(SimpleParser.ProcedureContext ctx) {
        SymbolTable.ValueExpr rType = new SymbolTable.ValueExpr(ctx.rtype().getText(), 0);
        TerminalNode vNode = ctx.para_list().VOID();
        List<SymbolTable.ParameterExpr> paramTypes = new ArrayList<>();
        if (vNode == null) {
            for (SimpleParser.PtypeContext ptype : ctx.para_list().ptype()) {
                String pTypeName = ptype.ID().getText();
                int dim = (ptype.getChildCount() - 1) / 2;
                boolean isRef = ptype.getChildCount() == 2;
                paramTypes.add(new SymbolTable.ParameterExpr(pTypeName, dim, isRef));
            }
        }

        Function fDecl = symTable.declareFunction(ctx.ID().getText(), rType, paramTypes, false);
        symTable.putFunction(ctx, fDecl);
        symTable.enterNewScope();
    }

    @Override
    public void exitProcedure(SimpleParser.ProcedureContext ctx) {
        symTable.leaveScope();
    }

    @Override
    public void enterStmt_list(SimpleParser.Stmt_listContext ctx) {
        symTable.enterNewScope();
    }

    @Override
    public void exitStmt_list(SimpleParser.Stmt_listContext ctx) {
        symTable.leaveScope();
    }
}
