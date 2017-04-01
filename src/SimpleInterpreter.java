import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.apache.commons.cli.*;

public class SimpleInterpreter {

    static class ModeConfiguration {
        boolean inOpt;
        boolean outOpt;
        String inFile;
        String outFile;

        ModeConfiguration(String args[]) {
            Options options = new Options();
            options.addOption(new Option("f", true, "input file"));
            options.addOption(new Option("o", true, "output file"));
            try {
                CommandLineParser parser = new DefaultParser();
                CommandLine cmd = parser.parse(options, args);
                inOpt = cmd.hasOption("f");
                inFile = cmd.getOptionValue("f");
                outOpt = cmd.hasOption("o");
                outFile = cmd.getOptionValue("o");
            }
            catch (ParseException e) {
                System.err.println(e.getMessage());
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ssli", options);
                System.exit(-1);
            }
        }
    }

    static class CodeReader extends BufferedReader {
        private boolean interactive;

        CodeReader(InputStream is) {
            super(new InputStreamReader(is));
            interactive = (is == System.in);
        }

        private int parenCount(String line) {
            if (line == null) return 0;

            int paren = 0;
            ANTLRInputStream input = new ANTLRInputStream(line);
            SimpleLexer lexer = new SimpleLexer(input);
            for (Token t : lexer.getAllTokens()) {
                switch (t.getType()) {
                    case SimpleLexer.LRB:
                    case SimpleLexer.LCB:
                    case SimpleLexer.LSB:
                    case SimpleLexer.IF:
                    case SimpleLexer.WHILE:
                        paren += 1; break;
                    case SimpleLexer.RRB:
                    case SimpleLexer.RCB:
                    case SimpleLexer.RSB:
                    case SimpleLexer.END:
                        paren -= 1; break;
                }
            }
            return paren;
        }

        String readCodes() throws IOException {
            if (interactive) System.out.print(">>> ");

            String code = readLine();
            int paren = parenCount(code);
            while (paren > 0) {
                String line = readLine();
                if (line == null) break;

                code += "\n" + line;
                paren += parenCount(line);
            }
            return code;
        }
    }

    /* Main */
    static public void main(String args[]) throws IOException {

        ModeConfiguration config = new ModeConfiguration(args);
        InputStream is = (config.inOpt) ? (new FileInputStream(config.inFile)) : (System.in);
        CodeReader cr = new CodeReader(is);

        GlobalTable symbols = new GlobalTable();

        String code = cr.readCodes();
        while (code != null) {
            InterpretProgramViaStream(code, symbols, config);
            code = cr.readCodes();
        }
    }

    /* Core Loop */
    static private void InterpretProgramViaStream(String code, GlobalTable symbols, ModeConfiguration config) throws IOException {

        // Build Lexer & Parser
        ANTLRInputStream input = new ANTLRInputStream(code);
        SimpleLexer lexer = new SimpleLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimpleParser parser = new SimpleParser(tokens);

        // Set Error Listener & Strategy
        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorListener(!config.inOpt));
        parser.setErrorHandler(new ErrorStrategy());

        // Build Parse Tree
        ParseTree tree;
        try { tree = parser.prgm(); }
        catch (Exception e) {
            if (config.inOpt) System.exit(-1);
            return;
        }

        // Check Scope Consistency
        ScopeChecker scpChecker = new ScopeChecker(symbols);
        ParseTreeWalker walker = new ParseTreeWalker();
        try { walker.walk(scpChecker, tree); }
        catch (RuntimeException e) {
            symbols.clear();
            if (config.inOpt) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
            else {
                System.out.println(e.getMessage());
                return;
            }
        }

        // Check Type Consistency
        TypeChecker typeChecker = new TypeChecker(scpChecker.global, scpChecker.scope);
        try { typeChecker.visit(tree); }
        catch (RuntimeException e) {
            symbols.clear();
            if (config.inOpt) {
                System.err.println(e.getMessage());
                System.exit(-1);
            }
            else {
                System.out.println(e.getMessage());
                return;
            }
        }

        // Declarations Confirmed
        symbols.commit();
        symbols.print();

        // TODO: Make AST & IR Codes
        System.out.println("Tree: " + tree.toStringTree(parser));

        // TODO: Execute the Code
    }

}
