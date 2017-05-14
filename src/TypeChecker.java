import org.antlr.v4.runtime.ParserRuleContext;

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

class TypeChecker extends SimpleParserBaseVisitor<Expression> {
    private SymbolTable symTable;

    /* Constructor */
    TypeChecker(SymbolTable _symTable) {
        symTable = _symTable;
    }

    @Override
    public Expression visitProcedure(SimpleParser.ProcedureContext ctx) {
        SymbolTable.FuncSymbol f = (SymbolTable.FuncSymbol) symTable.getSymbol(ctx.ID().getText());
        ValueType retType = f.overloads.get(f.overloads.size() - 1).rType;

        ValueExpression retExpr = (ValueExpression)visit(ctx.block());
        if (retExpr == null)
            retExpr = new ValueExpression(new Void(), false, true);
        if (!retExpr.getBase().equals(retType))
            throw new RuleException(ctx,
                    String.format("Procedure %s's return type is not match with contents (%s<->%s)",
                            ctx.ID().getText(), retType, retExpr.getBase()));
        return null;
    }

    private Expression MergeExpression(ParserRuleContext ctx, List<Expression> exprs) {
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
    public Expression visitDeclare(SimpleParser.DeclareContext ctx) {
        SymbolTable.VarSymbol v = (SymbolTable.VarSymbol) symTable.getSymbol(ctx);

        if (ctx.init().expr() != null) {
            ValueExpression initExpr = (ValueExpression) visit(ctx.init().expr());
            if (!v.type.equals(initExpr.getBase())) {
                throw new RuleException(ctx,
                        String.format("Variable initialize type mismatches (%s <-> %s)", v.type, initExpr));
            }
        }

        return null;
    }

    @Override
    public Expression visitAssign(SimpleParser.AssignContext ctx) {
        Expression assignee = visit(ctx.expr(0));
        Expression assignor = visit(ctx.expr(1));

        if (!assignee.isLValue()) {
            throw new RuleException(ctx,String.format("%s expression is not lvalue", ctx.expr(0).getText()));
        }

        if (!assignee.acceptable(assignor)) {
            throw new RuleException(ctx,String.format("Assign type mismatches (%s <-> %s", assignee, assignor));
        }

        return null;
    }

    @Override
    public Expression visitIfElse(SimpleParser.IfElseContext ctx) {
        Expression conditionExpr = visit(ctx.expr());
        if (!conditionExpr.isBoolean()) {
            throw new RuleException(ctx,
                    String.format("Condition expression must be boolean (%s:%s)", ctx.expr().getText(), conditionExpr));
        }

        return MergeExpression(ctx, ctx.stmt_list().stream()
                .map(this::visit)
                .collect(Collectors.toList()));
    }

    @Override
    public Expression visitDoWhile(SimpleParser.DoWhileContext ctx) {
        Expression conditionExpr = visit(ctx.expr());
        if (!conditionExpr.isBoolean()) {
            throw new RuleException(ctx,
                    String.format("Condition expression must be boolean (%s:%s)", ctx.expr().getText(), conditionExpr));
        }

        return visit(ctx.stmt_list());
    }

    @Override
    public Expression visitReturn(SimpleParser.ReturnContext ctx) {
        Expression retExpr;
        if (ctx.isEmpty()) {
            retExpr = new ValueExpression(new Void(), false, true);
        } else {
            retExpr = visit(ctx.expr());
        }
        return retExpr.getRValue();
    }

    @Override
    public Expression visitWhileDo(SimpleParser.WhileDoContext ctx) {
        Expression conditionExpr = visit(ctx.expr());
        if (!conditionExpr.isBoolean()) {
            throw new RuleException(ctx,
                    String.format("Condition expression must be boolean (%s:%s)", ctx.expr().getText(), conditionExpr));
        }

        return visit(ctx.stmt_list());
    }

    @Override
    public Expression visitStmt_list(SimpleParser.Stmt_listContext ctx) {
        return MergeExpression(ctx, ctx.stmt().stream()
                .map(this::visit)
                .collect(Collectors.toList()));
    }

    @Override
    public Expression visitBlock(SimpleParser.BlockContext ctx) {
        return visit(ctx.stmt_list());
    }

    @Override
    public Expression visitIdentifier(SimpleParser.IdentifierContext ctx) {
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

    private Expression visitPrimitive(String primitiveName) {
        SingleType uExpr = symTable.getSingleType(primitiveName);
        return new ValueExpression(uExpr, false, true);
    }

    @Override
    public Expression visitBoolean(SimpleParser.BooleanContext ctx){
        return visitPrimitive("bool");
    }

    @Override
    public Expression visitInteger(SimpleParser.IntegerContext ctx) {
        return visitPrimitive("int");
    }

    @Override
    public Expression visitString(SimpleParser.StringContext ctx) {
        return visitPrimitive("str");
    }

    @Override
    public Expression visitProcCall(SimpleParser.ProcCallContext ctx) {
        SymbolTable.FuncSymbol fSymbol = (SymbolTable.FuncSymbol) symTable.getSymbol(ctx.ID().getText());

        List<Expression> arguments = ctx.argu_list().expr().stream()
                .map(this::visit)
                .collect(Collectors.toList());

        CallableExpression funcExpression = new CallableExpression(fSymbol);
        Function f = funcExpression.callTest(arguments);
        if (f == null) {
            throw new RuleException(ctx,
                    String.format("No parameter set is matched with %s, (Callable: %s, Arguments: %s)",
                            ctx.ID().getText(), funcExpression, arguments));
        }
        symTable.putFunction(ctx, f);
        return new ValueExpression(f.rType, false, false);
    }

    @Override
    public Expression visitSubscript(SimpleParser.SubscriptContext ctx) {
        Expression containerExpr = visit(ctx.Container);
        Expression indexerExpr = visit(ctx.Indexer);
        if (!indexerExpr.isNumeric()) {
            throw new RuleException(ctx,String.format("Non integer subscript is not support (%s)", ctx.Indexer.getText()));
        }
        Expression rankDownExpr = containerExpr.rankDown();
        if (rankDownExpr == null) {
            throw new RuleException(ctx,String.format("Can't subscript for this expression (%s)", ctx.Container.getText()));
        }
        return rankDownExpr;
    }

    @Override
    public Expression visitSubstring(SimpleParser.SubstringContext ctx) {
        Expression containerExpr = visit(ctx.Container);
        Expression fromExpr = visit(ctx.From);
        Expression toExpr = visit(ctx.To);

        if (!containerExpr.isLiteral()) {
            throw new RuleException(ctx,String.format("Only string expression can be divided (%s)", ctx.Container.getText()));
        }

        if (!fromExpr.isNumeric() || toExpr.isNumeric()) {
            throw new RuleException(ctx,String.format("Indexing expression must be numeric (%s, %s)", ctx.From.getText(), ctx.To.getText()));
        }

        return containerExpr.getRValue();
    }

    private String OperatorTranslator(ParserRuleContext ctx, int opToken) {
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
                return "pow";
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

    private Expression operatorRedirection(ParserRuleContext pCtx, String operatorName, SimpleParser.ExprContext... operandCtxs) {
        List<ValueExpression> opArgs = new ArrayList<>();
        for (SimpleParser.ExprContext ctx : operandCtxs) {
            Expression expr = visit(ctx);

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
    public Expression visitUnaryPM(SimpleParser.UnaryPMContext ctx) {
        String operatorName = "unary_" + OperatorTranslator(ctx, ctx.op.getType());
        return operatorRedirection(ctx, operatorName, ctx.expr());
    }

    @Override
    public Expression visitPow(SimpleParser.PowContext ctx) {
        return operatorRedirection(ctx, "binary_pow", ctx.Base, ctx.Exponent);
    }

    @Override
    public Expression visitMulDiv(SimpleParser.MulDivContext ctx) {
        String opName = "binary_" + OperatorTranslator(ctx, ctx.op.getType());
        return operatorRedirection(ctx, opName, ctx.Oprnd1, ctx.Oprnd2);
    }

    @Override
    public Expression visitAddSub(SimpleParser.AddSubContext ctx) {
        String opName = "binary_" + OperatorTranslator(ctx, ctx.op.getType());
        return operatorRedirection(ctx, opName, ctx.Oprnd1, ctx.Oprnd2);
    }

    @Override
    public Expression visitCompare(SimpleParser.CompareContext ctx) {
        String opName = "cmp_" + OperatorTranslator(ctx, ctx.op.getType());
        return operatorRedirection(ctx, opName, ctx.Oprnd1, ctx.Oprnd2);
    }

    @Override
    public Expression visitNot(SimpleParser.NotContext ctx) {
        return operatorRedirection(ctx, "logical_not", ctx.expr());
    }

    @Override
    public Expression visitAnd(SimpleParser.AndContext ctx) {
        return operatorRedirection(ctx, "logical_and", ctx.Oprnd1, ctx.Oprnd2);
    }

    @Override
    public Expression visitOr(SimpleParser.OrContext ctx) {
        return operatorRedirection(ctx, "logical_or", ctx.Oprnd1, ctx.Oprnd2);
    }

    @Override
    public Expression visitBracket(SimpleParser.BracketContext ctx) {
        return visit(ctx.expr()).getRValue();
    }
}
