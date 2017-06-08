import ANTLR.*;
import AST.*;
import SVM.*;
import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.apache.commons.cli.*;

public class SimpleInterpreter {
    static private int totalLines;
    static private ModeConfig config;
    static private CodeReader reader;
    static private SymbolTable symTable;
    static private StackIndex globalIndex;

    static class ModeConfig {
        boolean inOpt;
        boolean outOpt;
        boolean logOpt;
        String inFile;
        String outFile;

        ModeConfig(String args[]) {
            Options options = new Options();
            options.addOption(new Option("f", true, "input file"));
            options.addOption(new Option("o", true, "output file"));
            options.addOption(new Option("l", false, "write log or not"));
            try {
                CommandLineParser parser = new DefaultParser();
                CommandLine cmd = parser.parse(options, args);
                inOpt = cmd.hasOption("f");
                inFile = cmd.getOptionValue("f");
                outOpt = cmd.hasOption("o");
                outFile = cmd.getOptionValue("o");
                logOpt = cmd.hasOption("l");
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
            if (!interactive) {
                StringBuilder codeBuilder = new StringBuilder();
                char data[] = new char[65536];
                do {
                    int readSize = read(data);
                    codeBuilder.append(data, 0, readSize);
                    if (readSize < data.length)
                        break;
                } while(true);
                readBuffer = codeBuilder.toString();
            } else {
                if (readBuffer.length() == 0)
                    System.out.print(">>> ");
                else
                    System.out.print("... ");
                String newCode = readLine();
                if (newCode == null)
                    return readBuffer;
                lastLine = newCode;
                readBuffer += lastLine + "\n";
            }
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
        config = new ModeConfig(args);
        InputStream is = (config.inOpt) ? (new FileInputStream(config.inFile)) : (System.in);

        reader = new CodeReader(is);
        symTable = new SymbolTable();
        globalIndex = new StackIndex(0, true);

        InterpretProgramViaStream();
    }

    /* Core Loop */
    static private void InterpretProgramViaStream() throws IOException {
        do {
            // Build Lexer & Parser
            String code = reader.readCode();
            SimpleLexer lexer = new SimpleLexer(new ANTLRInputStream(code));
            SimpleParser parser = new SimpleParser(new CommonTokenStream(lexer));

            // Set Error Listener
            parser.removeErrorListeners();
            parser.addErrorListener(new ErrorListener());

            // Build Parse Tree
            ParseTree tree;
            try {
                tree = parser.prgm();
            } catch (RuntimeException e) {
                if (reader.ready()) continue;
                System.err.println(e.getMessage());
                reader.flushBuffer();
                if (config.inOpt) return;
                else continue;
            }

            // Build AST
            ASTBuilder ab = new ASTBuilder();
            ASTNode prgm = ab.visit(tree);
            if (config.logOpt) prgm.visit(new ASTGraphLog());

            // Scope Validity & Type Consistency
            try {
                prgm.visit(new ScopeChecker(symTable));
                prgm.visit(new TypeChecker(symTable));
                symTable.commit();
                reader.flushBuffer();
            } catch (RuleException e) {
                symTable.clear();
                System.err.printf("%s ...line %d: %s\n",
                        e.errorData, e.localLine + totalLines,
                        e.getMessage());
                reader.flushBuffer();
                if (config.inOpt) return;
                else continue;
            }

            totalLines += code.split("\n").length - 1;

            // IR Code Generation
            IRBuilder irBuilder = new IRBuilder(globalIndex, symTable);
            IRCA prgmChunk = irBuilder.visit(prgm);
            globalIndex = irBuilder.top;

            if (config.logOpt) {
                BufferedWriter bw = new BufferedWriter(new FileWriter("IRCode.log"));
                for (IRStatement stmt : prgmChunk.chunk.statements)
                    bw.write(stmt.toString() + "\n");
                bw.close();
            }

            // Execution on VM
            String[] inst = prgmChunk.chunk.statements.stream()
                    .map(IRStatement::toString)
                    .toArray(String[]::new);
            try {
                SimpleVM.loadInst(inst);
            } catch (SimpleException e) {
                System.err.printf("Line %d, Code: %s\n", e.line, e.code);
                System.err.println(e.getMessage());
                return;
            }
        } while (reader.ready());
    }
}
