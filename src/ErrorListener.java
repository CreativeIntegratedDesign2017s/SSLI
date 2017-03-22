/**
 * Created by Holim on 2017-03-22.
 */

import org.antlr.v4.runtime.*;

public class ErrorListener extends BaseErrorListener {
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
                            int line, int charPositionInLine, String msg,
                            RecognitionException e)
    { System.err.println("line " + line + ": " + msg); }
}
