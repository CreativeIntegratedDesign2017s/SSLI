import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class IROptimizer {
    static class IRNode {
        IRStatement stmt;
        static int idCount = 1;
        int id;
        IRNode(IRStatement stmt) {
            this.stmt = stmt;
            this.id = idCount++;
            predecessors = new ArrayList<>();
        }

        List<IRNode> predecessors;
        IRNode directChild;
        IRNode jumpChild;

        static void DirectLink(IRNode from, IRNode to) {
            from.directChild = to;
            to.predecessors.add(from);
        }

        static void JumpLink(IRNode from, IRNode to) {
            from.jumpChild = to;
            to.predecessors.add(from);
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }

    IRStatement[] source;
    public IROptimizer() {
    }

    void setSource(IRStatement[] source) {
        this.source = source;
    }

    IRStatement[] rebuild(List<IRNode> nodes) {
        Map<IRNode, Integer> indexMap = new HashMap<>();
        IntStream.range(0, nodes.size())
                .forEach(idx -> indexMap.put(nodes.get(idx), idx));
        nodes.stream().forEach(n -> {
            if (n.jumpChild != null) {
                int selfIndex = indexMap.get(n);
                int jumpIndex = indexMap.get(n.jumpChild);
                Constant c = (Constant) n.stmt.arguments[n.stmt.arguments.length - 1];
                c.value = String.valueOf(jumpIndex - selfIndex - 1);
            }
        });
        return nodes.stream().map(n -> n.stmt).toArray(IRStatement[]::new);
    }

    IRStatement[] doOptimize() {
        if (source.length == 0)
            return source;

        List<IRNode> nodes = new ArrayList<>();
        for (IRStatement stmt : source) {
            nodes.add(new IRNode(stmt));
        }

        for (int i = 0; i < source.length; ++i) {
            IRStatement stmt = source[i];
            IRNode n = nodes.get(i);
            switch(stmt.command) {
            case "JMP":
            {
                Constant c = (Constant) stmt.arguments[0];
                int offset = Integer.valueOf(c.value);
                IRNode target = nodes.get(i + offset + 1);
                IRNode.JumpLink(n, target);
                break;
            }
            case "TEST":
            {
                Constant c = (Constant) stmt.arguments[1];
                int offset = Integer.valueOf(c.value);
                IRNode target = nodes.get(i + offset + 1);
                IRNode.JumpLink(n, target);
                // break없음 주의
            }
            default:
                if (i != source.length - 1) {
                    IRNode next = nodes.get(i + 1);
                    IRNode.DirectLink(n, next);
                }
                break;
            }
        }

        DataFlowAnalysis dfa = new DataFlowAnalysis();
        // Optimizing Step
        for (int iteration = 0; iteration < 1; ++iteration) {
            // constant & copy propagation
            DataFlowAnalysis.ReachingDefinition rd = dfa.AnalizeReachingDefinition(nodes);
            for (IRNode node : nodes) {
                if ((node.stmt.command.equals("LOAD") && node.stmt.arguments[1] instanceof StackIndex)
                        || node.stmt.command.equals("NEWTABLE"))
                    // 위의 경우 propagation 하지 않는다.
                    continue;

                Set<IRNode> curInSet = rd.in.get(node);
                for (int i = 1; i < node.stmt.arguments.length; ++i) {
                    IRArgument arg = node.stmt.arguments[i];
                    List<IRNode> defNodes = curInSet.stream()
                            .filter(n -> n.stmt.arguments[0].equals(arg))
                            .collect(Collectors.toList());
                    if (defNodes.size() != 1)
                        continue;
                    IRNode prevDefNode = defNodes.get(0);
                    String opCommand = prevDefNode.stmt.command;
                    if (!opCommand.equals("LOAD") && !opCommand.equals("MOVE") && !opCommand.equals("COPY"))
                        // LOAD, MOVE, COPY가 아니라면 propagation 이 불가능한 커맨드
                        continue;
                    if (opCommand.equals("LOAD") && prevDefNode.stmt.arguments[1] instanceof StackIndex)
                        // 주소 복사형 정의도 propagation 하지 않음
                        continue;

                    IRArgument ppgTarget = prevDefNode.stmt.arguments[1];
                    Set<IRNode> prevOut = rd.out.get(prevDefNode).stream()
                            .filter(n -> n.stmt.arguments[0].equals(ppgTarget))
                            .collect(Collectors.toSet());
                    Set<IRNode> curIn = curInSet.stream()
                            .filter(n -> n.stmt.arguments[0].equals(ppgTarget) && n != node)
                            .collect(Collectors.toSet());
                    if (!prevOut.equals(curIn))   // 서로다름
                        continue;

                    // do propagation
                    node.stmt.arguments[i] = prevDefNode.stmt.arguments[1];
                    if (node.stmt.command.equals("COPY") && prevDefNode.stmt.arguments[1] instanceof Constant)
                        node.stmt.command = "LOAD";
                }
            }

            // common sub-expression elimination

        }

        return rebuild(nodes);
    }
}
