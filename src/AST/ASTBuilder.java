package AST;

import ANTLR.*;
import ANTLR.SimpleParser.*;
import org.antlr.v4.runtime.tree.*;

public class ASTBuilder extends SimpleParserBaseVisitor<ASTNode> {

    public ASTNode visitPrgm(PrgmContext ctx) {
        ASTPrgm prgm = new ASTPrgm(ctx);
        for (UnitContext unit: ctx.unit())
            prgm.units.add((ASTUnit)visit(unit));
        return prgm;
    }

    /** Rules for Translation Unit **/
    public ASTNode visitStatement(StatementContext ctx) {
        ASTStmt stmt = (ASTStmt)visit(ctx.stmt());
        return new ASTStmtUnit(ctx, stmt);
    }
    public ASTNode visitProcedure(ProcedureContext ctx) {
        ASTProcUnit unit = new ASTProcUnit(ctx);
        unit.pid = ctx.ID().getSymbol();

        Para_listContext param = ctx.para_list();
        int count = (param.getChildCount() + 1) / 3;
        for (int i = 0 ; i < count; i++) {
            PtypeContext ptypeCtx = param.ptype(i);
            ASTProcUnit.ParaType ptype = new ASTProcUnit.ParaType();
            ptype.var = param.ID(i).getSymbol();
            ptype.tid = ptypeCtx.ID().getSymbol();
            ptype.dim = (ptypeCtx.getChildCount() - 1) / 2;
            ptype.ref = (ptypeCtx.getChildCount() > 1);
            unit.type.add(ptype);
        }

        unit.stmtList = (ASTStmtList)visit(ctx.block().stmt_list());

        return unit;
    }

    public ASTNode visitStmt_list(Stmt_listContext ctx) {
        ASTStmtList stmtList = new ASTStmtList(ctx);
        for (StmtContext st : ctx.stmt())
            stmtList.list.add((ASTStmt)visit(st));
        return stmtList;
    }

    /** 8 Rules for Statement **/
    public ASTNode visitEvaluate(EvaluateContext ctx) {
        ASTExpr expr = (ctx.expr() == null) ? null : (ASTExpr)visit(ctx.expr());
        return new ASTEval(ctx, expr);
    }
    public ASTNode visitDeclare(DeclareContext ctx) {
        TypeContext type = ctx.type();
        ASTDecl stmt = new ASTDecl(ctx);
        stmt.type.tid = type.ID().getSymbol();
        for (TerminalNode i : type.INT())
            stmt.type.size.add(Integer.parseInt(i.getSymbol().getText()));
        stmt.var = ctx.ID().getSymbol();
        stmt.init = (ctx.init().getChildCount() == 0) ? null : (ASTExpr)visit(ctx.init().expr());
        return stmt;
    }
    public ASTNode visitAssign(AssignContext ctx) {
        ASTExpr lval = (ASTExpr)visit(ctx.expr(0));
        ASTExpr rval = (ASTExpr)visit(ctx.expr(1));
        return new ASTAsgn(ctx, lval, rval);
    }
    public ASTNode visitIfElse(IfElseContext ctx) {
        ASTCond stmt = new ASTCond(ctx);
        stmt.cond = (ASTExpr)visit(ctx.expr());
        stmt.thenStmtList = (ASTStmtList)visit(ctx.stmt_list(0));
        Stmt_listContext elseStmt = ctx.stmt_list(1);
        if (elseStmt != null)
            stmt.elseStmtList = (ASTStmtList)visit(ctx.stmt_list(1));
        else
            stmt.elseStmtList = null;
        return stmt;
    }
    public ASTNode visitDoWhile(DoWhileContext ctx) {
        ASTUntil stmt = new ASTUntil(ctx);
        stmt.cond = (ASTExpr)visit(ctx.expr());
        stmt.loop = (ASTStmtList)visit(ctx.stmt_list());
        return stmt;
    }
    public ASTNode visitWhileDo(WhileDoContext ctx) {
        ASTWhile stmt = new ASTWhile(ctx);
        stmt.cond = (ASTExpr)visit(ctx.expr());
        stmt.loop = (ASTStmtList)visit(ctx.stmt_list());
        return stmt;
    }
    public ASTNode visitReturn(ReturnContext ctx) {
        ASTExpr val = (ctx.expr() == null) ? null : (ASTExpr)visit(ctx.expr());
        return new ASTReturn(ctx, val);
    }
    public ASTNode visitNested(NestedContext ctx) {
        ASTNested nest = new ASTNested(ctx);
        nest.stmtList = (ASTStmtList)visit(ctx.block().stmt_list());
        return nest;
    }

