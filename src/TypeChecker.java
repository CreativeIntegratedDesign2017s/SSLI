import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

abstract class Expression {

    Expression rankDown() { return null; }
    Expression call(List<Expression> argTypes) { return null; }
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
    CallableExpression(List<Function> types) {
        this.types = types;
    }

    @Override
    Expression call(List<Expression> argTypes) {
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
                return new ValueExpression(f.rType, false, false);
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

    static class TypeException extends RuntimeException {
        String errorData;
        int localLine;
        TypeException(ParserRuleContext ctx, String reason) {
            super(reason);
            errorData = ctx.getText();
            localLine = ctx.start.getLine();
        }
    }

    /* Constructor */
    TypeChecker(SymbolTable _symTable) {
        symTable = _symTable;
    }

    @Override
    public Expression visitProcedure(SimpleParser.ProcedureContext ctx) {
        //enterProcedure
        SymbolTable.ValueExpr rType = new SymbolTable.ValueExpr(ctx.rtype().getText(), 0);
        TerminalNode vNode = ctx.para_list().VOID();
        List<SymbolTable.ParameterExpr> paramTypes = new ArrayList<>();
        if (vNode == null) {
            for (SimpleParser.PtypeContext ptype : ctx.para_list().ptype()) {
                String pTypeName = ptype.ID().getText();
                int dim = (ptype.getChildCount() - 1) / 2;
                boolean isRef = ptype.getChildCount() == 2;
                paramTypes.add(new SymbolTable.ParameterExpr(pTypeName, dim, isRef));
            }
        }

        SymbolTable.FuncSymbol f = symTable.declareFunction(ctx.ID().getText(), rType, paramTypes, false);
        ValueType retType = f.overloads.get(f.overloads.size() - 1).rType;
        symTable.enterNewScope();

        //visit Children
        visit(ctx.para_list());
        ValueExpression retExpr = (ValueExpression)visit(ctx.block());
        if (retExpr == null)
            retExpr = new ValueExpression(new Void(), false, true);
        if (!retExpr.getBase().equals(retType))
            throw new TypeException(ctx,
                    String.format("Procedure %s's return type is not match with contents (%s<->%s)",
                            ctx.ID().getText(), retType, retExpr.getBase()));

        //exitProcedure
        symTable.print();
        symTable.leaveScope();

        return null;
    }

    private Expression MergeExpression(ParserRuleContext ctx, List<Expression> exprs) {
        Expression retExpr = null;
        for (Expression expr : exprs) {
            if (retExpr == null)
                retExpr = expr;
            else if (retExpr != expr) {
                throw new TypeException(ctx, String.format("return type mismatches (%s<->%s)", retExpr, expr));
            }
        }
        return retExpr;
    }

    @Override
    public Expression visitPara_list(SimpleParser.Para_listContext ctx) {
        for (int i = 0; i < (ctx.getChildCount() + 1) / 3; i++)
        {
            SimpleParser.PtypeContext typeCtx = ctx.ptype(i);
            symTable.declareVariable(ctx.ID(i).getText(),
                    new SymbolTable.ValueExpr(typeCtx.ID().getText(), (typeCtx.getChildCount() - 1) / 2));
        }

        return null;
    }

    @Override
    public Expression visitDeclare(SimpleParser.DeclareContext ctx) {
        SimpleParser.TypeContext typeCtx = ctx.type();
        SymbolTable.VarSymbol v = symTable.declareVariable(ctx.ID().getText(),
                new SymbolTable.ValueExpr(typeCtx.ID().getText(), (typeCtx.getChildCount() - 1) / 3));

        if (ctx.init().expr() != null) {
            ValueExpression initExpr = (ValueExpression) visit(ctx.init().expr());
            if (!v.type.equals(initExpr.getBase())) {
                throw new TypeException(ctx,
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
            throw new TypeException(ctx,String.format("%s expression is not lvalue", ctx.expr(0).getText()));
        }

        if (!assignee.acceptable(assignor)) {
            throw new TypeException(ctx,String.format("Assign type mismatches (%s <-> %s", assignee, assignor));
        }

        return null;
    }

    @Override
    public Expression visitIfElse(SimpleParser.IfElseContext ctx) {
        Expression conditionExpr = visit(ctx.expr());
        if (!conditionExpr.isBoolean()) {
            throw new TypeException(ctx,
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
            throw new TypeException(ctx,
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
            throw new TypeException(ctx,
                    String.format("Condition expression must be boolean (%s:%s)", ctx.expr().getText(), conditionExpr));
        }

        return visit(ctx.stmt_list());
    }

    @Override
    public Expression visitStmt_list(SimpleParser.Stmt_listContext ctx) {
        symTable.enterNewScope();

        Expression result = MergeExpression(ctx, ctx.stmt().stream()
                .map(this::visit)
                .collect(Collectors.toList()));

        symTable.print();
        symTable.leaveScope();
        return result;
    }

    @Override
    public Expression visitBlock(SimpleParser.BlockContext ctx) {
        return visit(ctx.stmt_list());
    }

    @Override
    public Expression visitIdentifier(SimpleParser.IdentifierContext ctx) {
        SymbolTable.Symbol symbol = symTable.getSymbol(ctx.getText());
        if (symbol == null)
            throw new TypeException(ctx,"No symbol has found with " + ctx.getText());

        if (symbol instanceof SymbolTable.VarSymbol) {
            SymbolTable.VarSymbol v = (SymbolTable.VarSymbol) symbol;
            return new ValueExpression(v.type, true, false);
        }
        else if (symbol instanceof SymbolTable.FuncSymbol) {
            SymbolTable.FuncSymbol f = (SymbolTable.FuncSymbol) symbol;
            return new CallableExpression(f.overloads);
        }

        throw new TypeException(ctx,"Undefined symbol" + ctx.getText());
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
        Expression funcExpression = visit(ctx.expr());
        if (!(funcExpression instanceof CallableExpression)) {
            throw new TypeException(ctx,String.format("%s is not callable", ctx.expr().getText()));
        }

        List<Expression> arguments = ctx.argu_list().expr().stream()
                .map(this::visit)
                .collect(Collectors.toList());

        Expression rE = funcExpression.call(arguments);
        if (rE == null) {
            throw new TypeException(ctx,
                    String.format("No parameter set is matched with %s, (Callable: %s, Arguments: %s)",
                            ctx.expr().getText(), funcExpression, arguments));
        }
        return rE;
    }

    @Override
    public Expression visitSubscript(SimpleParser.SubscriptContext ctx) {
        Expression containerExpr = visit(ctx.Container);
        Expression indexerExpr = visit(ctx.Indexer);
        if (!indexerExpr.isNumeric()) {
            throw new TypeException(ctx,String.format("Non integer subscript is not support (%s)", ctx.Indexer.getText()));
        }
        Expression rankDownExpr = containerExpr.rankDown();
        if (rankDownExpr == null) {
            throw new TypeException(ctx,String.format("Can't subscript for this expression (%s)", ctx.Container.getText()));
        }
        return rankDownExpr;
    }

    @Override
    public Expression visitSubstring(SimpleParser.SubstringContext ctx) {
        Expression containerExpr = visit(ctx.Container);
        Expression fromExpr = visit(ctx.From);
        Expression toExpr = visit(ctx.To);

        if (!containerExpr.isLiteral()) {
            throw new TypeException(ctx,String.format("Only string expression can be divided (%s)", ctx.Container.getText()));
        }

        if (!fromExpr.isNumeric() || toExpr.isNumeric()) {
            throw new TypeException(ctx,String.format("Indexing expression must be numeric (%s, %s)", ctx.From.getText(), ctx.To.getText()));
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
                throw new TypeException(ctx,String.format("Not supporting operator token %d", opToken));
        }
    }

    private Expression operatorRedirection(ParserRuleContext pCtx, String operatorName, SimpleParser.ExprContext... operandCtxs) {
        List<ValueExpression> opArgs = new ArrayList<>();
        for (SimpleParser.ExprContext ctx : operandCtxs) {
            Expression expr = visit(ctx);

            if (!(expr instanceof ValueExpression)) {
                throw new TypeException(ctx,"Function cannot associated with operator (" + ctx.getText() + ")");
            }
            opArgs.add((ValueExpression) expr);
        }

        String opSymbolName = String.format("@%s", operatorName);
        SymbolTable.FuncSymbol opSymbol = (SymbolTable.FuncSymbol)symTable.getSymbol(opSymbolName);
        if (opSymbol == null) {
            List<String> argTypeList = opArgs.stream()
                    .map(v -> v.getBase().getTypeName())
                    .collect(Collectors.toList());
            throw new TypeException(pCtx,
                    String.format("No operator has found with %s for (%s)", operatorName, String.join(", ", argTypeList)));
        }


        // 임시로 콜라블 익스프레션을 생성해서 호출
        CallableExpression temp = new CallableExpression(opSymbol.overloads);
        Expression ret = temp.call(opArgs.stream().map(o -> (Expression)o).collect(Collectors.toList()));
        if (ret == null)
        {
            List<String> argTypeList = opArgs.stream()
                    .map(v -> v.getBase().getTypeName())
                    .collect(Collectors.toList());
            throw new TypeException(pCtx,
                    String.format("No operator has found with %s for (%s)", operatorName, String.join(", ", argTypeList)));
        }
        return ret;
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
