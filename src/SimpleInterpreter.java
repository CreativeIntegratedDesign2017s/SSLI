import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.apache.commons.cli.*;

public class SimpleInterpreter {
    static int totalLines = 0;

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

            String line = readLine();
            if (line == null) return null;

            StringBuilder code = new StringBuilder(line);
            code.append("\n");

            int paren = parenCount(code.toString());
            while (paren > 0) {
                String appendLine = readLine();
                if (appendLine == null) break;
                else appendLine += "\n";

                code.append(appendLine);
                paren += parenCount(appendLine);
            }
            return code.toString();
        }
    }

    /* Main */
    static public void main(String args[]) throws IOException {

        ModeConfiguration config = new ModeConfiguration(args);
        InputStream is = (config.inOpt) ? (new FileInputStream(config.inFile)) : (System.in);
        CodeReader cr = new CodeReader(is);

        SymbolTable symTable = new SymbolTable();

        String code = cr.readCodes();
        while (code != null) {
            InterpretProgramViaStream(code, symTable, config);
            code = cr.readCodes();
        }
    }

    /* Core Loop */
    static private void InterpretProgramViaStream(String code, SymbolTable symTable, ModeConfiguration config) throws IOException {

        // Build Lexer & Parser
        ANTLRInputStream input = new ANTLRInputStream(code);
        SimpleLexer lexer = new SimpleLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SimpleParser parser = new SimpleParser(tokens);

        // Set Simple Error Listener
        parser.removeErrorListeners();
        parser.addErrorListener(new ErrorListener());

        // Build Parse Tree
        ParseTree tree = null;
        try { tree = parser.prgm(); }
        catch (RuntimeException e) {
            if (config.inOpt) {
                System.err.println(e.getMessage());
                System.exit(-1);
            } else {
                System.out.println(e.getMessage());
                return;
            }
        }

        // Check Type Consistency
        TypeChecker typeChecker = new TypeChecker(symTable);
        try { typeChecker.visit(tree); }
        catch (TypeChecker.TypeException e) {
            symTable.clear();
            if (config.inOpt) {
                System.err.println(
                        String.format("%s ...line %d: %s", e.errorData, e.localLine + totalLines, e.getMessage()));
                System.exit(-1);
            } else {
                System.out.println(e.getMessage());
                return;
            }
        }

        // Declarations Confirmed
        symTable.commit();
        symTable.print();

        totalLines += code.split("\r\n|\r|\n", -1).length - 1;

        // TODO: Make AST & IR Codes
        System.out.println("Tree: " + tree.toStringTree(parser));

        // TODO: Execute the Code
    }

}
