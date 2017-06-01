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

public class IRBuilder extends ASTListener<IRChunk> {
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
    public IRChunk visitProcUnit(ASTProcUnit ctx) {
        Function fDecl = symTable.getFunction(ctx);
        String funcName = ctx.pid.getText() + fDecl.getDecorator();
        IRChunk functionBegin = new IRChunk() {{
            statements = new ArrayList<IRStatement>() {{
                add(new IRStatement("PROC", new RawArg(funcName)));
            }};
        }};

        StackIndex prevTop = top;
        top = new StackIndex(0, false);

        for (ASTProcUnit.ParaType ptype : ctx.type)
        {
            SymbolTable.Symbol paramSymbol = symTable.getSymbol(ptype);
            symbolIndex.put(paramSymbol, incIndex(1));
        }

        IRChunk blockChunk = ctx.stmtList.visit(this);
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

        top = prevTop;

        return aggregateResult(functionBegin, blockChunk, new IRChunk(new IRStatement("RET", new StackIndex(1, false))));
    }

    @Override
    public IRChunk visitStmtList(ASTStmtList ctx) {
        IRChunk base = new IRChunk(){{
                statements = new ArrayList<>();
        }};
        return aggregateResult(base, visitChildren(ctx));
    }

    @Override
    public IRChunk visitDecl(ASTDecl ctx) {
        SymbolTable.Symbol v = symTable.getSymbol(ctx);
        symbolIndex.put(v, incIndex(1));
        if (ctx.init == null) {
            return null;
        }

        IRChunk initializeChunk = ctx.init.visit(this);
        incIndex(-1);
        IRChunk movChunk = new IRChunk(new IRStatement("MOVE", top, top.offset(1)));

        return aggregateResult(initializeChunk, movChunk);
    }

    @Override
    public IRChunk visitVariable(ASTVariable ctx) {
        SymbolTable.Symbol s = symTable.getSymbol(ctx);
        if (s instanceof SymbolTable.VarSymbol)
            return new IRChunk(new IRStatement("LOAD", incIndex(1), symbolIndex.get(s)));
        else
            throw new RuleException(ctx, "Identifier에 대한 심볼을 찾을 수 없습니다.(" + s.toString() + ")");
    }

    @Override
    public IRChunk visitConstant(ASTConstant ctx) {
        return new IRChunk(new IRStatement("LOAD", incIndex(1), new Constant(ctx.token.getText())));
    }

    @Override
    public IRChunk visitAsgn(ASTAsgn ctx) {
        StackIndex prevTop = top;
        IRChunk dest = ctx.lval.visit(this);
        IRChunk source = ctx.rval.visit(this);

        top = prevTop;
        IRChunk move = new IRChunk(new IRStatement("MOVE", top.offset(1), top.offset(2)));
        return aggregateResult(dest, source, move);
    }

    @Override
    public IRChunk visitCond(ASTCond ctx) {
        StackIndex prevTop = top;
        IRChunk elsePart = ctx.elseStmtList != null ? ctx.elseStmtList.visit(this) : null;
        top = prevTop;
        IRChunk ifPart = ctx.thenStmtList.visit(this);
        top = prevTop;

        elsePart = aggregateResult(elsePart, new IRChunk(new IRStatement("JMP", new Constant(ifPart.size()))));

        IRChunk exprChunk = ctx.cond.visit(this);
        IRChunk testChunk = new IRChunk(new IRStatement("TEST", top, new Constant(elsePart.size())));
        top = prevTop;
        return aggregateResult(exprChunk, testChunk, elsePart, ifPart);
    }

    @Override public IRChunk visitUntil(ASTUntil ctx) {
        StackIndex prevTop = top;
        IRChunk stmtChunk = visit(ctx.loop);
        top = prevTop;
        IRChunk exprChunk = visit(ctx.cond);

        IRChunk testChunk = new IRChunk(
                new IRStatement("TEST", top, new Constant(-(stmtChunk.size() + exprChunk.size() + 1))));
        top = prevTop;
        return aggregateResult(stmtChunk, exprChunk, testChunk);
    }

