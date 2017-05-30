
public class ASTListener<T> {
    public T visitConstant(ASTConstant node)	{ return null; }
    public T visitVariable(ASTVariable node)	{ return null; }
    public T visitUnary(ASTUnary node)			{ return null; }
    public T visitBinary(ASTBinary node)		{ return null; }
    public T visitSubscript(ASTSubscript node)	{ return null; }
    public T visitSubstring(ASTSubstring node)	{ return null; }
    public T visitProcCall(ASTProcCall node)	{ return null; }

    public T visitEval(ASTEval node)			{ return null; }
    public T visitDecl(ASTDecl node)			{ return null; }
    public T visitAsgn(ASTAsgn node)			{ return null; }
    public T visitCond(ASTCond node)			{ return null; }
    public T visitUntil(ASTUntil node)			{ return null; }
    public T visitWhile(ASTWhile node)			{ return null; }
    public T visitReturn(ASTReturn node)		{ return null; }
    public T visitNested(ASTNested node)		{ return null; }

    public T visitStmtUnit(ASTStmtUnit node)	{ return null; }
    public T visitProcUnit(ASTProcUnit node)	{ return null; }
    public T visitPrgm(ASTPrgm prgm)			{ return null; }
}
