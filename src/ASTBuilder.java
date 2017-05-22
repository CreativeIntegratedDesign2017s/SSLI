import org.antlr.v4.runtime.tree.*;

public class ASTBuilder extends SimpleParserBaseVisitor<ASTNode> {

    public ASTNode visitPrgm(SimpleParser.PrgmContext ctx) {
        ASTPrgm prgm = new ASTPrgm();
        for (SimpleParser.UnitContext unit: ctx.unit())
            prgm.units.add((ASTUnit)visit(unit));
        return prgm;
    }

    /** Rules for Translation Unit **/
    public ASTNode visitStatement(SimpleParser.StatementContext ctx) {
        ASTStmt stmt = (ASTStmt)visit(ctx.stmt());
        return new ASTStmtUnit(stmt);
    }
    public ASTNode visitProcedure(SimpleParser.ProcedureContext ctx) {
        ASTProcUnit unit = new ASTProcUnit();
        unit.pid = ctx.ID().getSymbol();
        unit.returnType = (ctx.rtype().VOID() != null) ? null : ctx.rtype().ID().getSymbol();

        SimpleParser.Para_listContext param = ctx.para_list();
        int count = (param.getChildCount() + 1) / 3;
        for (int i = 0 ; i < count; i++) {
            SimpleParser.PtypeContext ptypeCtx = param.ptype(i);
            ASTProcUnit.ParaType ptype = new ASTProcUnit.ParaType();
            ptype.var = param.ID(i).getSymbol();
            ptype.tid = ptypeCtx.ID().getSymbol();
            ptype.dim = (ptypeCtx.getChildCount() - 1) / 2;
            ptype.ref = (ptypeCtx.getChildCount() > 1);
            unit.type.add(ptype);
        }

        SimpleParser.Stmt_listContext stmtList = ctx.block().stmt_list();
        for (SimpleParser.StmtContext st : stmtList.stmt())
            unit.stmt.add((ASTStmt)visit(st));

        return unit;
    }

    /** 8 Rules for Statement **/
    public ASTNode visitEvaluate(SimpleParser.EvaluateContext ctx) {
        ASTExpr expr = (ctx.expr() == null) ? null : (ASTExpr)visit(ctx.expr());
        return new ASTEval(expr);
    }
    public ASTNode visitDeclare(SimpleParser.DeclareContext ctx) {
        SimpleParser.TypeContext type = ctx.type();
        ASTDecl stmt = new ASTDecl();
        stmt.type.tid = type.ID().getSymbol();
        for (TerminalNode i : type.INT())
            stmt.type.size.add(Integer.parseInt(i.getSymbol().getText()));
        stmt.var = ctx.ID().getSymbol();
        stmt.init = (ctx.init().getChildCount() == 0) ? null : (ASTExpr)visit(ctx.init().expr());
        return stmt;
    }
    public ASTNode visitAssign(SimpleParser.AssignContext ctx) {
        ASTExpr lval = (ASTExpr)visit(ctx.expr(0));
        ASTExpr rval = (ASTExpr)visit(ctx.expr(1));
        return new ASTAsgn(lval, rval);
    }
    public ASTNode visitIfElse(SimpleParser.IfElseContext ctx) {
        ASTCond stmt = new ASTCond();
        stmt.cond = (ASTExpr)visit(ctx.expr());
        SimpleParser.Stmt_listContext thenStmt = ctx.stmt_list(0);
        for (SimpleParser.StmtContext st : thenStmt.stmt())
            stmt.thenStmt.add((ASTStmt)visit(st));
        SimpleParser.Stmt_listContext elseStmt = ctx.stmt_list(1);
        if (elseStmt != null)
            for (SimpleParser.StmtContext st : elseStmt.stmt())
                stmt.elseStmt.add((ASTStmt)visit(st));
        return stmt;
    }
    public ASTNode visitDoWhile(SimpleParser.DoWhileContext ctx) {
        ASTUntil stmt = new ASTUntil();
        stmt.cond = (ASTExpr)visit(ctx.expr());
        for (SimpleParser.StmtContext st : ctx.stmt_list().stmt())
            stmt.loop.add((ASTStmt)visit(st));
        return stmt;
    }
    public ASTNode visitWhileDo(SimpleParser.WhileDoContext ctx) {
        ASTWhile stmt = new ASTWhile();
        stmt.cond = (ASTExpr)visit(ctx.expr());
        for (SimpleParser.StmtContext st : ctx.stmt_list().stmt())
            stmt.loop.add((ASTStmt)visit(st));
        return stmt;
    }
    public ASTNode visitReturn(SimpleParser.ReturnContext ctx) {
        ASTExpr val = (ctx.expr() == null) ? null : (ASTExpr)visit(ctx.expr());
        return new ASTReturn(val);
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
    public ASTNode visitBoolean(SimpleParser.BooleanContext ctx) {
        return new ASTConstant(ctx.BOOL().getSymbol());
    }
    public ASTNode visitInteger(SimpleParser.IntegerContext ctx) {
        return new ASTConstant(ctx.INT().getSymbol());
    }
    public ASTNode visitString(SimpleParser.StringContext ctx) {
        return new ASTConstant(ctx.STR().getSymbol());
    }
    public ASTNode visitIdentifier(SimpleParser.IdentifierContext ctx) {
        return new ASTVariable(ctx.ID().getSymbol());
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
