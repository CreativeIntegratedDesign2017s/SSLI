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
        String readBuffer;
        String lastLine;

        CodeReader(InputStream is) {
            super(new InputStreamReader(is));
            interactive = (is == System.in);
            readBuffer = "";
            lastLine = "";
        }

        String readCode() throws IOException {
            if (interactive) {
                if (readBuffer.length() == 0)
                    System.out.print(">>> ");
                else
                    System.out.print("... ");
            }
            String newCode = readLine();
            if (newCode == null)
                return readBuffer;
            lastLine = newCode;
            readBuffer += lastLine + "\n";
            return readBuffer;
        }

        void flushBuffer() {
            readBuffer = "";
        }

        @Override
        public boolean ready() throws IOException {
            if (interactive) { return lastLine.length() > 0; }
            else
                return super.ready();
        }
    }

    /* Main */
    static public void main(String args[]) throws IOException {
        ModeConfiguration config = new ModeConfiguration(args);
        InputStream is = (config.inOpt) ? (new FileInputStream(config.inFile)) : (System.in);
        CodeReader cr = new CodeReader(is);

        SymbolTable symTable = new SymbolTable();

        InterpretProgramViaStream(cr, symTable, config);
    }

    /* Core Loop */
    static private void InterpretProgramViaStream(CodeReader reader, SymbolTable symTable, ModeConfiguration config) throws IOException {
        do {
            String code = reader.readCode();

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
            try {
                tree = parser.prgm();
            } catch (RuntimeException e) {
                if (reader.ready()) {
                    continue;
                }
                if (config.inOpt) {
                    System.err.println(e.getMessage());
                    System.exit(-1);
                } else {
                    System.out.println(e.getMessage());
                    continue;
                }
            }

            // Check Type Consistency
            TypeChecker typeChecker = new TypeChecker(symTable);
            try {
                typeChecker.visit(tree);
            } catch (TypeChecker.TypeException e) {
                symTable.clear();
                if (config.inOpt) {
                    System.err.println(
                            String.format("%s ...line %d: %s", e.errorData, e.localLine + totalLines, e.getMessage()));
                    System.exit(-1);
                } else {
                    System.err.println(e.getMessage());
                    reader.flushBuffer();
                    continue;
                }
            }

            // Declarations Confirmed
            symTable.commit();
            symTable.print();

            totalLines += code.split("\r\n|\r|\n", -1).length - 1;

            // TODO: Make AST & IR Codes
            System.out.println("Tree: " + tree.toStringTree(parser));

            // TODO: Execute the Code
            reader.flushBuffer();
        } while (reader.ready());
    }

}
