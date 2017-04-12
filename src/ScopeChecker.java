import java.util.*;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.tree.*;

public class ScopeChecker extends SimpleParserBaseListener {

    /* Global table has global variables, types and procs.
     * Local tables have local variables for its proc or a block.
     * And each context has its scope. If it is not designated,
     * then identifiers that used in that context should be found
     * in global scope.
     */
    SymbolTable symTable;

    /* Constructor */
    ScopeChecker(SymbolTable _symTable) {
        symTable = _symTable;
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
        symTable.declareProcedure(ctx.ID().getText(), rType, paramTypes);
        symTable.enterNewScope();
    }

    @Override
    public void exitProcedure(SimpleParser.ProcedureContext ctx) {
        symTable.print();
        symTable.leaveScope();
    }

    @Override
    public void enterPara_list(SimpleParser.Para_listContext ctx) {
        for (int i = 0; i < (ctx.getChildCount() + 1) / 3; i++)
        {
            SimpleParser.PtypeContext typeCtx = ctx.ptype(i);
            symTable.declareVariable(ctx.ID(i).getText(),
                    new SymbolTable.ValueExpr(typeCtx.ID().getText(), (typeCtx.getChildCount() - 1) / 3));
        }
    }

    @Override
    public void exitDeclare(SimpleParser.DeclareContext ctx) {
        SimpleParser.TypeContext typeCtx = ctx.type();
        symTable.declareVariable(ctx.ID().getText(),
                new SymbolTable.ValueExpr(typeCtx.ID().getText(), (typeCtx.getChildCount() - 1) / 3));
    }

    @Override
    public void enterStmt_list(SimpleParser.Stmt_listContext ctx) {
        symTable.enterNewScope();
    }

    @Override
    public void exitStmt_list(SimpleParser.Stmt_listContext ctx) {
        symTable.print();
        symTable.leaveScope();
    }
}
