import org.antlr.v4.runtime.tree.*;
import java.util.*;
import java.util.List;

interface ASTNode {
    String toString();
}

public class ASTBuilder extends SimpleParserBaseVisitor<ASTNode> {

    public List<ASTUnit> prgm = new ArrayList<>();

    public ASTNode visitPrgm(SimpleParser.PrgmContext ctx) {
        for (SimpleParser.UnitContext unit: ctx.unit())
            prgm.add((ASTUnit)visit(unit));
        return null;
    }

    /** 3 Rules for Translation Unit **/
    public ASTNode visitStatement(SimpleParser.StatementContext ctx) {
        return new ASTStmtUnit((ASTStmt)visit(ctx.stmt()));
    }
    public ASTNode visitImport(SimpleParser.ImportContext ctx) {
        return new ASTImportUnit(ctx.STR().getSymbol());
    }
    public ASTNode visitProcedure(SimpleParser.ProcedureContext ctx) {
        ASTProcUnit unit = new ASTProcUnit();
        unit.procName = ctx.ID().getSymbol();
        unit.returnType = (ctx.rtype().VOID() != null) ? null : ctx.rtype().ID().getSymbol();

        SimpleParser.Para_listContext param = ctx.para_list();
        int count = (param.getChildCount() + 1) / 3;
        for (int i = 0 ; i < count; i++) {
            SimpleParser.PtypeContext ptypeCtx = param.ptype(i);
            ASTProcUnit.ParamType ptype = new ASTProcUnit.ParamType();
            ptype.id = param.ID(i).getSymbol();
            ptype.tid = ptypeCtx.ID().getSymbol();
            ptype.reference = (ptypeCtx.getChildCount() > 1);
            ptype.dimension = (ptypeCtx.getChildCount() - 1) / 2;
            unit.paramType.add(ptype);
        }

        SimpleParser.Stmt_listContext stmtList = ctx.block().stmt_list();
        for (SimpleParser.StmtContext st : stmtList.stmt())
            unit.stmt.add((ASTStmt)visit(st));

        return unit;
    }

    /** 8 Rules for Statement **/
    public ASTNode visitEvaluate(SimpleParser.EvaluateContext ctx) {
        return new ASTExprStmt((ASTExpr) visit(ctx.expr()));
    }
    public ASTNode visitDeclare(SimpleParser.DeclareContext ctx) {
        ASTDeclStmt stmt = new ASTDeclStmt();
        SimpleParser.TypeContext type = ctx.type();
        stmt.tid = type.ID().getSymbol();
        for (TerminalNode i : type.INT())
            stmt.size.add(Integer.parseInt(i.getSymbol().getText()));
        stmt.id = ctx.ID().getSymbol();
        if (ctx.init().getChildCount() == 0)
            stmt.init = null;
        else
            stmt.init = (ASTExpr)visit(ctx.init().expr());
        return stmt;
    }
    public ASTNode visitAssign(SimpleParser.AssignContext ctx) {
        return new ASTAsgnStmt((ASTExpr)visit(ctx.expr(0)), (ASTExpr)visit(ctx.expr(1)));
    }
    public ASTNode visitIfElse(SimpleParser.IfElseContext ctx) {
        ASTIfElse cond = new ASTIfElse((ASTExpr)visit(ctx.expr()));
        SimpleParser.Stmt_listContext thenStmt = ctx.stmt_list(0);
        for (SimpleParser.StmtContext st : thenStmt.stmt())
            cond.thenStmt.add((ASTStmt)visit(st));
        SimpleParser.Stmt_listContext elseStmt = ctx.stmt_list(1);
        if (elseStmt != null)
            for (SimpleParser.StmtContext st : elseStmt.stmt())
                cond.elseStmt.add((ASTStmt)visit(st));
        return cond;
    }
    public ASTNode visitDoWhile(SimpleParser.DoWhileContext ctx) {
        ASTDoWhile loop = new ASTDoWhile((ASTExpr)visit(ctx.expr()));
        for (SimpleParser.StmtContext st : ctx.stmt_list().stmt())
            loop.stmt.add((ASTStmt)visit(st));
        return loop;
    }
    public ASTNode visitWhileDo(SimpleParser.WhileDoContext ctx) {
        ASTWhileDo loop = new ASTWhileDo((ASTExpr)visit(ctx.expr()));
        for (SimpleParser.StmtContext st : ctx.stmt_list().stmt())
            loop.stmt.add((ASTStmt)visit(st));
        return loop;
    }
    public ASTNode visitReturn(SimpleParser.ReturnContext ctx) {
        return new ASTReturn((ASTExpr)visit(ctx.expr()));
    }
    public ASTNode visitNested(SimpleParser.NestedContext ctx) {
        ASTNested nest = new ASTNested();
        for (SimpleParser.StmtContext st : ctx.block().stmt_list().stmt())
            nest.stmt.add((ASTStmt)visit(st));
        return nest;
    }

