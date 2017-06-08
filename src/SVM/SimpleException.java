package SVM;

public class SimpleException extends RuntimeException {
    public ErrorCode code;
    public int line;
    SimpleException(ErrorCode c) { code = c; }
    SimpleException(Exception e, ErrorCode c, int n) { super(e); code = c; line = n;}
}
