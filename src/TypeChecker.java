import ANTLR.SimpleParser;
import AST.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

abstract class Expression {

    Expression rankDown() { return null; }
    boolean isNumeric() { return false; }
    boolean isLiteral() { return false; }
    boolean isBoolean() { return false; }
    boolean isLValue() { return false; }
    Expression getRValue() {return this;}
    boolean acceptable(Expression e) {return false;}
}

class ValueExpression extends Expression {
    private ValueType base;
    private boolean lValue;
    private boolean constant;

    ValueExpression(ValueType base, boolean lValue, boolean constant) {
        this.base = base;
        this.lValue = lValue;
        this.constant = constant;
    }

    ValueType getBase() {
        return base;
    }

    @Override
    boolean isLValue() {
        return lValue;
    }

    @Override
    Expression getRValue() {
        if (lValue)
            return new ValueExpression(base, false, constant);
        else
            return this;
    }

    @Override
    public String toString() {
        return (constant ? "const " : "") + base.getTypeName();
    }

    @Override
    boolean acceptable(Expression e) {
        ValueExpression ve = (ValueExpression) e;
        if (ve == null)
            return false;
        if (!lValue)
            return false;
        if (base instanceof Reference) {
            Reference ref = (Reference) base;
            if (!ve.isLValue())
                return false;

            if (ref.refTarget.equals(ve.base)) {
                return !ve.isConstant();
            }
        }
        return base.equals(ve.base);
    }

    @Override
    Expression rankDown() {
        TypeObject rd = base.rankDown();
        if (rd != null && rd instanceof ValueType)
            return new ValueExpression((ValueType)rd, lValue, constant);
        else
            return null;
    }

    @Override
    boolean isNumeric() {
        return base.getTypeName().equals("int");    // Alias친 타입을 numeric으로 취급할 것인가...?
    }

    @Override
    boolean isLiteral() {
        return base.getTypeName().equals("str");
    }

    @Override
    boolean isBoolean() {
        return base.getTypeName().equals("bool");
    }

    boolean isConstant() {
        return constant;
    }
}

class CallableExpression extends Expression {
    private List<Function> types;
    CallableExpression(SymbolTable.FuncSymbol fSymbol) {
        this.types = fSymbol.overloads;
    }

