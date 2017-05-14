import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

abstract class IRArgument {
}

class StackIndex extends IRArgument {
    int index;
    boolean globalStack;

    StackIndex(int index, boolean global) {
        this.index = index;
        this.globalStack = global;
    }

    @Override
    public String toString() {
        return (globalStack ? "G" : "") + String.valueOf(index);
    }

    StackIndex offset(int offset) {
        return new StackIndex(index + offset, globalStack);
    }
}

class Constant extends IRArgument{
    String value;
    Constant(String sValue) {
        value = sValue;
    }
    Constant(int nValue) {
        value = String.valueOf(nValue);
    }

    @Override
    public String toString() {
        return "$" + value;
    }
}

class RawArg extends IRArgument {
    String arg;
    RawArg(Object o) {
        arg = o.toString();
    }

    @Override
    public String toString() {
        return arg;
    }
}

class IRStatement {
    String stmt;
    IRStatement(String command, IRArgument ... arguments) {
        stmt = command;
        if (arguments.length > 0)
        stmt += " " + String.join(" ",
                Arrays.stream(arguments).map(Object::toString).collect(Collectors.toList()));
    }
    @Override
    public String toString() {
        return stmt;
    }
}

class IRChunk {
    IRChunk() {
    }
    IRChunk(IRStatement one) {
        statements = new ArrayList<IRStatement>(){{
            add(one);
        }};
    }
    int size() {
        return statements.size();
    }
    List<IRStatement> statements;
}

public class IRBuilder extends SimpleParserBaseVisitor<IRChunk> {
    StackIndex top;
    Map<SymbolTable.Symbol, StackIndex> symbolIndex = new HashMap<>();
    SymbolTable symTable;

    IRBuilder(StackIndex globalIndex, SymbolTable symTable) {
        top = globalIndex;
        this.symTable = symTable;
    }

    StackIndex incIndex(int offset) {
        top = top.offset(offset);
        return top;
    }

    @Override
    public IRChunk aggregateResult(IRChunk aggregate, IRChunk nextResult) {
        if (aggregate == null)
            return nextResult;
        if (nextResult == null)
            return aggregate;
        return new IRChunk() {{
            statements = Stream.concat(aggregate.statements.stream(), nextResult.statements.stream()).collect(Collectors.toList());
        }};
    }

    IRChunk aggregateResult(IRChunk ...chunks) {
        return new IRChunk(){{
            statements = Arrays.stream(chunks).filter(Objects::nonNull)
                .map(c -> c.statements)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        }};
    }

    @Override
    public IRChunk visitProcedure(SimpleParser.ProcedureContext ctx) {
        Function fDecl = symTable.getFunction(ctx);
        String funcName = ctx.ID().getText() + fDecl.getDecorator();
        IRChunk functionBegin = new IRChunk() {{
            statements = new ArrayList<IRStatement>() {{
                add(new IRStatement("PROC", new RawArg(funcName)));
            }};
        }};

        StackIndex prevTop = top;
        top = new StackIndex(0, false);

        SimpleParser.Para_listContext paraCtx = ctx.para_list();
        for (int i = 0; i < (paraCtx.getChildCount() + 1) / 3; i++)
        {
            SimpleParser.PtypeContext typeCtx = paraCtx.ptype(i);
            SymbolTable.Symbol paramSymbol = symTable.getSymbol(typeCtx);
            symbolIndex.put(paramSymbol, incIndex(1));
        }

        IRChunk blockChunk = visit(ctx.block());
        int maxLine = blockChunk.statements.size();
        for (int i = 0; i < maxLine; ++i) {
            IRStatement stmt = blockChunk.statements.get(i);
            if (stmt.stmt.equals("TEMP_RET")) {
                if (i < maxLine - 1)
                    blockChunk.statements.set(i, new IRStatement("JMP", new Constant(maxLine - i - 1)));
                else
                    // 마지막 줄이라 하더라도 줄을 지우면 그 위에서 명령줄의 수를 이용해서 작성해놓은 로직이 무너질 수 있음.
                    blockChunk.statements.set(i, new IRStatement("NOP"));
            }
        }

        top = new StackIndex(1, false);

        return aggregateResult(functionBegin, blockChunk, new IRChunk(new IRStatement("RET", top)));
    }

    @Override
    public IRChunk visitDeclare(SimpleParser.DeclareContext ctx) {
        SymbolTable.Symbol v = symTable.getSymbol(ctx);
        symbolIndex.put(v, incIndex(1));
        if (ctx.init().expr() == null) {
            return null;
        }

        IRChunk initializeChunk = visit(ctx.init().expr());
        incIndex(-1);
        IRChunk movChunk = new IRChunk(new IRStatement("MOVE", top, top.offset(1)));

        return aggregateResult(initializeChunk, movChunk);
    }

