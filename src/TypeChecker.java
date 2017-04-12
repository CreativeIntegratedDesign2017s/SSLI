import org.antlr.v4.runtime.tree.*;

class TypeChecker extends SimpleParserBaseVisitor<Integer> {

    GlobalTable global;
    ParseTreeProperty<LocalTable> scope;

    /* Constructor */
    /*
    TypeChecker(GlobalTable _global, ParseTreeProperty<LocalTable> _scope) {
        global = _global;
        scope = _scope;
    }
    */

}
