/**
 * Created by HeeM on 2017-03-13.
 * Simply implemented for System.in by Holim on 2017-03-13.
 * Interactive Mode with parentheses-matcher by Holim on 2017-03-19.
 */

import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class SimpleInterpreter {

    static public int paren_matcher(String line) {
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

        while (true) {
            System.out.print(">>> ");
            String code = br.readLine();
            int paren = paren_matcher(code);

            while (paren != 0) {
                System.out.print("... ");
                String line = br.readLine();
                code += line;
                paren += paren_matcher(line);
            }

            ANTLRInputStream input = new ANTLRInputStream(code);
            SimpleLexer lexer = new SimpleLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SimpleParser parser = new SimpleParser(tokens);
            ParseTree tree = parser.prgm();

            // TODO: Something
            System.out.println(tree.toStringTree(parser));
        }
    }
}
