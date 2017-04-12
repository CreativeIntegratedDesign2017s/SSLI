import java.util.List;

abstract class TypeObject {
    enum Class {
        Unbound,
        Alias,
        Primitive,
    }
    String name;
    abstract Class getClassType();
    abstract String getTypeName();
}

class Unbound extends TypeObject {
    @Override
    public Class getClassType() { return Class.Unbound; }
    @Override
    public String getTypeName() { return null; }
    @Override
    public String toString() { return String.format("(unbound:%s)", rawExpr); }

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
}
