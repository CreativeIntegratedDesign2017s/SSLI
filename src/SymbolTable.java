import java.util.*;
import java.util.stream.Collectors;

class SymbolTable {
    interface Symbol {
        boolean isBuiltInSymbol();
    }

    static class VarSymbol implements Symbol {
        ValueType type;

        VarSymbol(ValueType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return type.toString();
        }

        public boolean isBuiltInSymbol() {
            return false;
        }
    }

    static class FuncSymbol implements Symbol {
        FuncSymbol(boolean builtIn) {
            this.builtIn = builtIn;
            overloads = new ArrayList<>();
        }
        FuncSymbol() {
            this.builtIn = false;
            overloads = new ArrayList<>();
        }


        boolean builtIn;
        List<Function> overloads;

        boolean extend(Function ext) {
            if (overloads.isEmpty()) {
                overloads.add(ext);
                return true;
            }

            ValueType rType = overloads.get(0).rType;
            if (!rType.equals(ext.rType))
                return false;   // 리턴이 다른 함수 오버로딩 허용 안함

            for (Function overload : overloads) {
                List<ValueType> params = overload.acceptParams;
                if (params.equals(ext.acceptParams)) {
                    return false;
                }
            }

            overloads.add(ext);
            return true;
        }

        public String toString() {
            // ((int, int[]) -> str
            return (overloads.size() > 1 ? "{\n- [" : "[") + String.join("]\n- [", overloads.stream().map(func ->
            {
                List<String> paramStrings = func.acceptParams.stream().map(ValueType::toString)
                        .collect(Collectors.toList());
                String paramStr = String.join(",", paramStrings);
                return String.format("(%s) -> %s", paramStr, func.rType);
            }).collect(Collectors.toList())) + (overloads.size() > 1 ? "]\n}" : "]");
        }

        public boolean isBuiltInSymbol() {
            return builtIn;
        }
    }



    static class Scope {
        Scope() {
        }
        String getScopeName() {
            return "local";
        }

        private HashMap<String, Symbol> symbolMap = new HashMap<>();

        void put(String symbolName, Symbol symbol) {
            symbolMap.put(symbolName, symbol);
        }

        Symbol get(String symbolName) {
            return symbolMap.get(symbolName);
        }

        void print() {
            System.out.println("---" + getScopeName() + "---");
            symbolMap.forEach((name, symbol) -> {
                if (symbol.isBuiltInSymbol())
                    return;
                System.out.println(String.format("%s: %s", name, symbol));
            });
        }
    }

    static class GlobalScope extends Scope {
        GlobalScope() {
        }
        HashMap<String, Symbol> stagedSymbolMap = new HashMap<>();

        @Override
        void put(String symbolName, Symbol symbol) {
            stagedSymbolMap.put(symbolName, symbol);
        }

        @Override
        Symbol get(String symbolName) {
            Symbol staged = stagedSymbolMap.get(symbolName);
            if (staged != null)
                return staged;
            return super.get(symbolName);
        }

        @Override
        String getScopeName() {
            return "global";
        }

        @Override
        void print() {
            super.print();
            stagedSymbolMap.forEach((name, symbol) ->
                    System.out.println(String.format("%s: %s [staged]", name, symbol)));
        }

        void commit() {
            stagedSymbolMap.forEach(super::put);
            stagedSymbolMap.clear();
        }

        void clear() {
            stagedSymbolMap.clear();
        }
    }

    private Deque<Scope> scopeStack = new ArrayDeque<>();
    private Map<String, SingleType> singleTypes = new HashMap<>();
    private Map<Object, Symbol> ctxSymbolHash = new HashMap<>();
    private Map<Object, Function> ctxFunctionHash = new HashMap<>();

