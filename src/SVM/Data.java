package SVM;

enum DataType { Bool, Int, Str }

class Data {
    DataType type;

    static Data valueOf(String str) {
        char c = str.charAt(1);
        if (Character.isDigit(c) || c == '-')
            return new Int(Integer.parseInt(str.substring(1)));
        if (str.equals("$true"))
            return new Bool(true);
        if (str.equals("$false"))
            return new Bool(false);
        if (c == '"' && str.charAt(str.length() - 1) == '"') {
            String v = str.substring(2, str.length() - 1);
            v = v.replaceAll("\\\\n", "\\\n").replaceAll("\\\\t", "\\\t").replaceAll("\\\\r", "\\\r");
            return new Str(v);
        }
        throw new IllegalArgumentException();
    }

    static Data copy(Data data) {
        if (data.type == DataType.Int)
            return new Int(((Int)data).v);
        if (data.type == DataType.Str)
            return new Str(((Str)data).v);
        if (data.type == DataType.Bool)
            return new Bool(((Bool)data).v);
        return null;
    }

    static void vchg(Data dst, Data src) {
        if (dst.type == DataType.Int)
            ((Int)dst).v = ((Int)src).v;
        else if (dst.type == DataType.Str)
            ((Str)dst).v = ((Str)src).v;
        else if (dst.type == DataType.Bool)
            ((Bool)dst).v = ((Bool)dst).v;
    }
}

class Bool extends Data {
    boolean v;
    Bool(boolean b) { type = DataType.Bool; v = b; }
    static Bool not(Bool x) { return new Bool(!x.v); }
    static Bool and(Bool x, Bool y) { return new Bool(x.v && y.v); }
    static Bool or(Bool x, Bool y) { return new Bool(x.v || y.v); }
    @Override public String toString() { return "$" + (v ? "true" : "false"); }
}

class Int extends Data {
    int v;
    Int(int i) { type = DataType.Int; v = i; }
    static Int umn(Int x) { return new Int(-x.v); }
    static Int add(Int x, Int y) { return new Int(x.v + y.v); }
    static Int sub(Int x, Int y) { return new Int(x.v - y.v); }
    static Int mul(Int x, Int y) { return new Int(x.v * y.v); }
    static Int div(Int x, Int y) {
        if (y.v == 0) throw new SimpleException(ErrorCode.DivideZero);
        return new Int(x.v / y.v);
    }
    static Int pow(Int x, Int y) {
        if (x.v == 0 && y.v == 0) throw new SimpleException(ErrorCode.ZeroToZero);
        return new Int((int) Math.pow(x.v, y.v));
    }
    static Bool eq(Int x, Int y) { return new Bool(x.v == y.v); }
    static Bool ne(Int x, Int y) { return new Bool(x.v != y.v); }
    static Bool lt(Int x, Int y) { return new Bool(x.v < y.v); }
    static Bool le(Int x, Int y) { return new Bool(x.v <= y.v); }
    static Bool gt(Int x, Int y) { return new Bool(x.v > y.v); }
    static Bool ge(Int x, Int y) { return new Bool(x.v >= y.v); }
    @Override public String toString() { return "$" + String.valueOf(v); }
}

class Str extends Data {
    String v;
    Str(String s) { type = DataType.Str; v = s; }
    static Str concat(Str x, Str y) { return new Str(x.v + y.v); }
    static Str substr(Str x, Int beg, Int end) { return new Str(x.v.substring(beg.v, end.v)); }
    @Override public String toString() { return "$\"" + v + "\""; }
}
