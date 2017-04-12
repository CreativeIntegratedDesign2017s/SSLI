import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

class SymbolTable {
    static class Expression {
        TypeObject base;
        int dim;
        boolean isRef;

        Expression(TypeObject _base, int dimension) {
            base = _base;
            dim = dimension;
            isRef = false;
        }
        Expression(TypeObject _base, int dimension, boolean isReference) {
            base = _base;
            dim = dimension;
            isRef = isReference;
        }

        @Override
        public String toString() {
            return base.toString() + String.join("", Collections.nCopies(dim, "[]")) + (isRef ? "&" : "");
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;

            if (o.getClass() == getClass()) {
                // 같은 Expression만 비교가능
                Expression expr = (Expression)o;
                return base == expr.base && dim == expr.dim && isRef == expr.isRef;
            }
            return false;
        }
    }
    public interface Symbol {
        Expression getExpression();
        boolean extend(Symbol s);
    }

    static class Variable implements Symbol {
        Expression expr;
        Variable(Expression e) {
            expr = e;
        }
        public Expression getExpression() {
            return expr;
        }
        public boolean extend(Symbol ext) {
            // not support
            return false;
        }
        public String toString() {
            return expr.toString();
        }
    }
    static class Procedure implements Symbol {
        Procedure(Expression rType, List<Expression> parameterTypes) {
            returnType = rType;
            possibleParams = new ArrayList<>();
            possibleParams.add(parameterTypes);
        }

        private Expression returnType;
        private List<List<Expression>> possibleParams;

        public Expression getExpression() {
            return returnType;
        }
        public boolean extend(Symbol ext) {
            Procedure ps;
            try {
                ps = (Procedure)ext;
            }
            catch(ClassCastException e) {
                // 프로시저 이외에는 확장금지
                return false;
            }
            if (!returnType.equals(ps.returnType))
                return false;   // 리턴이 다른 함수 오버로딩 허용 안함

            List<Expression> newParams = ps.possibleParams.get(0);  // Extend에 들어오는 프로시저 심볼은 단 하나의 파라미터 셋을 보유해야함
            for (List<Expression> params : possibleParams) {
                if (params.equals(newParams)) {
                    return false;
                }
            }

            possibleParams.add(newParams);
            return true;
        }
        public String toString() {
            // ((int, int[]) -> str
            return (possibleParams.size() > 1 ? "{\n- [" : "[") + String.join("]\n- [", possibleParams.stream().map(params ->
            {
                List<String> paramStrings = params.stream().map(Expression::toString)
                        .collect(Collectors.toList());
                String paramStr = String.join(",", paramStrings);
                return String.format("(%s) -> %s", paramStr, returnType);
            }).collect(Collectors.toList())) + (possibleParams.size() > 1 ? "]\n}" : "]");
        }
    }

    static class Scope {
        String getScopeName() { return "local"; }
        private HashMap<String, Symbol> symbolMap = new HashMap<>();

        void put(String symbolName, Symbol symbol) {
            symbolMap.put(symbolName, symbol);
        }
        Symbol get(String symbolName) {
            return symbolMap.get(symbolName);
        }
        void print() {
            System.out.println("---" + getScopeName() + "---");
            symbolMap.forEach((name, symbol) -> System.out.println(String.format("%s: %s", name, symbol)));
        }
    }
    static class GlobalScope extends Scope {
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
        String getScopeName() { return "global"; }
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

    private Stack<Scope> scopeStack = new Stack<>();
    private Map<String, TypeObject> typeMap = new HashMap<>();

    SymbolTable() {
        Scope gs = new GlobalScope();
        scopeStack.push(gs);

        declarePrimitive("void");
        declarePrimitive("str");
        declarePrimitive("int");
        declarePrimitive("bool");
    }

    void enterNewScope() {
        Scope localScope = new Scope();
        scopeStack.push(localScope);
    }

    void leaveScope() {
        Scope last = scopeStack.peek();
        scopeStack.pop();
        if (scopeStack.empty()) {   // 스코프 스택이 비면 어떻게 하는가..
            scopeStack.push(last);
        }
    }

    void commit() {
        Scope current = scopeStack.peek();
        if (!(current instanceof GlobalScope)) {
            throw new RuntimeException("'commit' called from local scope");
        }
        ((GlobalScope)current).commit();
    }
    void clear() {
        Scope current = scopeStack.peek();
        if (!(current instanceof GlobalScope)) {
            throw new RuntimeException("'cancel' called from local scope");
        }
        ((GlobalScope)current).clear();
    }

    private TypeObject getTypeObject(String name) {
        return typeMap.get(name);
    }

    private void declareType(TypeObject to) {
        typeMap.put(to.getTypeName(), to);
    }

    private void declarePrimitive(String name) {
        declareType(new Primitive(name));
    }

    void declareAlias(String name, String target) {
        TypeObject base = getTypeObject(target);
        if (base == null)
            throw new RuntimeException(String.format(
                    "No target type object named by %s is declared for alias %s", target, name
            ));
        declareType(new Alias(name, base));
    }

    private void declareSymbol(String symbolName, Symbol s){
        Scope current = scopeStack.peek();
        Symbol prevDecl = current.get(symbolName);
        if (prevDecl != null) {
            if (!prevDecl.extend(s)) {
                throw new RuntimeException(String.format(
                        "%s symbol is previously declared by %s type", symbolName, prevDecl
                ));
            }
            return;
        }

        current.put(symbolName, s);
    }

    static class ValueExpr {
        String typeName;
        int dimension;
        ValueExpr(String tName, int dim) {
            typeName = tName;
            dimension = dim;
        }
    }
    static class ParameterExpr extends ValueExpr {
        boolean isRef;
        ParameterExpr(String name, int dim, boolean _isRef) {
            super(name, dim);
            isRef = _isRef;
        }
    }
    void declareVariable(String symbolName, ValueExpr expr)
    {
        TypeObject to = getTypeObject(expr.typeName);
        if (to == null)
            throw new RuntimeException(String.format(
                    "No target type named by %s is declared for variable %s", expr.typeName, symbolName));

        declareSymbol(symbolName, new Variable(new Expression(to, expr.dimension)));
    }

    void declareProcedure(String symbolName, ValueExpr rExpr, List<ParameterExpr> parameters) {
        TypeObject rTo = getTypeObject(rExpr.typeName);
        if (rTo == null)
            throw new RuntimeException(String.format(
                    "No target type named by %s is declared for return type of %s", rExpr.typeName, symbolName));

        List<Expression> paramExpressions = parameters.stream().map(paramExpr -> {
            TypeObject pTo = getTypeObject(paramExpr.typeName);
            if (pTo == null)
                throw new RuntimeException(String.format(
                        "No target type named by %s is declared for parameter of %s", paramExpr.typeName, symbolName));
            return new Expression(pTo, paramExpr.dimension, paramExpr.isRef);
        }).collect(Collectors.toList());

        declareSymbol(symbolName, new Procedure(new Expression(rTo, rExpr.dimension), paramExpressions));
    }

    void print() {
        System.out.println("---types---");
        typeMap.forEach((typeName, typeObject) ->
                System.out.println(typeObject.toString() + ": " + typeObject.getClassType().toString()));
        for (Scope scope : scopeStack) {
            scope.print();
        }
    }
}
