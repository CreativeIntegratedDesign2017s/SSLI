/**
 * Created by Holim on 2017-03-21.
 */

import java.util.*;
import org.antlr.v4.runtime.tree.*;

public class ScopeChecker extends SimpleParserBaseListener {
    /* Symbol Table & Scope */
    SymbolTable current;
    SymbolTable global;
    Stack<SymbolTable> block;
    public ParseTreeProperty<SymbolTable> scope;
    String scope_name;		// debug
    int scope_depth = 0;

    /* Constructor */
    public ScopeChecker(SymbolTable symbols) {
        this.current = symbols;
        this.global = symbols;
        block = new Stack<SymbolTable>();
        scope = new ParseTreeProperty<SymbolTable>();
        scope_name = "Global";
    }

    /* Error Reporting */
    private void error(String msg) {
        System.err.println(msg);
        throw new RuntimeException();
    }

    @Override
    public void enterProcedure(SimpleParser.ProcedureContext ctx) {
        // Check rType
        String rtype = ctx.rtype().getText();
        if (!global.hasType(rtype) && !rtype.equals("void"))
            error(rtype + ": Undeclared type");

        // Check pTypes
        List<String> ptype = new ArrayList<String>();
        int count = (ctx.para_list().getChildCount() + 1) / 3;
        for (int i = 0; i < count; i++) {
            ptype.add(ctx.para_list().ptype(i).getText());
            if (!global.hasType(ptype.get(i)))
                error(ptype.get(i) + ": Undeclared type");
        }

        // Check procedure name
        String id = ctx.ID().getText();
        if (global.hasProc(id, String.join(",",ptype)) || global.hasVar(id))
            error(id + "(" + String.join(",", ptype) + "): Already defined");

        global.declProc(id, String.join(",", ptype), rtype);
        block.push(global);
        current = new SymbolTable(current);
        scope_name = id;

        // Declare parameters in inner scope
        for (int i = 0; i < count; i++) {
            String pid = ctx.para_list().ID(i).getText();
            if (current.vars.get(pid) != null
            || global.types.get(pid) != null
            || global.procs.get(pid) != null)
                error(pid + ": Already defined");
            current.declVar(pid, ctx.para_list().ptype(i).getText());
        }
    }

    @Override
    public void exitProcedure(SimpleParser.ProcedureContext ctx) {
        if (!current.vars.isEmpty())
            current.print(scope_name);
        current = block.pop();
        scope_name = "Global";
    }

    @Override
    public void enterEnumerate(SimpleParser.EnumerateContext ctx) {
        // Check Enumerator Type name
        String id = ctx.ID().getText();
        if (global.types.get(id) != null
        || global.vars.get(id) != null)
            error(id + ": Already defined");
        global.declType(id);

        int count = (ctx.enum_list().getChildCount() + 1) / 2;
        for (int i = 0; i < count; i++) {
            String eid = ctx.enum_list().ID(i).getText();
            if (global.vars.get(eid) != null
            || global.types.get(eid) != null
            || global.procs.get(eid) != null)
                error(eid + ": Already defined");
            global.declVar(eid, id);
        }
    }

    @Override
    public void exitDeclare(SimpleParser.DeclareContext ctx) {
        String type = ctx.type().getText();
        if (!global.hasType(type))
            error("Undeclared Type '" + type + "'");

        String id = ctx.ID().getText();
        if (current.vars.get(id) != null
        || global.types.get(id) != null
        || global.procs.get(id) != null)
            error("'" + id + "' is already defined.");

        current.declVar(id, type);
    }

    @Override
    public void enterAssign(SimpleParser.AssignContext ctx) {
        String vid = ctx.dest().ID().getText();
        if (!current.isNameDeclared(vid))
            error(vid + ": Undeclared Identifier");
        scope.put(ctx, current);
    }

    @Override
    public void enterNested(SimpleParser.NestedContext ctx) {
        block.push(current);
        current = new SymbolTable(current);
        scope_depth += 1;
    }

    @Override
    public void exitNested(SimpleParser.NestedContext ctx) {
        if (!current.vars.isEmpty())
            current.print(scope_name + " " + String.valueOf(scope_depth) + "th");
        current = block.pop();
        scope_depth -= 1;
    }

    @Override
    public void enterIdentifier(SimpleParser.IdentifierContext ctx) {
        String vid = ctx.getText();
        if (!current.isNameDeclared(vid))
            error(vid + ": Undeclared Identifier");
        scope.put(ctx, current);
    }
}
