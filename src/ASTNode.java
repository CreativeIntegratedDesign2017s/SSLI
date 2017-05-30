import java.util.*;

interface ASTNode {
    <Type> Type visit(ASTListener<Type> al);
}

class ASTPrgm implements ASTNode {
    List<ASTUnit> units = new ArrayList<>();

    @Override public <Type>
    Type visit(ASTListener<Type> al) {
        return al.visitPrgm(this);
    }
}
