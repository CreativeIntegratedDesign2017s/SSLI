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
    // public ASTNode visitProcedure(SimpleParser.ProcedureContext ctx) { return this.visitChildren(ctx); }
    // public ASTNode visitImport(SimpleParser.ImportContext ctx) { return this.visitChildren(ctx); }

    /** 8 Rules for Statement **/
    public ASTNode visitEvaluate(SimpleParser.EvaluateContext ctx) {
        return new ASTExprStmt((ASTExpr) visit(ctx.expr()));
    }
    // public ASTNode visitDeclare(SimpleParser.DeclareContext ctx) { return visitChildren(ctx); }
    // public ASTNode visitAssign(SimpleParser.AssignContext ctx) { return visitChildren(ctx); }
    // public ASTNode visitIfElse(SimpleParser.IfElseContext ctx) { return visitChildren(ctx); }
    // public ASTNode visitDoWhile(SimpleParser.DoWhileContext ctx) { return visitChildren(ctx); }
    // public ASTNode visitWhileDo(SimpleParser.WhileDoContext ctx) { return visitChildren(ctx); }
    // public ASTNode visitReturn(SimpleParser.ReturnContext ctx) { return visitChildren(ctx); }
    // public ASTNode visitNested(SimpleParser.NestedContext ctx) { return visitChildren(ctx); }


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
        return this.visitChildren(ctx);
    }
    public ASTNode visitSubstring(SimpleParser.SubstringContext ctx) {
        return this.visitChildren(ctx);
    }
    public ASTNode visitProcCall(SimpleParser.ProcCallContext ctx) {
        return this.visitChildren(ctx);
    }

}
