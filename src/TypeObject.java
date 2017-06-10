import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

abstract class TypeObject {
    abstract String getTypeName();      // 타입을 바로 표현할 수 있는 문자열 (int, void, str, bool, (int, int)->void)
    boolean writable() { return true; }
    TypeObject rankDown() { /*throw new RuntimeException(String.format("%s type is not support rank donw", this));*/
        return null;
    }
    List<Integer> getShape() { return new ArrayList<>(); }
    @Override
    public boolean equals(Object o) {
        return o.getClass().equals(getClass());
    }
}

abstract class ValueType extends TypeObject {
}

abstract class SingleType extends ValueType {
    String name;

    SingleType(String typeName) {
        name = typeName;
    }

    @Override
    public String getTypeName() { return name; }
    @Override
    public String toString() {
        return String.format("%s", name);
    }
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && name.equals(((SingleType) o).name);
    }
}

class Primitive extends SingleType {
    Primitive(String name) {
        super(name);
    }
}

class Alias extends SingleType {
    private TypeObject base;
    @Override
    public String getTypeName() { return name; }

    Alias(String typeName, TypeObject b) {
        super(typeName);
        base = b;
    }
    public String toString() {
        return String.format("%s(<-%s)", name, base.getTypeName());
    }
}

class Array extends ValueType {
    SingleType base;
    private List<Integer> shape;

    Array (SingleType base, List<Integer> shape) {
        if (shape.size() <= 0)
            throw new RuntimeException("only positive dimension array is available!");

        this.base = base;
        this.shape = shape;
    }

    @Override
    public String getTypeName() {
        return base.getTypeName() + String.join("", Collections.nCopies(shape.size(), "[]"));
    }
    @Override
    public String toString() { return String.format("%d shape array of %s", shape.size(), base); }
    @Override
    public TypeObject rankDown() {
        if (shape.size() == 1) {
            return base;
        } else {
            List<Integer> downList = new ArrayList<>(shape);
            downList.remove(0);
            return new Array(base, downList);
        }
    }
    @Override
    public List<Integer> getShape() {
        return shape;
    }
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && ((Array)o).base.equals(base) && ((Array)o).shape.size() == shape.size();
    }
}

class Reference extends ValueType {
    ValueType refTarget;
    Reference(ValueType refTarget) {
        this.refTarget = refTarget;
    }
    @Override
    String getTypeName() {
        return refTarget.getTypeName() + "&";
    }
    @Override
    public String toString() {
        return "reference of " + refTarget.toString();
    }
    @Override
    public boolean equals(Object o) {
        return super.equals(o) && refTarget.equals(((Reference)o).refTarget);
    }
}

class Function extends TypeObject {
    SingleType rType;   // 현시점에서는 SingleType 리턴으로 충분함
    List<ValueType> acceptParams;

    Function(SingleType rType, List<ValueType> acceptParams) {
        this.rType = rType;
        this.acceptParams = acceptParams;
    }

    public void setReturnType(SingleType rType) {
        this.rType = rType;
    }

    @Override
    public String getTypeName() {
        return "[" +
                String.join(",", acceptParams.stream().map(ValueType::getTypeName).collect(Collectors.toList()))
                + "] -> " + rType.getTypeName();
    }

    @Override
    public String toString() {
        return getTypeName();
    }

    public String getDecorator() {
        if (acceptParams.size() > 0)
            return "@" + String.join("@",
                    acceptParams.stream().map(ValueType::getTypeName).collect(Collectors.toList()));
        else
            return "";
    }
}

class VoidType extends SingleType {
    VoidType() {
        super("void");
    }
    public boolean writable() { return false; }
}