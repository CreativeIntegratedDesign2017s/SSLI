/**
 * Created by HeeM on 2017-03-13.
 * Simply implemented for System.in by Holim on 2017-03-13.
 * Interactive Mode with parentheses-matcher by Holim on 2017-03-19.
 * Bail-out Error Reporting & Scope Checker by Holim on 2017-03-22.
 */

import java.io.*;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.apache.commons.cli.*;

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
        ModeConfiguration mc = ParsingMode(args);

        System.out.println("Welcome to Simple Interpreter!");
        BufferedReader br = mc.QueryInputStream();
        GlobalTable symbols = new GlobalTable();

        while (InterpretProgramViaStream(br, symbols, mc.mode)){}
    }

    private static boolean InterpretProgramViaStream(BufferedReader br, GlobalTable symbols, Mode mode) throws IOException {
        if (mode == Mode.Interactive)
            System.out.print(">>> ");
        String code = br.readLine();
        if (code == null)
            return false;
        code += "\n";

        // Match #.brackets in input codes
        int paren = parenCount(code);
        while (paren > 0) {
            if (mode == Mode.Interactive)
                System.out.print("... ");
            String line = br.readLine();
            if (line == null)
                break;
            code += line + "\n";
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
        catch (Exception e) {
            return true;
        }

        // Check Scope Consistency
        ScopeChecker scpChecker = new ScopeChecker(symbols);
        ParseTreeWalker walker = new ParseTreeWalker();
        try { walker.walk(scpChecker, tree); }
        catch (Exception e) {
            symbols.clear();
            return true;
        }

        // Check Type Consistency
        TypeChecker typeChecker = new TypeChecker(scpChecker.global, scpChecker.scope);
        try { typeChecker.visit(tree); }
        catch (Exception e) {
            symbols.clear();
            return true;
        }

        // Declarations Confirmed
        symbols.commit();
        symbols.print();

        // TODO: Make AST & IR Codes
        System.out.println("Tree: " + tree.toStringTree(parser));

        // TODO: Execute the Code
        return true;
    }

    enum Mode {
        Interactive,
        FileInput
    }

    static class ModeConfiguration {
        Mode mode;
        String inputFile;

        BufferedReader QueryInputStream() {
            switch (mode) {
                case Interactive:
                    return new BufferedReader(new InputStreamReader(System.in));
                case FileInput:
                    try {
                        InputStream is = new FileInputStream(inputFile);
                        return new BufferedReader(new InputStreamReader(is));
                    } catch (java.io.FileNotFoundException e) {
                        return null;
                    }
                default:
                    return null;
            }
        }
    }

    static private ModeConfiguration ParsingMode(String args[]) {
        Options options = new Options();

        Option output = new Option("f", "file", true, "file input");
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return new ModeConfiguration();
        }

        ModeConfiguration mc = new ModeConfiguration();
        mc.mode = cmd.hasOption("file") ? Mode.FileInput : Mode.Interactive;
        if (mc.mode == Mode.FileInput)
            mc.inputFile = cmd.getOptionValue("file");
        return mc;
    }
}
