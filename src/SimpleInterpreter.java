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
    static private ASTBuilder astBuilder;
    static private ASTGraphLog astGrapher;
    static private ScopeChecker scpChecker;
    static private TypeChecker typeChecker;
    static private IRBuilder irBuilder;
    static private StackIndex globalIndex;
    static private IROptimizer optimizer;

    static class ModeConfig {
        boolean inOpt;
        boolean outOpt;
        boolean execOpt;
        String inFile;
        String outFile;
        String execFile;

        ModeConfig(String args[]) {
            Options options = new Options();
            options.addOption(new Option("x", true, "exec file"));
            options.addOption(new Option("f", true, "input file"));
            options.addOption(new Option("o", true, "output file"));
            try {
                CommandLineParser parser = new DefaultParser();
                CommandLine cmd = parser.parse(options, args);
                execOpt = cmd.hasOption("x");
                execFile = cmd.getOptionValue("x");
                inOpt = cmd.hasOption("f");
                inFile = cmd.getOptionValue("f");
                outOpt = cmd.hasOption("o");
                outFile = (inOpt && outOpt) ? cmd.getOptionValue("o") : "IRCode.log";
            }
            catch (ParseException e) {
                System.err.println(e.getMessage());
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("ssli", options);
                System.exit(-1);
            }
        }
    }

    /* Main */
    static public void main(String args[]) throws IOException {
        optimizer = new IROptimizer();
        config = new ModeConfig(args);
        if (config.execOpt) {
            try (BufferedReader br = new BufferedReader(new FileReader(config.execFile))) {
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
            astBuilder = new ASTBuilder();
            astGrapher = new ASTGraphLog("ASTGraph.log");
            scpChecker = new ScopeChecker(symTable);
            typeChecker = new TypeChecker(symTable);
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
            parser.removeErrorListeners();
            parser.addErrorListener(new ErrorListener());

            // Build Abstract Syntax Tree
            ASTNode prgm;
            try {
                ParseTree tree = parser.prgm();
                prgm = astBuilder.visit(tree);
                astGrapher.visit(prgm);
            } catch (RuntimeException e) {
                System.out.println(e.getMessage());
                if (config.inOpt) break;
                else continue;
            }

            // Check Grammar Consistency
            try {
                scpChecker.visit(prgm);
                typeChecker.visit(prgm);
                symTable.commit();
            } catch (RuleException e) {
                symTable.clear();
                System.out.printf("line %d: %s\n",
                        e.localLine + totalLines, e.errorData);
                System.out.println(e.getMessage());
                if (config.inOpt) break;
                else continue;
            }

            // IR Code Generation
            IRCA prgmChunk = irBuilder.visit(prgm);
<<<<<<< HEAD
            IROptimizer optimizer = new IROptimizer(prgmChunk.chunk.statements.toArray(new IRStatement[prgmChunk.chunk.statements.size()]));
            optimizer.doOptimize();
=======
            IRStatement[] statements = prgmChunk.chunk.statements.toArray(
                    new IRStatement[prgmChunk.chunk.statements.size()]);

            globalIndex = irBuilder.top;
>>>>>>> add constant & copy propagation

            String[] irCodes = Arrays.stream(statements)
                    .map(IRStatement::toString)
                    .toArray(String[]::new);

            OutputStream fs = new FileOutputStream(config.outFile);
            fs.write(String.join("\n", irCodes).getBytes());
            fs.close();

            // Execution on VM
            try { SimpleVM.loadInst(irCodes); }
            catch (SimpleException e) {
                System.out.printf("VM Error on Proc %s, Line %d, Code: %s\n",
                        (e.proc == null) ? "" : e.proc, e.line, e.code);
                System.out.println(e.getMessage());
                return;
            }

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

            optimizer.setSource(statements);
            statements = optimizer.doOptimize();

            irCodes = Arrays.stream(statements)
                    .map(IRStatement::toString)
                    .toArray(String[]::new);
            if (config.inOpt && config.outOpt) {
                OutputStream fs = new FileOutputStream(config.outFile);
                fs.write(String.join("\n", irCodes).getBytes());
                fs.close();
            }
            else {
                BufferedWriter bw = new BufferedWriter(new FileWriter("IRCode_opt.log"));
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

            totalLines += code.split("\n").length - 1;

        } while (reader.ready());
    }
}
