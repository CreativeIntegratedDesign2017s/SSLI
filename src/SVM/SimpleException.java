package SVM;

public class SimpleException extends RuntimeException {
    public ErrorCode code;
    public String proc;
    public int line;
    public String inst;
    SimpleException(ErrorCode c) { code = c; }
    SimpleException(Exception e, ErrorCode c, String name, int n) {
        super(e);
        code = c;
        proc = name;
        line = n;
    }
}
