/**
 * Created by HeeM on 2017-03-13.
 * Simply implemented for System.in by Holim on 2017-03-13.
 * Interactive Mode with parentheses-matcher by Holim on 2017-03-19.
 * Bail-out Error Reporting & Scope Checker by Holim on 2017-03-22.
 */

import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class SimpleInterpreter {

    static int paren_matcher(String line) {
        int paren = 0;
        for (int i = 0; i < line.length(); i++) {
            switch (line.charAt(i)) {
                case '(': case '{': case '[':
                    paren += 1; break;
                case ')': case '}': case ']':
                    paren -= 1; break;
            }
        }
        return paren;
    }

    static public void main(String args[]) throws IOException {
        System.out.println("Welcome to Simple Interpreter!");
        InputStream is = System.in;
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        SymbolTable symbols = new SymbolTable(null);
        symbols.declType("bool");
        symbols.declType("int");
        symbols.declType("str");
        symbols.declProc("print", "bool", "void");
        symbols.declProc("print", "int", "void");
        symbols.declProc("print", "str", "void");
        symbols.declProc("exit", "void", "void");

        while (true) {
            System.out.print(">>> ");
            String code = br.readLine() + "\n";

            // Match #.brackets in input codes
            int paren = paren_matcher(code);
            while (paren > 0) {
                System.out.print("... ");
                String line = br.readLine() + "\n";
                code += line;
                paren += paren_matcher(line);
            }

            // Build Lexer & Parser
            ANTLRInputStream input = new ANTLRInputStream(code);
            SimpleLexer lexer = new SimpleLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SimpleParser parser = new SimpleParser(tokens);

            // Set Error Listener & Strategy
            parser.removeErrorListeners();
            parser.addErrorListener(new ErrorListener());
            parser.setErrorHandler(new ErrorStrategy());

            // Build Parse Tree
            ParseTree tree;
            try { tree = parser.prgm(); }
            catch (Exception e) { continue; }

            // Check Scope Consistency
            ScopeChecker scpChecker = new ScopeChecker(symbols);
            ParseTreeWalker walker = new ParseTreeWalker();
            try { walker.walk(scpChecker, tree); }
            catch (Exception e) { continue; }
            finally {
                scpChecker.printGlobal();
            }

            // Check Type Consistency
            TypeChecker typeChecker = new TypeChecker(scpChecker.scope);
            try { typeChecker.visit(tree); }
            catch (Exception e) { continue; }

            // TODO: Make AST & IR Codes
            System.out.println(tree.toStringTree(parser));

            // TODO: Execute the Code
        }
    }
}
