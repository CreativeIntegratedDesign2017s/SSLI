/**
 * Created by Holim on 2017-03-21.
 */

import java.util.*;
import org.antlr.v4.runtime.tree.*;

public class ScopeChecker extends SimpleParserBaseListener {
    /* Symbol Table & Scope */
    SymbolTable current;
    Stack<SymbolTable> block = new Stack<SymbolTable>();
    public ParseTreeProperty<SymbolTable> scope = new ParseTreeProperty<SymbolTable>();

    /* Constructor */
    public ScopeChecker(SymbolTable symbols) {
        this.current = symbols;
    }

    /* Error Reporting */
    private void error(String msg) {
        System.err.println(msg);
        throw new RuntimeException();
    }

    @Override
    public void enterProcedure(SimpleParser.ProcedureContext ctx) {
        String rtype = ctx.rtype().getText();
        if (!rtype.equals("void") && !current.checkTypeDeclared(rtype))
            error("Undeclared type '" + rtype + "'");

        String ptype;
        int count = (ctx.para_list().getChildCount() + 1) / 3;
        if (count != 0) {
            ptype = ctx.para_list().ptype(0).getText();
            if (!current.checkTypeDeclared(ptype))
                error("Undeclared type '" + ptype + "'");
            for (int i = 1; i < count; i++) {
                String temp = ctx.para_list().ptype(i).getText();
                if (!current.checkTypeDeclared(temp))
                    error("Undeclared type '" + temp + "'");
                ptype += "," + temp;
            }
        }
        else { ptype = "void"; }

        String id = ctx.ID().getText();
        String type = ptype + "->" + rtype;
        if (current.checkProcRedundant(id, type))
            error("'" + id + "' is already defined.");

        current = new SymbolTable(SymbolTable.procDecl, id, type, current);
        block.push(current);

        for (int i = 0; i < count; i++) {
            String pid = ctx.para_list().ID(i).getText();
            if (current.checkVarRedundant(pid, block.peek()))
                error("'" + pid + "' is already defined.");

            current = new SymbolTable(SymbolTable.varDecl,
                    pid, ctx.para_list().ptype(i).getText(),
                    current);
        }
    }

    @Override
    public void exitProcedure(SimpleParser.ProcedureContext ctx) { current = block.pop(); }

    @Override
    public void enterEnumerate(SimpleParser.EnumerateContext ctx) {
        String id = ctx.ID().getText();
        if (current.checkTypeRedundant(id))
            error("'" + id + "' is already defined.");

        current = new SymbolTable(SymbolTable.typeDecl, id, "sort", current);

        int count = (ctx.enum_list().getChildCount() + 1) / 2;
        for (int i = 0; i < count; i++) {
            String eid = ctx.enum_list().ID(i).getText();
            if (current.checkVarRedundant(eid, null))
                error("'" + eid + "' is already defined.");

            current = new SymbolTable(SymbolTable.varDecl, eid, id, current);
        }
    }

    @Override
    public void exitDeclare(SimpleParser.DeclareContext ctx) {
        String type = ctx.type().getText();
        if (!current.checkTypeDeclared(type))
            error("Undeclared Type '" + type + "'");

        String id = ctx.ID().getText();
        if (current.checkVarRedundant(id, block.peek()))
            error("'" + id + "' is already defined.");

        current = new SymbolTable(SymbolTable.varDecl, id, type, current);
    }

    @Override
    public void enterAssign(SimpleParser.AssignContext ctx) {
        String vid = ctx.dest().ID().getText();
        if (!current.checkVarDeclared(vid))
            error("Undeclared Variable '" + vid + "'");
    }

    @Override
    public void enterNested(SimpleParser.NestedContext ctx) { block.push(current); }

    @Override
    public void exitNested(SimpleParser.NestedContext ctx) { current = block.pop(); }

    @Override
    public void enterIdentifier(SimpleParser.IdentifierContext ctx) {
        String vid = ctx.getText();
        if (!current.checkNameDeclared(vid))
            error("Undeclared Variable '" + vid + "'");

        scope.put(ctx, current);
    }
}