    SymbolTable() {
        Scope gs = new GlobalScope();
        scopeStack.push(gs);

        declareSingleType("void", new VoidType());
        declarePrimitive("str");
        declarePrimitive("int");
        declarePrimitive("bool");
        declareFunction("print", new ValueExpr("void"),
                new ArrayList<ParameterExpr>() {{
                    add(new ParameterExpr("int", 0, false));
                }}, false);
        declareFunction("print", new ValueExpr("void"),
                new ArrayList<ParameterExpr>() {{
                    add(new ParameterExpr("str", 0, false));
                }}, false);
        declareFunction("print", new ValueExpr("void"),
                new ArrayList<ParameterExpr>() {{
                    add(new ParameterExpr("bool", 0, false));
                }}, false);

        class OperatorInfo {
            String name, rType;
            List<String> args;
            public OperatorInfo(String name, String rType, String... arguments) {
                this.name = name;
                this.rType = rType;
                args = Arrays.stream(arguments).collect(Collectors.toList());
            }
        }
        List<OperatorInfo> primitiveOperators = new ArrayList<OperatorInfo>() {
            {
                // int operators
                add(new OperatorInfo("unary_plus", "int", "int"));
                add(new OperatorInfo("unary_minus", "int", "int"));
                add(new OperatorInfo("binary_plus", "int", "int", "int"));
                add(new OperatorInfo("binary_minus", "int", "int", "int"));
                add(new OperatorInfo("binary_div", "int", "int", "int"));
                add(new OperatorInfo("binary_mult", "int", "int", "int"));
                add(new OperatorInfo("binary_pow", "int", "int", "int"));
                add(new OperatorInfo("binary_lessthan", "bool", "int", "int"));
                add(new OperatorInfo("binary_greaterthan", "bool", "int", "int"));
                add(new OperatorInfo("binary_equal", "bool", "int", "int"));
                add(new OperatorInfo("binary_notequal", "bool", "int", "int"));
                add(new OperatorInfo("binary_lessequal", "bool", "int", "int"));
                add(new OperatorInfo("binary_greaterequal", "bool", "int", "int"));

                // bool operators
                add(new OperatorInfo("binary_and", "bool", "bool", "bool"));
                add(new OperatorInfo("binary_or", "bool", "bool", "bool"));
                add(new OperatorInfo("unary_not", "bool", "bool"));
            }
        };
        for (OperatorInfo opInfo : primitiveOperators) {
            declareFunction("@" + opInfo.name, new ValueExpr(opInfo.rType)
                    , new ArrayList<ParameterExpr>() {{
                        for (String paramType : opInfo.args)
                            add(new ParameterExpr(paramType, 0, false));
                    }}, true);
        }

    }

    void enterNewScope() {
        Scope localScope = new Scope();
        scopeStack.push(localScope);
    }

    void leaveScope() {
        Scope last = scopeStack.peek();
        scopeStack.pop();
        if (scopeStack.isEmpty()) {   // 스코프 스택이 비면 어떻게 하는가..
            scopeStack.push(last);
        }
    }

    void commit() {
        Scope current = scopeStack.peek();
        if (!(current instanceof GlobalScope)) {
            throw new RuntimeException("'commit' called from local scope");
        }
        ((GlobalScope) current).commit();
    }

    void clear() {
        while (!scopeStack.isEmpty() && !(scopeStack.peek() instanceof GlobalScope))
            scopeStack.pop();
        if (scopeStack.isEmpty()) {
            throw new RuntimeException("scope stackIndex has broken");
        }
        ((GlobalScope) scopeStack.peek()).clear();
    }

    Symbol getSymbol(String name) {
        for (Scope scope : scopeStack) {
            Symbol sym = scope.get(name);
            if (sym != null)
                return sym;
        }
        return null;
    }

    Symbol getSymbol(Object ctx) {
        return ctxSymbolHash.get(ctx);
    }

    void putSymbol(Object ctx, Symbol symbol) {
        ctxSymbolHash.put(ctx, symbol);
    }

    Function getFunction(ASTNode ctx) {
        return ctxFunctionHash.get(ctx);
    }

    void putFunction(ASTNode ctx, Function f) {
        ctxFunctionHash.put(ctx, f);
    }

    SingleType getSingleType(String name) {
        return singleTypes.get(name);
    }

    private void declareSingleType(String name, SingleType single) {
        SingleType prev = singleTypes.get(name);
        if (prev != null) {
            throw new RuntimeException(String.format("type %s is already registered in %s", name, prev));
        }
        singleTypes.put(name, single);
    }

    private void declarePrimitive(String name) {
        declareSingleType(name, new Primitive(name));
    }

