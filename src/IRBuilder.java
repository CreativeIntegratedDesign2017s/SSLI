import ANTLR.SimpleParser;
import AST.*;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof StackIndex)) {
            return false;
        }
        StackIndex si = (StackIndex)o;
        return si.globalStack == globalStack && si.index == index;
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Constant))
            return false;
        Constant c = (Constant) o;
        return c.value.equals(value);
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof RawArg))
            return false;
        RawArg c = (RawArg) o;
        return c.arg.equals(arg);
    }
}

class IRStatement {
    String command;
    IRArgument[] arguments;
    IRStatement(String command, IRArgument ... arguments) {
        this.arguments = arguments;
        this.command = command;
    }
    @Override
    public String toString() {
        String stmt = command;
        if (arguments.length > 0)
        {
            for (IRArgument arg : arguments) {
                if (arg == null)
                    throw new RuleException("??", 0, String.format("IRArgument is null (command: %s)", command));
            }
            stmt += " " + String.join(" ",
                    Arrays.stream(arguments).map(Object::toString).collect(Collectors.toList()));
        }
        return stmt;
    }
}

class IRChunk {
    IRChunk() {
    }
    IRChunk(IRStatement ...stmts) {
        statements = Arrays.stream(stmts).collect(Collectors.toList());
    }
    int size() {
        return statements.size();
    }
    List<IRStatement> statements;
}

class IRCA {
    IRArgument argument;
    IRChunk chunk;
    IRCA(IRChunk chunk, IRArgument arg) {
        this.chunk = chunk;
        this.argument = arg;
    }
    IRCA(IRChunk chunk) {
        this.chunk = chunk;
        this.argument = null;
    }
    IRCA(IRArgument arg) {
        this.argument = arg;
        this.chunk = null;
    }
}

public class IRBuilder extends ASTListener<IRCA> {
    StackIndex top;
    Map<SymbolTable.Symbol, StackIndex> symbolIndex = new HashMap<>();
    SymbolTable symTable;
    StackIndex returnIndex;

    IRBuilder(StackIndex globalIndex, SymbolTable symTable) {
        top = globalIndex;
        this.symTable = symTable;
    }

    StackIndex incIndex(int offset) {
        top = top.offset(offset);
        return top;
    }

    @Override
    public IRCA aggregateResult(IRCA aggregate, IRCA nextResult) {
        IRChunk aggChunk = aggregate != null ? aggregate.chunk : null;
        IRChunk nextChunk = nextResult != null ? nextResult.chunk : null;
        IRChunk aggResultChunk;
        if (aggChunk == null)
            aggResultChunk = nextChunk;
        else if (nextChunk == null)
            aggResultChunk = aggChunk;
        else {
            aggResultChunk = new IRChunk(){{
                statements = Stream.concat(aggChunk.statements.stream(), nextChunk.statements.stream())
                        .collect(Collectors.toList());
            }};
        }

        IRArgument argument = nextResult != null ? nextResult.argument : null;

        return new IRCA(aggResultChunk, argument);
    }

    IRCA aggregateResult(IRCA ...results) {
        if (results.length == 0)
            return null;
        else
            return new IRCA(new IRChunk() {{
                statements = Arrays.stream(results).filter(Objects::nonNull)
                        .map(c -> c.chunk)
                        .filter(Objects::nonNull)
                        .map(c -> c.statements)
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
            }}, results[results.length - 1].argument);
    }

    IRCA visitWithIncIndex(ASTNode n, boolean ref) {
        StackIndex target = top.offset(1);
        IRCA irca = visit(n);
        if (irca.argument.equals(target))
            return irca;
        else
        {
            top = target;
            IRStatement copyingCommand;
            if (ref || irca.argument instanceof Constant)
                copyingCommand = new IRStatement("LOAD", target, irca.argument);
            else
                copyingCommand = new IRStatement("COPY", target, irca.argument);

            return aggregateResult(irca,
                    new IRCA(new IRChunk(copyingCommand), top));
        }
    }

    @Override
    public IRCA visitProcUnit(ASTProcUnit ctx) {
        Function fDecl = symTable.getFunction(ctx);
        String funcName = ctx.pid.getText() + fDecl.getDecorator();

        StackIndex prevTop = top;
        top = new StackIndex(0, false);

        for (ASTProcUnit.ParaType ptype : ctx.type)
        {
            SymbolTable.Symbol paramSymbol = symTable.getSymbol(ptype);
            symbolIndex.put(paramSymbol, incIndex(1));
        }

        IRStatement retStmt = null;
        if (fDecl.rType.getTypeName().equals("void")) {
            returnIndex = null;
            retStmt = new IRStatement("RET");
        }
        else {
            returnIndex = top.offset(1);  // 인자를 다 할당하고 난 후의 처음 인덱스. 즉 로컬변수의 첫번째 인덱스인데 이녀석을 리턴 인덱스로 이용한다.
            retStmt = new IRStatement("RET", returnIndex);
        }


        IRCA blockChunk = ctx.stmtList.visit(this);
        int maxLine = blockChunk.chunk.statements.size();

        top = prevTop;

        IRCA functionBegin = new IRCA(new IRChunk() {{
            statements = new ArrayList<IRStatement>() {{
                add(new IRStatement("PROC",
                        new RawArg(funcName),
                        new RawArg(String.valueOf(blockChunk.chunk.statements.size() + 1))));
            }};
        }});

        return aggregateResult(functionBegin, blockChunk, new IRCA(new IRChunk(retStmt)));
    }

