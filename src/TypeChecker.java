import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class TypeChecker extends SimpleParserBaseVisitor<Integer> {

    SymbolTable symTable;

    /* Constructor */
    TypeChecker(SymbolTable _symTable) {
        symTable = _symTable;
    }

    @Override
    public Integer visitProcedure(SimpleParser.ProcedureContext ctx) {
        //enterProcedure
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

        //visit Children
        Integer result = visitChildren(ctx);

        //exitProcedure
        symTable.print();
        symTable.leaveScope();

        return result;
    }

    @Override
    public Integer visitPara_list(SimpleParser.Para_listContext ctx) {
        for (int i = 0; i < (ctx.getChildCount() + 1) / 3; i++)
        {
            SimpleParser.PtypeContext typeCtx = ctx.ptype(i);
            symTable.declareVariable(ctx.ID(i).getText(),
                    new SymbolTable.ValueExpr(typeCtx.ID().getText(), (typeCtx.getChildCount() - 1) / 3));
        }

        Integer result = visitChildren(ctx);

        return result;
    }

    @Override
    public Integer visitDeclare(SimpleParser.DeclareContext ctx) {

        Integer result = visitChildren(ctx);

        SimpleParser.TypeContext typeCtx = ctx.type();
        symTable.declareVariable(ctx.ID().getText(),
                new SymbolTable.ValueExpr(typeCtx.ID().getText(), (typeCtx.getChildCount() - 1) / 3));

        return result;
    }

    @Override
    public Integer visitStmt_list(SimpleParser.Stmt_listContext ctx) {
        symTable.enterNewScope();

        Integer result = visitChildren(ctx);

        symTable.print();
        symTable.leaveScope();
        return result;
    }
}
