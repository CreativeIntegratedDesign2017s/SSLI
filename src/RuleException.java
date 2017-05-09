import org.antlr.v4.runtime.ParserRuleContext;

public class RuleException extends RuntimeException {
    String errorData;
    int localLine;
    RuleException(ParserRuleContext ctx, String reason) {
        super(reason);
        errorData = ctx.getText();
        localLine = ctx.start.getLine();
    }
}