    @Override
    public IRCA visitStmtList(ASTStmtList ctx) {
        IRCA base = new IRCA(new IRChunk(){{
                statements = new ArrayList<>();
        }});
        return aggregateResult(base, visitChildren(ctx));
    }

    @Override
    public IRCA visitDecl(ASTDecl ctx) {
        SymbolTable.Symbol s = symTable.getSymbol(ctx);
        symbolIndex.put(s, incIndex(1));
        StackIndex target = top;

        IRCA stackInitializer = null;
        SymbolTable.VarSymbol vs = (SymbolTable.VarSymbol)s;
        List<Integer> shape = vs.type.getShape();
        if (shape.size() > 0)
        {
            stackInitializer = new IRCA(new IRChunk(){{
                statements = new ArrayList<>();
                // need table creation
                for(int s : shape) {
                    statements.add(new IRStatement("LOAD", incIndex(1), new Constant(s)));
                }
                statements.add(
                        new IRStatement("NEWTABLE", target, target.offset(1), new Constant(shape.size())));
            }});
        }

        IRCA initializeChunk = null;
        if (ctx.init != null) {
            initializeChunk = ctx.init.visit(this);
            if (initializeChunk.argument instanceof Constant) {
                stackInitializer = null;        // 초기화가
                initializeChunk = aggregateResult(initializeChunk,
                        new IRCA(new IRChunk(new IRStatement("LOAD", target, initializeChunk.argument))));
            } else if (ctx.init instanceof ASTBinary || ctx.init instanceof ASTUnary) {
                IRStatement lastStmt = initializeChunk.chunk.statements.get(initializeChunk.chunk.statements.size() - 1);
                lastStmt.arguments[0] = target;
            } else
                initializeChunk = aggregateResult(initializeChunk,
                        new IRCA(new IRChunk(new IRStatement("COPY", target, initializeChunk.argument))));
        }
        else if (shape.size() == 0)
            initializeChunk = new IRCA(new IRChunk(
                    new IRStatement("LOAD", target, new Constant(vs.type.getDefaultValue()))));

        return aggregateResult(stackInitializer, initializeChunk);
    }

    @Override
    public IRCA visitVariable(ASTVariable ctx) {
        SymbolTable.Symbol s = symTable.getSymbol(ctx);
        if (s instanceof SymbolTable.VarSymbol)
            return new IRCA(symbolIndex.get(s));
        else
            throw new RuleException(ctx, "Identifier에 대한 심볼 인덱스를 찾을 수 없습니다.(" + s.toString() + ")");
    }

    @Override
    public IRCA visitConstant(ASTConstant ctx) {
        return new IRCA(new Constant(ctx.token.getText()));
    }

    @Override
    public IRCA visitAsgn(ASTAsgn ctx) {
        StackIndex prevTop = top;
        IRCA dest = ctx.lval.visit(this);
        IRCA source = ctx.rval.visit(this);
        top = prevTop;

        if (ctx.lval instanceof ASTVariable && (ctx.rval instanceof ASTBinary || ctx.rval instanceof ASTUnary)) {
            IRStatement lastStmt = source.chunk.statements.get(source.chunk.statements.size() - 1);
            lastStmt.arguments[0] = dest.argument;
            return source;
        }

        IRCA move = new IRCA(new IRChunk(new IRStatement("MOVE", dest.argument, source.argument)));
        return aggregateResult(dest, source, move);
    }

    @Override
    public IRCA visitCond(ASTCond ctx) {
        StackIndex prevTop = top;
        IRCA elsePart = ctx.elseStmtList != null ? ctx.elseStmtList.visit(this) : null;
        IRCA ifPart = ctx.thenStmtList.visit(this);

        elsePart = aggregateResult(elsePart, new IRCA(
                new IRChunk(new IRStatement("JMP", new Constant(ifPart.chunk.size())))));

        IRCA condIrca = ctx.cond.visit(this);
        IRCA testChunk = new IRCA(
                new IRChunk(new IRStatement("TEST", condIrca.argument, new Constant(elsePart.chunk.size()))));
        return aggregateResult(condIrca, testChunk, elsePart, ifPart);
    }

    @Override public IRCA visitUntil(ASTUntil ctx) {
        StackIndex prevTop = top;
        IRCA stmtChunk = visit(ctx.loop);
        IRCA condIrca = visit(ctx.cond);

        IRCA testChunk = new IRCA(new IRChunk(
                new IRStatement("TEST", condIrca.argument, new Constant(-(stmtChunk.chunk.size() + condIrca.chunk.size() + 1)))));
        return aggregateResult(stmtChunk, condIrca, testChunk);
    }

