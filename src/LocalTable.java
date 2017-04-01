import java.util.*;

class LocalTable {

    class Variable {
        String type;
        int dimension;
        boolean isConstant;

        Variable(String _type, int _dimension, boolean _isConstant) {
            type = _type;
            dimension = _dimension;
            isConstant = _isConstant;
        }

        String getTypeStr() {
            return type + String.join("", Collections.nCopies(dimension, "[]"));
        }
    }

    HashMap<String, Variable> vars;
    private String scpName;
    private int scpDepth;
    private GlobalTable global;
    private LocalTable prev;

    /* Constructor */
    LocalTable(String _name, GlobalTable _global) {
        vars = new HashMap<>();
        scpName = _name;
        scpDepth = 0;
        global = _global;
        prev = null;
    }

    LocalTable(LocalTable _prev) {
        vars = new HashMap<>();
        scpName = _prev.scpName;
        scpDepth = _prev.scpDepth + 1;
        global = _prev.global;
        prev = _prev;
    }

    void print() {
        System.out.printf("< %s[%d] >\n", scpName, scpDepth);
        for (String name : vars.keySet())
            System.out.printf("%-19s %s\n", name, vars.get(name).getTypeStr());
    }

    void declVar(String name, SimpleParser.TypeContext typeCtx) {

        if (vars.get(name) != null || global.types.get(name) != null || global.procs.get(name) != null)
            throw new RuntimeException(name + ": Already defined identifier");

        String type = typeCtx.ID().getText();
        if (global.types.get(type) == null)
            throw new RuntimeException(type + ": Undeclared type");

        vars.put(name, new Variable(
                type,
                (typeCtx.getChildCount() - 1) / 3,
                false));
    }

    void declVar(String name, SimpleParser.PtypeContext typeCtx) {

        if (vars.get(name) != null || global.types.get(name) != null || global.procs.get(name) != null)
            throw new RuntimeException(name + ": Already defined identifier");

        String type = typeCtx.ID().getText();
        if (global.types.get(type) == null)
            throw new RuntimeException(type + ": Undeclared type");

        vars.put(name, new Variable(
                type,
                (typeCtx.getChildCount() - 1) / 2,
                false));
    }

    boolean isTypedID(String name) {
        if (vars.get(name) != null) return true;
        else if (prev == null) return global.isTypedID(name);
        else return prev.isTypedID(name);
    }
}
