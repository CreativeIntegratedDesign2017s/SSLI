package SVM;

public class SimpleException extends RuntimeException {
    public ErrorCode code;
    SimpleException(ErrorCode c) { code = c; }
}