    /** 16 Rules for Expression **/
    public ASTNode visitBracket(BracketContext ctx) {
        return visit(ctx.expr());
    }
    public ASTNode visitBoolean(BooleanContext ctx) {
        return new ASTConstant(ctx, ctx.BOOL().getSymbol(), ASTConstant.ConstantType.Boolean);
    }
    public ASTNode visitInteger(IntegerContext ctx) {
        return new ASTConstant(ctx, ctx.INT().getSymbol(), ASTConstant.ConstantType.Integer);
    }
    public ASTNode visitString(StringContext ctx) {
        return new ASTConstant(ctx, ctx.STR().getSymbol(), ASTConstant.ConstantType.String);
    }
    public ASTNode visitIdentifier(IdentifierContext ctx) {
        return new ASTVariable(ctx, ctx.ID().getSymbol());
    }
    public ASTNode visitUnaryPM(UnaryPMContext ctx) {
        return new ASTUnary(ctx, ctx.op, (ASTExpr)visit(ctx.expr()));
    }
    public ASTNode visitNot(NotContext ctx) {
        return new ASTUnary(ctx, ctx.NOT().getSymbol(), (ASTExpr)visit(ctx.expr()));
    }
    public ASTNode visitPow(PowContext ctx) {
        return new ASTBinary(ctx, ctx.POW().getSymbol(), (ASTExpr)visit(ctx.Base), (ASTExpr)visit(ctx.Exponent));
    }
    public ASTNode visitMulDiv(MulDivContext ctx) {
        return new ASTBinary(ctx, ctx.op, (ASTExpr)visit(ctx.Oprnd1), (ASTExpr)visit(ctx.Oprnd2));
    }
    public ASTNode visitAddSub(AddSubContext ctx) {
        return new ASTBinary(ctx, ctx.op, (ASTExpr)visit(ctx.Oprnd1), (ASTExpr)visit(ctx.Oprnd2));
    }
    public ASTNode visitCompare(CompareContext ctx) {
        return new ASTBinary(ctx, ctx.op, (ASTExpr)visit(ctx.Oprnd1), (ASTExpr)visit(ctx.Oprnd2));
    }
    public ASTNode visitAnd(AndContext ctx) {
        return new ASTBinary(ctx, ctx.AND().getSymbol(), (ASTExpr)visit(ctx.Oprnd1), (ASTExpr)visit(ctx.Oprnd2));
    }
    public ASTNode visitOr(OrContext ctx) {
        return new ASTBinary(ctx, ctx.OR().getSymbol(), (ASTExpr)visit(ctx.Oprnd1), (ASTExpr)visit(ctx.Oprnd2));
    }
    public ASTNode visitSubscript(SubscriptContext ctx) {
        return new ASTSubscript(ctx, (ASTExpr)visit(ctx.Container), (ASTExpr)visit(ctx.Indexer));
    }
    public ASTNode visitSubstring(SubstringContext ctx) {
        return new ASTSubstring(ctx, (ASTExpr)visit(ctx.Container), (ASTExpr)visit(ctx.From), (ASTExpr)visit(ctx.To));
    }
    public ASTNode visitProcCall(ProcCallContext ctx) {
        ASTProcCall expr = new ASTProcCall(ctx, ctx.ID().getSymbol());
        Argu_listContext args = ctx.argu_list();
        if (args.getChildCount() != 0 && args.VOID() == null)
            for (ExprContext param: args.expr())
                expr.param.add((ASTExpr)visit(param));
        return expr;
    }

}
