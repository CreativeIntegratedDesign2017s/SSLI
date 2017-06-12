import ANTLR.*;
import AST.*;
import SVM.*;
import java.io.*;
import java.util.*;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.apache.commons.cli.*;

public class SimpleInterpreter {
    static private int totalLines;
    static private ModeConfig config;
    static private CodeReader reader;
    static private SymbolTable symTable;
    static private IRBuilder irBuilder;

    static class ModeConfig {
        boolean inOpt;
        boolean outOpt;
        boolean execOpt;
        String inFile;
        String outFile;
        String execFile;

        ModeConfig(String args[]) {
            Options options = new Options();
            options.addOption(new Option("f", true, "input file"));
            options.addOption(new Option("o", true, "output file"));
            options.addOption(new Option("x", true, "exec file"));
            try {
                CommandLineParser parser = new DefaultParser();
                CommandLine cmd = parser.parse(options, args);
                inOpt = cmd.hasOption("f");
                inFile = cmd.getOptionValue("f");
                outOpt = cmd.hasOption("o");
                outFile = cmd.getOptionValue("o");
                execOpt = cmd.hasOption("x");
                execFile = cmd.getOptionValue("x");
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

        private String readEntireFile() throws IOException {
            StringBuilder codeBuilder = new StringBuilder();
            char data[] = new char[65536];
            do {
                int readSize = read(data);
                codeBuilder.append(data, 0, readSize);
                if (readSize < data.length)
                    break;
            } while(true);
            return codeBuilder.toString();
        }

        private int cntParen(String code) {
            int cnt = 0;
            SimpleLexer lexer = new SimpleLexer(new ANTLRInputStream(code));
            for (Token token : lexer.getAllTokens()) {
                switch (token.getType()) {
                    case SimpleLexer.LRB:
                    case SimpleLexer.LSB:
                    case SimpleLexer.LCB:
                    case SimpleLexer.IF:
                    case SimpleLexer.PROC:
                        cnt += 2;
                        break;
                    case SimpleLexer.DO:
                    case SimpleLexer.WHILE:
                        cnt += 1;
                        break;
                    case SimpleLexer.RRB:
                    case SimpleLexer.RSB:
                    case SimpleLexer.RCB:
                    case SimpleLexer.END:
                        cnt -= 2;
                        break;
                }
            }
            return cnt;
        }

        String readCode() throws IOException {
            if (!interactive)
                return readEntireFile();

            System.out.print(">>> ");
            StringBuilder code = new StringBuilder(readLine());
            int matcher = cntParen(code.toString());
            while (matcher > 0) {
                System.out.print("... ");
                String line = readLine();
                code.append("\n").append(line);
                matcher += cntParen(line);
            }
            return code.toString();
        }
    }

    /* Main */
    static public void main(String args[]) throws IOException {
        config = new ModeConfig(args);
        if (config.execOpt) {
            try(BufferedReader br = new BufferedReader(new FileReader(config.execFile))) {
                List<String> list = new ArrayList<>();
                String line = br.readLine();
                while (line != null) {
                    list.add(line);
                    line = br.readLine();
                }
                SimpleVM.loadInst(list.toArray(new String[list.size()]));
            }
        }
        else {
            InputStream is = (config.inOpt) ? (new FileInputStream(config.inFile)) : (System.in);
            reader = new CodeReader(is);
            symTable = new SymbolTable();
            irBuilder = new IRBuilder(new StackIndex(0, true), symTable);
            InterpretProgramViaStream();
        }
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
                System.err.println(e.getMessage());
                if (config.inOpt) break;
                else continue;
            }

            // Build AST
            ASTBuilder ab = new ASTBuilder();
            ASTGraphLog gv = new ASTGraphLog("ASTGraph.log");
            ASTNode prgm = ab.visit(tree);
            gv.visit(prgm);

            // Scope Validity & Type Consistency
            try {
                prgm.visit(new ScopeChecker(symTable));
                prgm.visit(new TypeChecker(symTable));
                symTable.commit();
            } catch (RuleException e) {
                symTable.clear();
                System.err.printf("%s ...line %d: %s\n",
                        e.errorData, e.localLine + totalLines,
                        e.getMessage());
                if (config.inOpt) break;
                else continue;
            }

            totalLines += code.split("\n").length - 1;

            // IR Code Generation
            IRCA prgmChunk = irBuilder.visit(prgm);

            String[] irCodes = prgmChunk.chunk.statements.stream()
                    .map(IRStatement::toString)
                    .toArray(String[]::new);
            if (config.inOpt && config.outOpt) {
                OutputStream fs = new FileOutputStream(config.outFile);
                fs.write(String.join("\n", irCodes).getBytes());
                fs.close();
            }
            else {
                BufferedWriter bw = new BufferedWriter(new FileWriter("IRCode.log"));
                bw.write(String.join("\n", irCodes));
                bw.close();

                try {
                    SimpleVM.loadInst(irCodes);
                } catch (SimpleException e) {
                    System.err.printf("Proc %s, Line %d, Code: %s\n",
                            (e.proc == null) ? "" : e.proc,
                            e.line,
                            e.code);
                    System.err.println(e.getMessage());
                    return;
                }
            }
        } while (true);
    }
}
