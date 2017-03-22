/**
 * Created by Holim on 2017-03-22.
 */

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class TypeChecker extends SimpleParserBaseVisitor<Integer> {
    ParseTreeProperty<SymbolTable> scope;

    /* Constructor */
    TypeChecker(ParseTreeProperty<SymbolTable> scope) {
        this.scope = scope;
    }
}