    @Override
    public IRChunk visitIdentifier(SimpleParser.IdentifierContext ctx) {
        SymbolTable.Symbol s = symTable.getSymbol(ctx);
        if (s instanceof SymbolTable.VarSymbol)
            return new IRChunk(new IRStatement("LEA", incIndex(1), symbolIndex.get(s)));
        else
            throw new RuleException(ctx, "Identifier에 대한 심볼을 찾을 수 없습니다.(" + s.toString() + ")");
    }

    @Override
    public IRChunk visitBoolean(SimpleParser.BooleanContext ctx) {
        return new IRChunk(new IRStatement("LOAD", incIndex(1), new Constant(ctx.getText())));
    }

    @Override
    public IRChunk visitInteger(SimpleParser.IntegerContext ctx) {
        return new IRChunk(new IRStatement("LOAD", incIndex(1), new Constant(ctx.getText())));
    }

    @Override
    public IRChunk visitString(SimpleParser.StringContext ctx) {
        return new IRChunk(new IRStatement("LOAD", incIndex(1), new Constant(ctx.getText())));
    }

    @Override public IRChunk visitAssign(SimpleParser.AssignContext ctx) {
        StackIndex prevTop = top;
        IRChunk dest = visit(ctx.expr(0));
        IRChunk source = visit(ctx.expr(1));

        top = prevTop;
        IRChunk move = new IRChunk(new IRStatement("MOVE", top.offset(1), top.offset(2)));
        return aggregateResult(dest, source, move);
    }
    
    @Override public IRChunk visitIfElse(SimpleParser.IfElseContext ctx) {
        StackIndex prevTop = top;
        IRChunk elsePart = ctx.stmt_list(1) != null ? visit(ctx.stmt_list(1)) : null;
        top = prevTop;
        IRChunk ifPart = visit(ctx.stmt_list(0));
        top = prevTop;

        elsePart = aggregateResult(elsePart, new IRChunk(new IRStatement("JMP", new Constant(ifPart.size()))));

        IRChunk exprChunk = visit(ctx.expr());
        IRChunk testChunk = new IRChunk(new IRStatement("TEST", top, new Constant(elsePart.size())));
        top = prevTop;
        return aggregateResult(exprChunk, testChunk, elsePart, ifPart);
    }
    
    @Override public IRChunk visitDoWhile(SimpleParser.DoWhileContext ctx) {
        StackIndex prevTop = top;
        IRChunk stmtChunk = visit(ctx.stmt_list());
        top = prevTop;
        IRChunk exprChunk = visit(ctx.expr());

        IRChunk testChunk = new IRChunk(
                new IRStatement("TEST", top, new Constant(-(stmtChunk.size() + exprChunk.size() + 1))));
        top = prevTop;
        return aggregateResult(stmtChunk, exprChunk, testChunk);
    }
    
    @Override public IRChunk visitWhileDo(SimpleParser.WhileDoContext ctx) {
        StackIndex prevTop = top;
        IRChunk stmtChunk = visit(ctx.stmt_list());
        top = prevTop;
        IRChunk exprChunk = visit(ctx.expr());

        IRChunk testChunk = new IRChunk(
                new IRStatement("TEST", top, new Constant(-(stmtChunk.size() + exprChunk.size() + 1))));
        top = prevTop;
        return aggregateResult(new IRChunk(new IRStatement("JMP", new Constant(stmtChunk.size()))),
                stmtChunk, exprChunk, testChunk);
    }
    
    @Override public IRChunk visitReturn(SimpleParser.ReturnContext ctx) {
        IRChunk retExprChunk = ctx.expr() != null ? visit(ctx.expr()) : null;
        IRChunk moveRetValueChunk = new IRChunk(
                new IRStatement("MOVE", new StackIndex(1, false), top));
        return aggregateResult(retExprChunk, moveRetValueChunk, new IRChunk(new IRStatement("TEMP_RET")));
    }
    
    @Override public IRChunk visitSubstring(SimpleParser.SubstringContext ctx) {
        // Substring은 언어단에서 지원하는 기능이지만 IRCode에서 바로 지원하지 않고 시스템 콜 함수를 이용할 계획
        // d = substring(a, b, c)와 같이 생각하자
        IRChunk funcChunk = new IRChunk(
                new IRStatement("LOAD", incIndex(1), new Constant("\"substring@str\"")));
        StackIndex prevTop = top;
        IRChunk containerChunk = visit(ctx.Container);
        IRChunk fromChunk = visit(ctx.From);
        IRChunk toChunk = visit(ctx.To);
        IRChunk callChunk = new IRChunk(new IRStatement("CALL", prevTop, new Constant(3)));
        top = prevTop;
        return aggregateResult(funcChunk, containerChunk, fromChunk, toChunk, callChunk);
    }

