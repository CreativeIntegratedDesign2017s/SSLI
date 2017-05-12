class ASTNode {
    public String errorData;
    public int localLine;
}

class PrgmNode extends ASTNode {}

class StmtNode extends ASTNode {}
class DeclareStmt extends StmtNode {}
class AssignStmt extends StmtNode {}
class IfElseStmt extends StmtNode {}
class DoWhileStmt extends StmtNode {}
class WhileDoStmt extends StmtNode {}
class ReturnStmt extends StmtNode {}
class NestedStmt extends StmtNode {
    public List<StmtNode> stmtLst;
}

class ProcNode extends ASTNode {}

class ImportNode extends ASTNode {}

class BlockNode extends ASTNode {}

class Stmt_listNode extends ASTNode {
    public List<StmtNode> stmtLst
}

class ExprNode extends ASTNode {}
class BinaryInfixExpr extends ExprNode {
    public ExprNode Oprnd1;
    public Token op;
    public ExprNode Oprnd2;
}
class AddSubExpr extends BinaryInfixExpr {}

class SubstrExpr extends ExprNode {}

class ASTBuilder {}

