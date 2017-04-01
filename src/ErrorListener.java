import org.antlr.v4.runtime.*;

public class ErrorListener extends BaseErrorListener {
    private boolean interactive;

    ErrorListener(boolean _interactive) {
        interactive = _interactive;
    }

    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e) {
        if (interactive)
            System.out.println("line " + line + ": " + msg);
        else
            System.err.println("line " + line + ": " + msg);
    }
}