    IRChunk binaryExpression(String command, SimpleParser.ExprContext operand1, SimpleParser.ExprContext operand2) {
        IRChunk oprnd1Chunk = visit(operand1);
        IRChunk oprnd2Chunk = visit(operand2);
        top = top.offset(-1);
        IRChunk opChunk = new IRChunk(new IRStatement(command, top, top, top.offset(1)));
        return aggregateResult(oprnd1Chunk, oprnd2Chunk, opChunk);
    }

    IRChunk unaryExpression(String command, SimpleParser.ExprContext oprand) {
        IRChunk oprandChunk = visit(oprand);
        IRChunk opChunk = new IRChunk(new IRStatement(command, top, top));
        return aggregateResult(oprandChunk, opChunk);
    }

    private String OperatorTranslator(int opToken) {
        switch(opToken) {
            case SimpleParser.ADD:
                return "ADD";
            case SimpleParser.SUB:
                return "SUB";
            case SimpleParser.DIV:
                return "DIV";
            case SimpleParser.MUL:
                return "MUL";
            case SimpleParser.POW:
                return "POW";
            case SimpleParser.LT:
                return "LT";
            case SimpleParser.GT:
                return "GT";
            case SimpleParser.EQ:
                return "EQ";
            case SimpleParser.NEQ:
                return "NEQ";
            case SimpleParser.LE:
                return "LE";
            case SimpleParser.GE:
                return "GE";
            case SimpleParser.NOT:
                return "NOT";
            case SimpleParser.AND:
                return "AND";
            case SimpleParser.OR:
                return "OR";
            case SimpleParser.AMP:
                return "AMP";
            default:
                return "NOP";
        }
    }
    
    @Override public IRChunk visitOr(SimpleParser.OrContext ctx) {
        return binaryExpression(OperatorTranslator(ctx.OR().getSymbol().getType()), ctx.Oprnd1, ctx.Oprnd2);
    }
    
    @Override public IRChunk visitMulDiv(SimpleParser.MulDivContext ctx) {
        return binaryExpression(OperatorTranslator(ctx.op.getType()), ctx.Oprnd1, ctx.Oprnd2);
    }
    
    @Override public IRChunk visitAddSub(SimpleParser.AddSubContext ctx) {
        return binaryExpression(OperatorTranslator(ctx.op.getType()), ctx.Oprnd1, ctx.Oprnd2);
    }
    
    @Override public IRChunk visitSubscript(SimpleParser.SubscriptContext ctx) {
        return binaryExpression("GETTABLE", ctx.Container, ctx.Indexer);
    }
    
    @Override public IRChunk visitNot(SimpleParser.NotContext ctx) {
        return unaryExpression(OperatorTranslator(ctx.NOT().getSymbol().getType()), ctx.expr());
    }

    @Override public IRChunk visitProcCall(SimpleParser.ProcCallContext ctx) {
        Function f = symTable.getFunction(ctx);
        IRChunk funcChunk = new IRChunk(
                new IRStatement("LOAD", incIndex(1),
                        new Constant("\"" + ctx.ID() + f.getDecorator() + "\"")));
        StackIndex nextTop = top;
        IRChunk parameterChunk = visit(ctx.argu_list());
        IRChunk callChunk = new IRChunk(
                new IRStatement("CALL", nextTop, new Constant(top.index - nextTop.index)));
        top = nextTop;
        return aggregateResult(funcChunk, parameterChunk, callChunk);
    }
    
    @Override public IRChunk visitAnd(SimpleParser.AndContext ctx) {
        return binaryExpression(OperatorTranslator(ctx.AND().getSymbol().getType()), ctx.Oprnd1, ctx.Oprnd2);
    }
    
    @Override public IRChunk visitPow(SimpleParser.PowContext ctx) {
        return binaryExpression(OperatorTranslator(ctx.POW().getSymbol().getType()), ctx.Base, ctx.Exponent);
    }
    
    @Override public IRChunk visitCompare(SimpleParser.CompareContext ctx) {
        return binaryExpression(OperatorTranslator(ctx.op.getType()), ctx.Oprnd1, ctx.Oprnd2);
    }
    
    @Override public IRChunk visitUnaryPM(SimpleParser.UnaryPMContext ctx) {
        if (ctx.op.getType() == SimpleParser.ADD)   // 바이너리 에드를 생각못했넹..
            return visit(ctx.expr());

        return unaryExpression("UMN", ctx.expr());
    }

    @Override public IRChunk visitArgu_list(SimpleParser.Argu_listContext ctx) {
        List<IRChunk> exprs = ctx.expr().stream().map(this::visit).collect(Collectors.toList());
        return aggregateResult(exprs.toArray(new IRChunk[exprs.size()]));
    }
}