    Function callTest(List<Expression> argTypes) {
        for (Expression e : argTypes) {
            if (!(e instanceof ValueExpression))
                // 함수 인자에 함수 못씀
                return null;
        }

        for (Function f : types) {
            boolean pass = true;
            Iterator<Expression> iter = argTypes.iterator();
            for (ValueType parameter : f.acceptParams) {
                if (!iter.hasNext()) {
                    pass = false;
                    break;
                }
                Expression testExpr = iter.next();
                ValueExpression paramExpr = new ValueExpression(parameter, true, false);
                if (!paramExpr.acceptable(testExpr)) {
                    pass = false;
                    break;
                }
            }
            if (pass) {
                return f;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return types.toString();
    }
}

class TypeChecker extends ASTListener<Expression> {
    private SymbolTable symTable;

    /* Constructor */
    TypeChecker(SymbolTable _symTable) {
        symTable = _symTable;
    }

    @Override
    public Expression visitProcUnit(ASTProcUnit ctx) {
        Function f = symTable.getFunction(ctx);

        ValueExpression retExpr = (ValueExpression)ctx.stmtList.visit(this);
        if (retExpr == null)
            retExpr = new ValueExpression(new VoidType(), false, true);
        f.setReturnType((SingleType)retExpr.getBase());
        return null;
    }

    private Expression MergeExpression(ASTNode ctx, List<Expression> exprs) {
        Expression retExpr = null;
        for (Expression expr : exprs) {
            if (retExpr == null)
                retExpr = expr;
            else if (retExpr.equals(expr)) {
                throw new RuleException(ctx, String.format("return type mismatches (%s<->%s)", retExpr, expr));
            }
        }
        return retExpr;
    }

    @Override
    public Expression visitDecl(ASTDecl ctx) {
        SymbolTable.VarSymbol v = (SymbolTable.VarSymbol) symTable.getSymbol(ctx);

        if (v.type instanceof Array) {
            Array at = (Array) v.type;
            if (at.base.getTypeName() != "int") {
                throw new RuleException(ctx,
                        String.format("Static Array Declaration only supports 'int' type"));
            }
        }

        if (ctx.init != null) {
            ValueExpression initExpr = (ValueExpression) ctx.init.visit(this);
            if (!v.type.equals(initExpr.getBase())) {
                throw new RuleException(ctx,
                        String.format("Variable initialize type mismatches (%s <-> %s)", v.type, initExpr));
            }
        }

        return null;
    }

    @Override
    public Expression visitAsgn(ASTAsgn ctx) {
        Expression assignee = ctx.lval.visit(this);
        Expression assignor = ctx.rval.visit(this);

        if (!assignee.isLValue()) {
            throw new RuleException(ctx,String.format("%s expression is not lvalue", ctx.lval.getText()));
        }

        if (!assignee.acceptable(assignor)) {
            throw new RuleException(ctx,String.format("Assign type mismatches (%s <-> %s", assignee, assignor));
        }

        return null;
    }

    @Override
    public Expression visitCond(ASTCond ctx) {
        Expression conditionExpr = ctx.cond.visit(this);
        if (!conditionExpr.isBoolean()) {
            throw new RuleException(ctx,
                    String.format("Condition expression must be boolean (%s:%s)", ctx.cond.getText(), conditionExpr));
        }
        List<ASTStmtList> stmtList = new ArrayList<ASTStmtList>(){{
                add(ctx.thenStmtList);
                if (ctx.elseStmtList != null)
                    add(ctx.elseStmtList);
        }};
        return MergeExpression(ctx, stmtList.stream()
                .map(sl -> sl.visit(this))
                .collect(Collectors.toList()));
    }

    @Override
    public Expression visitUntil(ASTUntil ctx) {
        Expression conditionExpr = ctx.cond.visit(this);
        if (!conditionExpr.isBoolean()) {
            throw new RuleException(ctx,
                    String.format("Condition expression must be boolean (%s:%s)", ctx.cond.getText(), conditionExpr));
        }

        return ctx.loop.visit(this);
    }

    @Override
    public Expression visitReturn(ASTReturn ctx) {
        Expression retExpr;
        if (ctx.val == null) {
            retExpr = new ValueExpression(new VoidType(), false, true);
        } else {
            retExpr = ctx.val.visit(this);
        }
        return retExpr.getRValue();
    }

    @Override
    public Expression visitWhile(ASTWhile ctx) {
        Expression conditionExpr = ctx.cond.visit(this);
        if (!conditionExpr.isBoolean()) {
            throw new RuleException(ctx,
                    String.format("Condition expression must be boolean (%s:%s)", ctx.cond.getText(), conditionExpr));
        }

        return ctx.loop.visit(this);
    }

    @Override
    public Expression visitStmtList(ASTStmtList ctx) {
        return MergeExpression(ctx, ctx.list.stream()
                .map(n -> n.visit(this))
                .collect(Collectors.toList()));
    }

    @Override
    public Expression visitVariable(ASTVariable ctx) {
        SymbolTable.Symbol symbol = symTable.getSymbol(ctx);

        if (symbol instanceof SymbolTable.VarSymbol) {
            SymbolTable.VarSymbol v = (SymbolTable.VarSymbol) symbol;
            return new ValueExpression(v.type, true, false);
        }
        else if (symbol instanceof SymbolTable.FuncSymbol) {
            SymbolTable.FuncSymbol f = (SymbolTable.FuncSymbol) symbol;
            return new CallableExpression(f);
        }

        throw new RuleException(ctx,"Undefined symbol" + ctx.getText());
    }

    @Override
    public Expression visitConstant(ASTConstant ctx){
        String primitiveName;
        switch(ctx.type) {
            case Boolean:
                primitiveName = "bool";
                break;
            case Integer:
                primitiveName = "int";
                break;
            default:
            case String:
                primitiveName = "str";
        }
        SingleType uExpr = symTable.getSingleType(primitiveName);
        return new ValueExpression(uExpr, false, true);
    }

    @Override
    public Expression visitProcCall(ASTProcCall ctx) {
        SymbolTable.FuncSymbol fSymbol = (SymbolTable.FuncSymbol) symTable.getSymbol(ctx.pid.getText());

        if (fSymbol == null) {
            throw new RuleException(ctx, String.format("Can't found the function symbol with %s", ctx.pid.getText()));
        }

        List<Expression> arguments = ctx.param.stream()
                .map(n -> n.visit(this))
                .collect(Collectors.toList());

        CallableExpression funcExpression = new CallableExpression(fSymbol);
        Function f = funcExpression.callTest(arguments);
        if (f == null) {
            throw new RuleException(ctx,
                    String.format("No parameter set is matched with %s, (Callable: %s, Arguments: %s)",
                            ctx.pid.getText(), funcExpression, arguments));
        }
        symTable.putFunction(ctx, f);
        return new ValueExpression(f.rType, false, false);
    }

    @Override
    public Expression visitSubscript(ASTSubscript ctx) {
        Expression containerExpr = ctx.arr.visit(this);
        Expression indexerExpr = ctx.index.visit(this);
        if (!indexerExpr.isNumeric()) {
            throw new RuleException(ctx,String.format("Non integer subscript is not support (%s)", ctx.index.getText()));
        }
        Expression rankDownExpr = containerExpr.rankDown();
        if (rankDownExpr == null) {
            throw new RuleException(ctx,String.format("Can't subscript for this expression (%s)", ctx.arr.getText()));
        }
        return rankDownExpr;
    }

    @Override
    public Expression visitSubstring(ASTSubstring ctx) {
        Expression containerExpr = ctx.str.visit(this);
        Expression fromExpr = ctx.index1.visit(this);
        Expression toExpr = ctx.index2.visit(this);

        if (!containerExpr.isLiteral()) {
            throw new RuleException(ctx,String.format("Only string expression can be divided (%s)", ctx.str.getText()));
        }

        if (!fromExpr.isNumeric() || toExpr.isNumeric()) {
            throw new RuleException(ctx,String.format("Indexing expression must be numeric (%s, %s)", ctx.index1.getText(), ctx.index2.getText()));
        }

        return containerExpr.getRValue();
    }

    private String OperatorTranslator(ASTNode ctx, int opToken) {
        switch(opToken) {
            case SimpleParser.ADD:
                return "plus";
            case SimpleParser.SUB:
                return "minus";
            case SimpleParser.DIV:
                return "div";
            case SimpleParser.MUL:
                return "mult";
            case SimpleParser.POW:
                return "binary_pow";
            case SimpleParser.LT:
                return "lessthan";
            case SimpleParser.GT:
                return "greaterthan";
            case SimpleParser.EQ:
                return "equal";
            case SimpleParser.NEQ:
                return "notequal";
            case SimpleParser.LE:
                return "lessequal";
            case SimpleParser.GE:
                return "greaterequal";
            case SimpleParser.NOT:
                return "not";
            case SimpleParser.AND:
                return "and";
            case SimpleParser.OR:
                return "or";
            case SimpleParser.AMP:
                return "amp";
            default:
                throw new RuleException(ctx,String.format("Not supporting operator token %d", opToken));
        }
    }

    private Expression operatorRedirection(ASTNode pCtx, String operatorName, ASTExpr... operandCtxs) {
        List<ValueExpression> opArgs = new ArrayList<>();
        for (ASTExpr ctx : operandCtxs) {
            Expression expr = ctx.visit(this);

            if (!(expr instanceof ValueExpression)) {
                throw new RuleException(ctx,"Function cannot associated with operator (" + ctx.getText() + ")");
            }
            opArgs.add((ValueExpression) expr);
        }

        String opSymbolName = String.format("@%s", operatorName);
        SymbolTable.FuncSymbol opSymbol = (SymbolTable.FuncSymbol)symTable.getSymbol(opSymbolName);
        if (opSymbol == null) {
            List<String> argTypeList = opArgs.stream()
                    .map(v -> v.getBase().getTypeName())
                    .collect(Collectors.toList());
            throw new RuleException(pCtx,
                    String.format("No operator has found with %s for (%s)", operatorName, String.join(", ", argTypeList)));
        }


        // 임시로 콜라블 익스프레션을 생성해서 호출
        CallableExpression temp = new CallableExpression(opSymbol);
        Function ret = temp.callTest(opArgs.stream().map(o -> (Expression)o).collect(Collectors.toList()));
        if (ret == null)
        {
            List<String> argTypeList = opArgs.stream()
                    .map(v -> v.getBase().getTypeName())
                    .collect(Collectors.toList());
            throw new RuleException(pCtx,
                    String.format("No operator has found with %s for (%s)", operatorName, String.join(", ", argTypeList)));
        }
        return new ValueExpression(ret.rType, false, false);
    }

    @Override
    public Expression visitUnary(ASTUnary ctx) {
        String operatorName = "unary_" + OperatorTranslator(ctx, ctx.op.getType());
        return operatorRedirection(ctx, operatorName, ctx.oprnd);
    }

    @Override
    public Expression visitBinary(ASTBinary ctx) {
        return operatorRedirection(ctx, "binary_" + OperatorTranslator(ctx, ctx.op.getType()), ctx.oprnd1, ctx.oprnd2);
    }
}