    @Override public IRCA visitWhile(ASTWhile ctx) {
        StackIndex prevTop = top;
        IRCA stmtChunk = visit(ctx.loop);
        IRCA condIrca = visit(ctx.cond);

        IRCA testChunk = new IRCA(new IRChunk(
                new IRStatement("TEST", condIrca.argument,
                        new Constant(-(stmtChunk.chunk.size() + condIrca.chunk.size() + 1)))));
        return aggregateResult(new IRCA(new IRChunk(new IRStatement("JMP", new Constant(stmtChunk.chunk.size())))),
                stmtChunk, condIrca, testChunk);
    }

    @Override public IRCA visitReturn(ASTReturn ctx) {
        IRCA retExprChunk = null;
        if (ctx.val != null) {
            retExprChunk = visit(ctx.val);
        }
        if (retExprChunk != null)
            return aggregateResult(retExprChunk,
                    new IRCA(new IRChunk(new IRStatement("RET", retExprChunk.argument))));
        else
            return new IRCA(new IRChunk(new IRStatement("RET")));
    }

    @Override public IRCA visitSubstring(ASTSubstring ctx) {
        // Substring은 언어단에서 지원하는 기능이지만 IRCode에서 바로 지원하지 않고 시스템 콜 함수를 이용할 계획
        // d = substring(a, b, c)와 같이 생각하자
        IRCA funcChunk = new IRCA(new IRChunk(
                new IRStatement("LOAD", incIndex(1), new Constant("\"substr@str@int@int\""))));
        StackIndex prevTop = top;
        IRCA containerChunk = visitWithIncIndex(ctx.str, false);
        IRCA fromChunk = visitWithIncIndex(ctx.index1, false);
        IRCA toChunk = visitWithIncIndex(ctx.index2, false);
        IRCA callChunk = new IRCA(new IRChunk(new IRStatement("CALL", prevTop, new Constant(3))), prevTop);
        return aggregateResult(funcChunk, containerChunk, fromChunk, toChunk, callChunk);
    }

    @Override public IRCA visitSubscript(ASTSubscript ctx) {
        StackIndex target = incIndex(1);
        IRCA arrChunk = visit(ctx.arr);
        IRCA indexChunk = visit(ctx.index);

        IRCA opChunk = new IRCA(new IRChunk(
                new IRStatement("GETTABLE", target, arrChunk.argument, indexChunk.argument)), target);
        return aggregateResult(arrChunk, indexChunk, opChunk);
    }

    @Override
    public IRCA visitUnary(ASTUnary ctx) {
        StackIndex target = incIndex(1);
        IRCA oprandChunk = visit(ctx.oprnd);
        if (ctx.op.getType() == SimpleParser.ADD)
            return oprandChunk;

        if (ctx.op.getType() == SimpleParser.SUB) {
            IRCA opChunk = new IRCA(new IRChunk(new IRStatement("UMN", target, oprandChunk.argument)), target);
            return aggregateResult(oprandChunk, opChunk);
        } else if (ctx.op.getType() == SimpleParser.NOT) {
            IRCA opChunk = new IRCA(new IRChunk(new IRStatement("NOT", target, oprandChunk.argument)), target);
            return aggregateResult(oprandChunk, opChunk);
        } else
            throw new RuleException(ctx, "Undefined unary operator");
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
    public IRCA visitBinary(ASTBinary ctx) {
        StackIndex target = incIndex(1);
        IRCA oprnd1Chunk = visit(ctx.oprnd1);
        IRCA oprnd2Chunk = visit(ctx.oprnd2);
        IRCA opChunk = new IRCA(
                new IRChunk(
                        new IRStatement(
                                OperatorTranslator(ctx.op.getType()),
                                target,
                                oprnd1Chunk.argument,
                                oprnd2Chunk.argument)
                ),
                target
        );

        return aggregateResult(oprnd1Chunk, oprnd2Chunk, opChunk);
    }

    @Override public IRCA visitProcCall(ASTProcCall ctx) {
        Function f = symTable.getFunction(ctx);
        IRCA funcChunk = new IRCA(new IRChunk(
                new IRStatement("LOAD", incIndex(1),
                        new Constant("\"" + ctx.pid.getText() + f.getDecorator() + "\""))));
        StackIndex target = top;
        List<IRCA> exprs = new ArrayList<>();
        for (int i = 0; i < f.acceptParams.size(); ++i) {
            exprs.add(visitWithIncIndex(ctx.param.get(i), f.acceptParams.get(i) instanceof Reference));
        }
        IRCA parameterChunk = aggregateResult(exprs.toArray(new IRCA[exprs.size()]));
        IRCA callChunk = new IRCA(new IRChunk(
                new IRStatement("CALL", target, new Constant(top.index - target.index))), target);
        return aggregateResult(funcChunk, parameterChunk, callChunk);
    }
}
