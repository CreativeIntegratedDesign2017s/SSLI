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

        /*
        global.declProc(ctx.ID().getText(),		// Proc ID
                ctx.para_list(),				// Proc parameters
                ctx.rtype().getText());			// Proc return
        current = new LocalTable(ctx.ID().getText(), global);
        */
    }

    @Override
    public void exitProcedure(SimpleParser.ProcedureContext ctx) {
        symTable.print();
        symTable.leaveScope();

        /*
        current.print();
        current = null;
        */
    }

    @Override
    public void enterPara_list(SimpleParser.Para_listContext ctx) {
        for (int i = 0; i < (ctx.getChildCount() + 1) / 3; i++)
        {
            SimpleParser.PtypeContext typeCtx = ctx.ptype(i);
            symTable.declareVariable(ctx.ID(i).getText(),
                    new SymbolTable.ValueExpr(typeCtx.ID().getText(), (typeCtx.getChildCount() - 1) / 3));
            //current.declVar(ctx.ID(i).getText(), ctx.ptype(i));
        }
    }

    @Override
    public void enterEnumerate(SimpleParser.EnumerateContext ctx) {
        String name = ctx.ID().getText();
        SimpleParser.Enum_listContext enumCtx = ctx.enum_list();

        symTable.declareEnum(name, enumCtx.ID().stream().map(TerminalNode::getText)
                                                        .collect(Collectors.toList()));

        //global.declEnum(ctx.ID().getText(), ctx.enum_list());
    }

    @Override
    public void exitDeclare(SimpleParser.DeclareContext ctx) {
        /*
        if (current == null)
            global.declVar(ctx.ID().getText(), ctx.type());
        else
            current.declVar(ctx.ID().getText(), ctx.type());
        */
        SimpleParser.TypeContext typeCtx = ctx.type();
        symTable.declareVariable(ctx.ID().getText(),
                new SymbolTable.ValueExpr(typeCtx.ID().getText(), (typeCtx.getChildCount() - 1) / 3));
    }

    @Override
    public void enterNested(SimpleParser.NestedContext ctx) {
        symTable.enterNewScope();
        /*
        scpStack.push(current);
        if (current == null)
            current = new LocalTable("Nested", global);
        else
            current = new LocalTable(current);
        */
    }

    @Override
    public void exitNested(SimpleParser.NestedContext ctx) {
        symTable.print();
        symTable.leaveScope();
    }
}