    @Override public IRChunk visitWhile(ASTWhile ctx) {
        StackIndex prevTop = top;
        IRChunk stmtChunk = visit(ctx.loop);
        top = prevTop;
        IRChunk exprChunk = visit(ctx.cond);

        IRChunk testChunk = new IRChunk(
                new IRStatement("TEST", top, new Constant(-(stmtChunk.size() + exprChunk.size() + 1))));
        top = prevTop;
        return aggregateResult(new IRChunk(new IRStatement("JMP", new Constant(stmtChunk.size()))),
                stmtChunk, exprChunk, testChunk);
    }

    @Override public IRChunk visitReturn(ASTReturn ctx) {
        IRChunk retExprChunk = ctx.val != null ? visit(ctx.val) : null;
        IRChunk moveRetValueChunk = new IRChunk(
                new IRStatement("MOVE", new StackIndex(1, false), top));
        return aggregateResult(retExprChunk, moveRetValueChunk, new IRChunk(new IRStatement("TEMP_RET")));
    }

    @Override public IRChunk visitSubstring(ASTSubstring ctx) {
        // Substring은 언어단에서 지원하는 기능이지만 IRCode에서 바로 지원하지 않고 시스템 콜 함수를 이용할 계획
        // d = substring(a, b, c)와 같이 생각하자
        IRChunk funcChunk = new IRChunk(
                new IRStatement("LOAD", incIndex(1), new Constant("\"substring@str\"")));
        StackIndex prevTop = top;
        IRChunk containerChunk = visit(ctx.str);
        IRChunk fromChunk = visit(ctx.index1);
        IRChunk toChunk = visit(ctx.index2);
        IRChunk callChunk = new IRChunk(new IRStatement("CALL", prevTop, new Constant(3)));
        top = prevTop;
        return aggregateResult(funcChunk, containerChunk, fromChunk, toChunk, callChunk);
    }

    @Override
    public IRChunk visitUnary(ASTUnary ctx) {
        IRChunk oprandChunk = visit(ctx.oprnd);
        if (ctx.op.getType() == SimpleParser.ADD)
            return oprandChunk;
        else if (ctx.op.getType() == SimpleParser.SUB) {
            IRChunk opChunk = new IRChunk(new IRStatement("UMN", top, top));
            return aggregateResult(oprandChunk, opChunk);
        } else if (ctx.op.getType() == SimpleParser.NOT) {
            IRChunk opChunk = new IRChunk(new IRStatement("NOT", top, top));
            return aggregateResult(oprandChunk, opChunk);
        } else
            return null;
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

    @Override
    public IRChunk visitBinary(ASTBinary ctx) {
        IRChunk oprnd1Chunk = visit(ctx.oprnd1);
        IRChunk oprnd2Chunk = visit(ctx.oprnd2);
        top = top.offset(-1);
        IRChunk opChunk = new IRChunk(new IRStatement(OperatorTranslator(ctx.op.getType()), top, top, top.offset(1)));
        return aggregateResult(oprnd1Chunk, oprnd2Chunk, opChunk);
    }

    @Override public IRChunk visitProcCall(ASTProcCall ctx) {
        Function f = symTable.getFunction(ctx);
        IRChunk funcChunk = new IRChunk(
                new IRStatement("LOAD", incIndex(1),
                        new Constant("\"" + ctx.pid.getText() + f.getDecorator() + "\"")));
        StackIndex nextTop = top;
        List<IRChunk> exprs = ctx.param.stream().map(this::visit).collect(Collectors.toList());
        IRChunk parameterChunk = aggregateResult(exprs.toArray(new IRChunk[exprs.size()]));
        IRChunk callChunk = new IRChunk(
                new IRStatement("CALL", nextTop, new Constant(top.index - nextTop.index)));
        top = nextTop;
        return aggregateResult(funcChunk, parameterChunk, callChunk);
    }
}
