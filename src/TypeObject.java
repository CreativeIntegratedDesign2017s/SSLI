import java.util.List;

public abstract class TypeObject {
    enum Class {
        Unbound,
        Primitive,
        Enum,
        Alias
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
    String rawExpr;
}

class Primitive extends TypeObject {
    @Override
    public Class getClassType() { return Class.Primitive; }
    @Override
    public String getTypeName() { return name; }

    public Primitive(String typeName) {
        name = typeName;
    }
    @Override
    public String toString() {
        return String.format("%s", name);
    }
}

class Enum extends TypeObject {
    private List<String> candidates;

    @Override
    public Class getClassType() {
        return Class.Enum;
    }

    @Override
    public String getTypeName() {
        return name;
    }

    public Enum(String typeName, List<String> _candidates) {
        name = typeName;
        candidates = _candidates;
    }

    public String toString() {
        return name;
    }
}

class Alias extends TypeObject {
    private TypeObject base;
    @Override
    public Class getClassType() { return Class.Alias; }
    @Override
    public String getTypeName() { return name; }

    public Alias(String typeName, TypeObject b) {
        name = typeName;
        base = b;
    }
    public String toString() {
        return String.format("%s(<-%s)", name, base.getTypeName());
    }
}