    /** 16 Rules for Expression **/
    public ASTNode visitBracket(SimpleParser.BracketContext ctx) {
        return visit(ctx.expr());
    }
    public ASTNode visitIdentifier(SimpleParser.IdentifierContext ctx) {
        return new ASTTerm(ASTTerm.Sort.ID, ctx.ID().getSymbol());
    }
    public ASTNode visitBoolean(SimpleParser.BooleanContext ctx) {
        return new ASTTerm(ASTTerm.Sort.BOOL, ctx.BOOL().getSymbol());
    }
    public ASTNode visitInteger(SimpleParser.IntegerContext ctx) {
        return new ASTTerm(ASTTerm.Sort.INT, ctx.INT().getSymbol());
    }
    public ASTNode visitString(SimpleParser.StringContext ctx) {
        return new ASTTerm(ASTTerm.Sort.STR, ctx.STR().getSymbol());
    }
    public ASTNode visitUnaryPM(SimpleParser.UnaryPMContext ctx) {
        return new ASTUnary(ctx.op, (ASTExpr)visit(ctx.expr()));
    }
    public ASTNode visitNot(SimpleParser.NotContext ctx) {
        return new ASTUnary(ctx.NOT().getSymbol(), (ASTExpr)visit(ctx.expr()));
    }
    public ASTNode visitPow(SimpleParser.PowContext ctx) {
        return new ASTBinary(ctx.POW().getSymbol(), (ASTExpr)visit(ctx.Base), (ASTExpr)visit(ctx.Exponent));
    }
    public ASTNode visitMulDiv(SimpleParser.MulDivContext ctx) {
        return new ASTBinary(ctx.op, (ASTExpr)visit(ctx.Oprnd1), (ASTExpr)visit(ctx.Oprnd2));
    }
    public ASTNode visitAddSub(SimpleParser.AddSubContext ctx) {
        return new ASTBinary(ctx.op, (ASTExpr)visit(ctx.Oprnd1), (ASTExpr)visit(ctx.Oprnd2));
    }
    public ASTNode visitCompare(SimpleParser.CompareContext ctx) {
        return new ASTBinary(ctx.op, (ASTExpr)visit(ctx.Oprnd1), (ASTExpr)visit(ctx.Oprnd2));
    }
    public ASTNode visitAnd(SimpleParser.AndContext ctx) {
        return new ASTBinary(ctx.AND().getSymbol(), (ASTExpr)visit(ctx.Oprnd1), (ASTExpr)visit(ctx.Oprnd2));
    }
    public ASTNode visitOr(SimpleParser.OrContext ctx) {
        return new ASTBinary(ctx.OR().getSymbol(), (ASTExpr)visit(ctx.Oprnd1), (ASTExpr)visit(ctx.Oprnd2));
    }
    public ASTNode visitSubscript(SimpleParser.SubscriptContext ctx) {
        return new ASTSubscript((ASTExpr)visit(ctx.Container), (ASTExpr)visit(ctx.Indexer));
    }
    public ASTNode visitSubstring(SimpleParser.SubstringContext ctx) {
        return new ASTSubstring((ASTExpr)visit(ctx.Container), (ASTExpr)visit(ctx.From), (ASTExpr)visit(ctx.To));
    }
    public ASTNode visitProcCall(SimpleParser.ProcCallContext ctx) {
        ASTProcCall expr = new ASTProcCall(ctx.ID().getSymbol());
        SimpleParser.Argu_listContext args = ctx.argu_list();
        if (args.getChildCount() != 0 && args.VOID() == null)
            for (SimpleParser.ExprContext param: args.expr())
                expr.param.add((ASTExpr)visit(param));
        return expr;
    }

}
