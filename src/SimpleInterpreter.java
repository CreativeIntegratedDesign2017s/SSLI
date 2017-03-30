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

    private static int parenCount(String line) {
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
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        GlobalTable symbols = new GlobalTable();

        while (true) {
            System.out.print(">>> ");
            String code = br.readLine() + "\n";

            // Match #.brackets in input codes
            int paren = parenCount(code);
            while (paren > 0) {
                System.out.print("... ");
                String line = br.readLine() + "\n";
                code += line;
                paren += parenCount(line);
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
            catch (Exception e) {
                symbols.clear();
                continue;
            }

            // Check Type Consistency
            TypeChecker typeChecker = new TypeChecker(scpChecker.global, scpChecker.scope);
            try { typeChecker.visit(tree); }
            catch (Exception e) {
                symbols.clear();
                continue;
            }

            // Declarations Confirmed
            symbols.commit();
            symbols.print();

            // TODO: Make AST & IR Codes
            System.out.println("Tree: " + tree.toStringTree(parser));

            // TODO: Execute the Code
        }
    }
}
