import java.util.List;

abstract class TypeObject {
    enum Class {
        Unbound,
        Alias,
        Primitive,
        Void,
    }
    String name;
    abstract Class getClassType();
    abstract String getTypeName();
    abstract boolean writable();
}

class Unbound extends TypeObject {
    @Override
    public Class getClassType() { return Class.Unbound; }
    @Override
    public String getTypeName() { return null; }
    @Override
    public String toString() { return String.format("(unbound:%s)", rawExpr); }
    @Override
    public boolean writable() { return false; }

    public Unbound(String rawExpression) {
        rawExpr = rawExpression;
    }
    private String rawExpr;
}

class Primitive extends TypeObject {
    @Override
    public Class getClassType() { return Class.Primitive; }
    @Override
    public String getTypeName() { return name; }

    Primitive(String typeName) {
        name = typeName;
    }
    @Override
    public String toString() {
        return String.format("%s", name);
    }
    @Override
    public boolean writable() { return true; }
}

class Alias extends TypeObject {
    private TypeObject base;
    @Override
    public Class getClassType() { return Class.Alias; }
    @Override
    public String getTypeName() { return name; }

    Alias(String typeName, TypeObject b) {
        name = typeName;
        base = b;
    }
    public String toString() {
        return String.format("%s(<-%s)", name, base.getTypeName());
    }
    @Override
    public boolean writable() { return true; }
}

class Void extends TypeObject {
    @Override
    public Class getClassType() { return Class.Void; }
    @Override
    public String getTypeName() { return "void"; }
    @Override
    public String toString() { return "void"; }
    @Override
    public boolean writable() { return false; }
}
