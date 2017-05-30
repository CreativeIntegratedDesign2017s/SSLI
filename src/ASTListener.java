import java.util.ArrayList;

public class ASTListener<T> {
    public T visitConstant(ASTConstant node)	{ return visitChildren(node); }
    public T visitVariable(ASTVariable node)	{ return visitChildren(node); }
    public T visitUnary(ASTUnary node)			{ return visitChildren(node); }
    public T visitBinary(ASTBinary node)		{ return visitChildren(node); }
    public T visitSubscript(ASTSubscript node)	{ return visitChildren(node); }
    public T visitSubstring(ASTSubstring node)	{ return visitChildren(node); }
    public T visitProcCall(ASTProcCall node)	{ return visitChildren(node); }

    public T visitEval(ASTEval node)			{ return visitChildren(node); }
    public T visitDecl(ASTDecl node)			{ return visitChildren(node); }
    public T visitAsgn(ASTAsgn node)			{ return visitChildren(node); }
    public T visitCond(ASTCond node)			{ return visitChildren(node); }
    public T visitUntil(ASTUntil node)			{ return visitChildren(node); }
    public T visitWhile(ASTWhile node)			{ return visitChildren(node); }
    public T visitReturn(ASTReturn node)		{ return visitChildren(node); }
    public T visitNested(ASTNested node)		{ return visitChildren(node); }

    public T visitStmtList(ASTStmtList node)    { return visitChildren(node); }

    public T visitStmtUnit(ASTStmtUnit node)	{ return visitChildren(node); }
    public T visitProcUnit(ASTProcUnit node)	{ return visitChildren(node); }
    public T visitPrgm(ASTPrgm prgm)			{ return visitChildren(prgm); }

    T aggregateResult(T aggregate, T nextResult) {
        return nextResult;
    }
    T visitChildren(ASTNode n) {
        ArrayList<T> aggregate = new ArrayList<T>(){{
           add(null);
        }};
        n.foreachChild(cn -> (Void)aggregate.set(0, aggregateResult(aggregate.get(0), cn.visit(this))));
        return aggregate.get(0);
    }
}
