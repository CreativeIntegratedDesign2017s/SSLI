import org.antlr.v4.runtime.*;

public class ErrorListener extends BaseErrorListener {
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e) {
        throw new RuntimeException("line " + line + ": " + msg);
    }
}
