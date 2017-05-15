import java.util.List;

public class ASTUnit {
}

class PrgmNode extends ASTNode {}


class ProcNode extends ASTNode {}

class ImportNode extends ASTNode {}

class BlockNode extends ASTNode {}

class Stmt_listNode extends ASTNode {
    public List<StmtNode> stmtLst;
}
