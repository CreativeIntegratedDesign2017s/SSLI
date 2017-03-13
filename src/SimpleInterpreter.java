/**
 * Created by HeeM on 2017-03-13.
 * Simply implemented for System.in by Holim on 2017-03-13.
 */

import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class SimpleInterpreter {
    static public void main(String args[]) throws IOException {
    	System.out.println("Welcome to Simple Interpreter!");

        ANTLRInputStream input = new ANTLRInputStream(System.in);

        SimpleLexer lexer = new SimpleLexer(input);

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        SimpleParser parser = new SimpleParser(tokens);

        ParseTree tree = parser.prgm();

        System.out.println(tree.toStringTree(parser));
    }
}
