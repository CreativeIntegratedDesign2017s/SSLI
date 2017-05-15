import java.util.List;

public class ASTExpr {
}

class ExprNode extends ASTNode {}
class BinaryExpr extends ExprNode {
    public ExprNode oprnd1;
    public ExprNode oprnd2;
}
class PowExpr extends BinaryExpr {}
class MulExpr extends BinaryExpr {}
class DivExpr extends BinaryExpr {}
class AddExpr extends BinaryExpr {}
class SubExpr extends BinaryExpr {}
class AndExpr extends BinaryExpr {}
class OrExpr extends BinaryExpr {}

class LTExpr extends BinaryExpr {}
class LEExpr extends BinaryExpr {}
class EQExpr extends BinaryExpr {}
class NEQExpr extends BinaryExpr {}
class GEExpr extends BinaryExpr {}
class GTExpr extends BinaryExpr {}

class UnaryExpr extends ExprNode {
    public ExprNode oprnd;
}
class PlusExpr extends UnaryExpr {}
class MinusExpr extends UnaryExpr {}
class NegativeExpr extends UnaryExpr {}

class ProcCallExpr extends ExprNode {
    List<ExprNode> argumentList;
}
class SubscriptExpr extends ExprNode {}
class SubstrExpr extends ExprNode {}

class TerminalExpr extends ExprNode {}
class ID extends TerminalExpr {}
class INT extends TerminalExpr {}
class BOOL extends TerminalExpr {}
class STR extends TerminalExpr {}