    void declareAlias(String name, String target) {
        TypeObject base = getSingleType(target);
        if (base == null)
            throw new RuntimeException(String.format(
                    "No target type object named by %s is declared for alias %s", target, name
            ));
        declareSingleType(name, new Alias(name, base));
    }

    static class ValueExpr {
        String typeName;
        List<Integer> shape;
        ValueExpr(String tName) {
            this.typeName = tName;
            this.shape = new ArrayList<>();
        }
        ValueExpr(String tName, List<Integer> shape) {
            this.typeName = tName;
            this.shape = shape;
        }
    }
    static class ParameterExpr extends ValueExpr {
        boolean isRef;
        ParameterExpr(String name, int dim, boolean _isRef) {
            super(name, new ArrayList<Integer>(){{
                for (int i = 0; i < dim; ++i) {
                    add(-1);        // 파라미터의 어레이 사이즈는 미리 특정할 수 없음
                }
            }});
            isRef = _isRef;
        }
    }
    ValueType MakeArrayOrSingle(SingleType single, List<Integer> shape) {
        if (shape.size() > 0)
            return new Array(single, shape);
        else
            return single;
    }
    VarSymbol declareVariable(String symbolName, ValueExpr expr)
    {
        SingleType to = getSingleType(expr.typeName);
        if (to == null)
            throw new RuntimeException(String.format(
                    "No target type named by %s is declared for variable %s", expr.typeName, symbolName));

        // 문법에서 걸리기 때문에 void 타입으로 변수를 선언할 수는 없긴함.
        if (!to.writable())
            throw new RuntimeException(String.format(
                    "type %s is not allow to write", to.getTypeName()));

        Scope current = scopeStack.peek();
        Symbol prevDecl = current.get(symbolName);
        if (prevDecl != null) {
            throw new RuntimeException(String.format(
                    "symbol name %s is already defined in this scope", symbolName));
        }
        VarSymbol retSymbol = new VarSymbol(MakeArrayOrSingle(to, expr.shape));
        current.put(symbolName, retSymbol);
        return retSymbol;
    }

    Function declareFunction(String symbolName, ValueExpr rExpr, List<ParameterExpr> parameters, boolean builtIn) {
        SingleType rTo = getSingleType(rExpr.typeName);
        if (rTo == null)
            throw new RuntimeException(String.format(
                    "No target type named by %s is declared for return type of %s", rExpr.typeName, symbolName));

        List<ValueType> paramExpressions = parameters.stream().map(paramExpr -> {
            SingleType pTo = getSingleType(paramExpr.typeName);
            if (pTo == null)
                throw new RuntimeException(String.format(
                        "No target type named by %s is declared for parameter of %s", paramExpr.typeName, symbolName));

            ValueType paramType = MakeArrayOrSingle(pTo, paramExpr.shape);
            if (paramExpr.isRef)
                return new Reference(paramType);
            else
                return paramType;
        }).collect(Collectors.toList());

        Function declFunc = new Function(rTo, paramExpressions);

        Scope current = scopeStack.peek();
        if (!(current instanceof GlobalScope)) {
            throw new RuntimeException("No procedure declaration is available on local scope");
        }
        Symbol prevDecl = current.get(symbolName);
        if (prevDecl != null) {
            if (prevDecl instanceof FuncSymbol) {
                FuncSymbol funcDecl = (FuncSymbol)prevDecl;
                if (!funcDecl.extend(declFunc)) {
                    throw new RuntimeException(String.format(
                            "%s symbol is previously declared by %s type", symbolName, prevDecl
                    ));
                }
                return declFunc;
            } else {
                throw new RuntimeException(
                        String.format("%s is previously defined by %s symbol", symbolName, prevDecl)
                );
            }
        }
        FuncSymbol ret = new FuncSymbol(builtIn) {
            {
                extend(declFunc);
            }
        };
        current.put(symbolName, ret);
        return declFunc;
    }

    void print() {
        System.out.println("---single types---");
        singleTypes.forEach((typeName, typeObject) ->
                System.out.println(typeObject.toString() + ": " + typeObject.getClass().toString()));
        for (Scope scope : scopeStack) {
            scope.print();
        }
    }
}
