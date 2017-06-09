import AST.ASTNode;

public class RuleException extends RuntimeException {
    String errorData;
    int localLine;
    RuleException(ASTNode ctx, String reason) {
        super(reason);
        errorData = ctx.getText();
        localLine = ctx.getLine();
    }

    RuleException(String errData, int line, String reason){
        super(reason);
        errorData = errData;
        localLine = line;
    }
}