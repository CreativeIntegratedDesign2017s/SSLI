import java.util.*;
import java.util.stream.*;

class GlobalTable {

    class Type {
        boolean isPrimitive;
        List<String> struct;

        Type(Boolean _prime, List<String> _struct) {
            isPrimitive = _prime;
            struct = _struct;
        }
    }

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

    class Parameter {
        String type;
        int dimension;
        boolean isReference; // Arrays must be passed by reference

        Parameter(String _type, int _dimension, boolean _isReference) {
            type = _type;
            dimension = _dimension;
            isReference = _isReference;
        }

        String getTypeStr() {
            if (isReference) return type + "&";
            else return type + String.join("", Collections.nCopies(dimension, "[]"));
        }
    }

    class Procedure { List<Parameter> param; String rtype; }

    private boolean paramListEq(List<Parameter> a, List<Parameter> b) {
        if (a == null) return (b == null);
        else if (b == null) return false;
        else if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).type.equals(b.get(i).type))
                return false;
            if (a.get(i).dimension != b.get(i).dimension)
                return false;
        }
        return true;
    }

    private String paramListStr(List<Parameter> a) {
        return (a == null) ? "void" : a.stream().map(Parameter::getTypeStr).collect(Collectors.joining(","));
    }

    private static void scpException(String msg) {
        System.out.println(msg);
        throw new RuntimeException();
    }

    /* Fields variables */
    HashMap<String,Type> types;
    HashMap<String, Variable> vars;
    HashMap<String, List<Procedure>> procs;

    /* Uncommited declaration lists */
    private List<String> _typeClearList;
    private List<String> _varClearList;
    private List<String> _procClearList;
    private List<List<Parameter>> _paramClearList;

    /* Constructor */
    GlobalTable() {
        types = new HashMap<>();
        vars = new HashMap<>();
        procs = new HashMap<>();
        commit();

        // Default Symbols
        types.put("bool", new Type(true, null));
        types.put("int", new Type(true, null));
        types.put("str", new Type(true, null));

        Procedure printBool = new Procedure(); printBool.param = new LinkedList<>();
        Procedure printInt = new Procedure(); printInt.param = new LinkedList<>();
        Procedure printStr = new Procedure(); printStr.param = new LinkedList<>();
        printBool.param.add(new Parameter("bool", 0, false));
        printInt.param.add(new Parameter("int", 0, false));
        printStr.param.add(new Parameter("str", 0, false));
        printBool.rtype = "void"; printInt.rtype = "void"; printStr.rtype = "void";

        // Support for function overloading
        List<Procedure> printDecl = new LinkedList<>();
        printDecl.add(printBool);
        printDecl.add(printInt);
        printDecl.add(printStr);
        procs.put("print", printDecl);

        Procedure exit = new Procedure();
        exit.param = null;			// 'param' is null when it has no parameters
        exit.rtype = "void";		// 'rtype' cannot be null
        List<Procedure> exitDecl = new LinkedList<>();
        exitDecl.add(exit);
        procs.put("exit", exitDecl);
    }

    void print() {
        System.out.println("< Global Scope >");

        for (String name : types.keySet())
            System.out.printf("%-19s %s\n", name, "type");
        for (String name : procs.keySet())
            for (Procedure proc : procs.get(name))
                System.out.printf("%-19s %s -> %s\n", name, paramListStr(proc.param), proc.rtype);
        for (String name: vars.keySet())
            System.out.printf("%-19s %s\n", name, vars.get(name).getTypeStr());
    }

    void declVar(String name, SimpleParser.TypeContext typeCtx) {

        if (vars.get(name) != null || types.get(name) != null || procs.get(name) != null)
            scpException(name + ": Already defined identifier");

        String type = typeCtx.ID().getText();
        if (types.get(type) == null)
            scpException(type + ": Undeclared type");

        vars.put(name, new Variable(type, (typeCtx.getChildCount() - 1) / 3, false));
        _varClearList.add(name);
    }

    void declEnum(String name, SimpleParser.Enum_listContext enumCtx) {

        if (vars.get(name) != null || types.get(name) != null || procs.get(name) != null)
            scpException(name + ": Already defined identifier");

        for (int i = 0; i < (enumCtx.getChildCount() + 1) / 2; i++) {
            String vid = enumCtx.ID(i).getText();
            if (vars.get(vid) != null || types.get(vid) != null || procs.get(vid) != null)
                scpException(vid + ": Already defined identifier");
        }

        types.put(name, new Type(true, null));
        _typeClearList.add(name);
        for (int i = 0; i < (enumCtx.getChildCount() + 1) / 2; i++) {
            String vid = enumCtx.ID(i).getText();
            vars.put(vid, new Variable(name, 0, true));
            _varClearList.add(vid);
        }
    }

    void declProc(String name, SimpleParser.Para_listContext paraCtx, String rtype) {

        if (vars.get(name) != null || (types.get(name) != null && !rtype.equals(name)))
            scpException(name + ": Already defined identifier");

        if (types.get(rtype) == null && !rtype.equals("void"))
            scpException(rtype + ": Undeclared type");

        Procedure proc = new Procedure();
        proc.rtype = rtype;
        int paraCount = (paraCtx.getChildCount() + 1) / 3;
        if (paraCount != 0) {
            proc.param = new LinkedList<>();
            for (int i = 0; i < paraCount; i++) {
                proc.param.add(new Parameter(
                        paraCtx.ptype(i).ID().getText(),
                        (paraCtx.ptype(i).getChildCount() - 1) / 2,
                        (paraCtx.ptype(i).getChildCount() == 2)));
            }
        }
        else { proc.param = null; }

        List<Procedure> loads = procs.get(name);
        if (loads != null) {
            boolean isDup = false;
            for (Procedure item : loads)
                if (paramListEq(item.param, proc.param))
                    isDup = true;
            if (isDup)
                scpException(name + ": Already defined identifier");
        }
        else {
            loads = new LinkedList<>();
            procs.put(name, loads);
        }

        loads.add(proc);
        _procClearList.add(name);
        _paramClearList.add(proc.param);
    }

    void commit() {
        _varClearList = new LinkedList<>();
        _typeClearList = new LinkedList<>();
        _procClearList = new LinkedList<>();
        _paramClearList = new LinkedList<>();
    }

    void clear() {
        for (String name : _varClearList)
            vars.remove(name);
        for (String name : _typeClearList)
            types.remove(name);
        for (int i = 0; i < _procClearList.size(); i++) {
            List<Procedure> loads = procs.get(_procClearList.get(i));
            for (Procedure proc : loads) {
                if (proc.param == _paramClearList.get(i)) {
                    loads.remove(proc);
                    break;
                }
            }
            if (loads.size() == 0)
                procs.remove(_procClearList.get(i));
        }
        commit();
    }

    boolean isTypedID(String name) {
        return (vars.get(name) != null || procs.get(name) != null);
    }
}
