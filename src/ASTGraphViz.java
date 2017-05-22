
public class ASTGraphViz extends ASTListener<Object> {
    public Object visitConstant(ASTConstant node)	{ return null; }
    public Object visitVariable(ASTVariable node)	{ return null; }
    public Object visitUnary(ASTUnary node)			{ return null; }
    public Object visitBinary(ASTBinary node)		{ return null; }
    public Object visitSubscript(ASTSubscript node)	{ return null; }
    public Object visitSubstring(ASTSubstring node)	{ return null; }
    public Object visitProcCall(ASTProcCall node)	{ return null; }

    public Object visitEval(ASTEval node)			{ return null; }
    public Object visitDecl(ASTDecl node)			{ return null; }
    public Object visitAsgn(ASTAsgn node)			{ return null; }
    public Object visitCond(ASTCond node)			{ return null; }
    public Object visitUntil(ASTUntil node)			{ return null; }
    public Object visitWhile(ASTWhile node)			{ return null; }
    public Object visitReturn(ASTReturn node)		{ return null; }
    public Object visitNested(ASTNested node)		{ return null; }

    public Object visitStmtUnit(ASTStmtUnit node)	{ return null; }
    public Object visitProcUnit(ASTProcUnit node)	{ return null; }
    public Object visitPrgm(ASTPrgm prgm)			{ return null; }
}
