import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DataFlowAnalysis {
    class ReachingDefinition {
        Map<IROptimizer.IRNode, Set<IROptimizer.IRNode>> in, out, gen, kill;
        Map<StackIndex, Set<IROptimizer.IRNode>> defs;

        ReachingDefinition(List<IROptimizer.IRNode> source) {
            in = new HashMap<>();
            out = new HashMap<>();
            defs = new HashMap<>();
            gen = new HashMap<>();
            kill = new HashMap<>();

            String[] opsGen = {"MOVE", "COPY", "UMN", "NOT", "ADD", "SUB", "DIV", "MUL", "POW",
                    "EQ", "GT", "GE", "LT", "LE", "NEQ", "NEWTABLE", "CALL", "LOAD", "GETTABLE"};
            Set<String> defOps = new HashSet<>(Arrays.asList(opsGen));

            // initialize structures and make defs
            for (IROptimizer.IRNode node : source) {
                in.put(node, new HashSet<>());
                out.put(node, new HashSet<>());
                Set<IROptimizer.IRNode> genSet = new HashSet<>();
                gen.put(node, genSet);
                IRStatement stmt = node.stmt;
                if (defOps.contains(stmt.command)) {
                    genSet.add(node);
                    Set<IROptimizer.IRNode> dd = defs.getOrDefault(stmt.arguments[0], new HashSet<>());
                    dd.add(node);
                    defs.put((StackIndex)stmt.arguments[0], dd);
                }
            }

            // kill initialize
            for (IROptimizer.IRNode node : source) {
                IRStatement stmt = node.stmt;
                Set<IROptimizer.IRNode> ks = new HashSet<>();
                kill.put(node, ks);
                if (defOps.contains(stmt.command)) {
                    Set<IROptimizer.IRNode> defsWithArg = defs.get(stmt.arguments[0]);
                    if (defsWithArg != null) {
                        List<IROptimizer.IRNode> k = defsWithArg.stream().filter(o -> !o.equals(node)).collect(Collectors.toList());
                        ks.addAll(k);
                    }
                    if (stmt.command.equals("LOAD") && stmt.arguments[1] instanceof StackIndex) {
                        // 메모리 로드는 둘의 위상이 같아지는 것. 즉 양방향으로 kill을 세팅한다.
                        defsWithArg = defs.get(stmt.arguments[1]);
                        if (defsWithArg != null) {
                            List<IROptimizer.IRNode> k = defsWithArg.stream().filter(o -> !o.equals(node)).collect(Collectors.toList());
                            ks.addAll(k);
                        }
                    }
                }
            }

            // iteration start
            boolean changed;
            do {
                changed = false;

                for(IROptimizer.IRNode node : source) {
                    Set<IROptimizer.IRNode> inset = in.get(node);
                    for (IROptimizer.IRNode pn : node.predecessors) {
                        changed = changed || inset.addAll(out.get(pn));
                    }
                    Set<IROptimizer.IRNode> prevOut = out.get(node);
                    Set<IROptimizer.IRNode> nextOut = new HashSet<>(inset);
                    nextOut.removeAll(kill.get(node));
                    nextOut.addAll(gen.get(node));

                    out.put(node, nextOut);
                    changed = changed || !prevOut.equals(nextOut);
                }
            } while(changed);

            System.out.println("ReachingDefinition");
            for (IROptimizer.IRNode node : source) {
                System.out.println(String.format("%d:\tgen: %s\tkill: %s\tin: %s\tout: %s",
                        node.id, gen.get(node), kill.get(node), in.get(node), out.get(node)));
            }
        }
    }

    class ReachingExpression {
        class ExpressionInfo {
            IRArgument dest;
            String command;
            Set<IRArgument> argSet;

            ExpressionInfo(IRArgument dest, String command, IRArgument[] arguments) {
                this.dest = dest;
                this.command = command;
                this.argSet = new HashSet<IRArgument>(){{
                    addAll(Arrays.asList(arguments));
                }};
            }

            boolean test(IRArgument tArg) {
                for (IRArgument arg : argSet) {
                    if (arg.equals(tArg))
                        return false;
                }
                return true;
            }

            @Override
            public boolean equals(Object o) {
                if (!(o instanceof ExpressionInfo))
                    return false;
                ExpressionInfo ei = (ExpressionInfo) o;
                return ei.command.equals(command) && argSet.equals(ei.argSet);
            }

            @Override
            public int hashCode() {
                return toString().hashCode();
            }

            @Override
            public String toString() {
                return dest + " = " + command + " " + argSet;
            }
        }
        Map<IROptimizer.IRNode, Set<ExpressionInfo>> gen, in, out;
        Map<IROptimizer.IRNode, IRArgument> kill;

        ReachingExpression(List<IROptimizer.IRNode> source) {
            in = new HashMap<>();
            out = new HashMap<>();
            gen = new HashMap<>();
            kill = new HashMap<>();

            String[] ops = {"UMN", "NOT", "ADD", "SUB", "DIV", "MUL", "POW",
                    "EQ", "GT", "GE", "LT", "LE", "NEQ"};
            Set<String> defOps = new HashSet<>(Arrays.asList(ops));

            Set<ExpressionInfo> allExprs = new HashSet<>();
            for (IROptimizer.IRNode node : source) {
                in.put(node, new HashSet<>());
                out.put(node, new HashSet<>());
                gen.put(node, new HashSet<>());
                if (defOps.contains(node.stmt.command)) {
                    ExpressionInfo expr = new ExpressionInfo(node.stmt.arguments[0], node.stmt.command,
                            Arrays.copyOfRange(node.stmt.arguments, 1, node.stmt.arguments.length));
                    allExprs.add(expr);
                    Set<ExpressionInfo> genSet = gen.get(node);
                    if (expr.test(node.stmt.arguments[0]))
                        genSet.add(expr);
                    kill.put(node, node.stmt.arguments[0]);
                }
            }
            for (int i = 0; i < source.size(); ++i) {
                IROptimizer.IRNode node = source.get(i);
                if (i != 0) {
                    in.get(node).addAll(allExprs);
                }
                out.get(node).addAll(allExprs);
            }

            // iteration start
            boolean changed;
            do {
                changed = false;
                for (IROptimizer.IRNode node : source) {
                    Set<ExpressionInfo> is = null;
                    for (IROptimizer.IRNode pred : node.predecessors) {
                        if (is == null)
                            is = new HashSet<>(out.get(pred));
                        else
                            is.retainAll(out.get(pred));
                    }
                    if (is != null)
                    {
                        Set<ExpressionInfo> prev = in.put(node, is);
                        if (!prev.equals(is))
                            changed = true;
                    }
                    else
                        is = in.get(node);
                    Set<ExpressionInfo> nextOut = is.stream()
                            .filter(o -> o.test(kill.get(node)))
                            .collect(Collectors.toSet());
                    nextOut.addAll(gen.get(node));
                    Set<ExpressionInfo> prevOut = out.put(node, nextOut);
                    if (!prevOut.equals(nextOut))
                        changed = true;
                }
            } while(changed);

            System.out.println("ReachingExpression");
            for (IROptimizer.IRNode node : source) {
                System.out.println(String.format("%d:\tgen: %s\tkill: %s\tin: %s\tout: %s",
                        node.id, gen.get(node), kill.get(node), in.get(node), out.get(node)));
            }
        }
    }

    ReachingDefinition AnalizeReachingDefinition(List<IROptimizer.IRNode> nodes) {
        return new ReachingDefinition(nodes);
    }

    ReachingExpression AnalizeReachingExpression(List<IROptimizer.IRNode> nodes) {
        return new ReachingExpression(nodes);
    }
}
