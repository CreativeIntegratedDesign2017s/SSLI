import java.io.*;
import org.antlr.v4.runtime.*;
import ANTLR.SimpleLexer;

class CodeReader extends BufferedReader {
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

        StringBuilder code = new StringBuilder();
        while (code.length() == 0) {
            System.out.print(">>> ");
            code.append(readLine());
        }
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
