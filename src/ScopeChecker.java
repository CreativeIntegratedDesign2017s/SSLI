import java.util.*;
import org.antlr.v4.runtime.tree.*;

public class ScopeChecker extends SimpleParserBaseListener {

    /* Global table has global variables, types and procs.
     * Local tables have local variables for its proc or a block.
     * And each context has its scope. If it is not designated,
     * then identifiers that used in that context should be found
     * in global scope.
     */
    GlobalTable global;
    ParseTreeProperty<LocalTable> scope;
    private LocalTable current;
    private Stack<LocalTable> scpStack;

    /* Constructor */
    ScopeChecker(GlobalTable symbols) {
        current = null;
        scpStack = new Stack<>();
        global = symbols;
        scope = new ParseTreeProperty<>();
    }

    @Override
    public void enterProcedure(SimpleParser.ProcedureContext ctx) {
        global.declProc(ctx.ID().getText(),		// Proc ID
                ctx.para_list(),				// Proc parameters
                ctx.rtype().getText());			// Proc return
        current = new LocalTable(ctx.ID().getText(), global);
    }

    @Override
    public void exitProcedure(SimpleParser.ProcedureContext ctx) {
        current.print();
        current = null;
    }

    @Override
    public void enterPara_list(SimpleParser.Para_listContext ctx) {
        for (int i = 0; i < (ctx.getChildCount() + 1) / 3; i++)
            current.declVar(ctx.ID(i).getText(), ctx.ptype(i));
    }

    @Override
    public void enterEnumerate(SimpleParser.EnumerateContext ctx) {
        global.declEnum(ctx.ID().getText(), ctx.enum_list());
    }

    @Override
    public void exitDeclare(SimpleParser.DeclareContext ctx) {
        if (current == null)
            global.declVar(ctx.ID().getText(), ctx.type());
        else
            current.declVar(ctx.ID().getText(), ctx.type());
    }

    @Override
    public void enterNested(SimpleParser.NestedContext ctx) {
        scpStack.push(current);
        if (current == null)
            current = new LocalTable("Nested", global);
        else
            current = new LocalTable(current);
    }

    @Override
    public void exitNested(SimpleParser.NestedContext ctx) {
        current.print();
        current = scpStack.pop();
    }

    @Override
    public void enterIdentifier(SimpleParser.IdentifierContext ctx) {
        String vid = ctx.getText();
        if (current == null) {
            if (!global.isTypedID(vid))
                throw new RuntimeException(vid + ": Undeclared Identifier");
        }
        else {
            if (!current.isTypedID(vid))
                throw new RuntimeException(vid + ": Undeclared Identifier");
            scope.put(ctx, current);
        }
    }
}
