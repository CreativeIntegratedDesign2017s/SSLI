import java.util.List;

public class ASTStmt {
}

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
