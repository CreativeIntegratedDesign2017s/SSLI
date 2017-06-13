import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class IROptimizer {
    static class IRNode {
        IRStatement stmt;
        static int idCount = 1;
        int id;
        IRNode(IRStatement stmt) {
            this.stmt = stmt;
            this.id = idCount++;
            predecessors = new ArrayList<>();
            codeChild = null;
            codeMother = null;
        }

        List<IRNode> predecessors;
        IRNode directChild;
        IRNode jumpChild;
        IRNode codeChild, codeMother;

        static void setDirectLink(IRNode from, IRNode to) {
            from.directChild = to;
            to.predecessors.add(from);
        }

        static void setJumpLink(IRNode from, IRNode to) {
            from.jumpChild = to;
            to.predecessors.add(from);
        }

        void setCodeChild(IRNode codeChild) {
            this.codeChild = codeChild;
            if  (codeChild != null)
                codeChild.codeMother = this;
        }

        void insertIRStatement(IRStatement stmt) {
            IRNode childNode = new IRNode(stmt);
            childNode.directChild = directChild;
            childNode.jumpChild = jumpChild;
            childNode.predecessors.add(this);
            directChild = childNode;
            jumpChild = null;

            childNode.setCodeChild(codeChild);
            setCodeChild(childNode);
        }

        void suicide() {
            for (IRNode mother : predecessors) {
                if (jumpChild != null) { // jumpChild를 가진 노드를 제거하는게 지금 가능한가?
                    mother.replaceChild(this, jumpChild);
                    jumpChild.replaceMother(this, mother);
                }
                if (directChild != null) {
                    mother.replaceChild(this, directChild);
                    directChild.replaceMother(this, mother);
                }
            }
            codeMother.setCodeChild(codeChild);
        }

        void replaceChild(IRNode from, IRNode to) {
            if (from == directChild)
                directChild = to;
            else
                jumpChild = to;
        }

        void replaceMother(IRNode from, IRNode to) {
            for (int i = 0; i < predecessors.size(); ++i) {
                if (predecessors.get(i) == from) {
                    predecessors.set(i, to);
                    break;
                }
            }
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }
    }

    int TEMP_REGISTER_POOL_SIZE = 50;
    int TEMP_REGISTER_START_INDEX = 200;
    StackIndex tempIndex;

    public IROptimizer() {
        tempIndex = new StackIndex(TEMP_REGISTER_START_INDEX, true, true);
        lastReachingDefinitionOut = new HashSet<>();
        lastReachingExpressionOut = null;
        lastReachingExpressionBindInfo = new HashMap<>();
    }

    /*
    StackIndex newTemporaryIndex() {
        tempIndex = tempIndex.offset(1);
        if (tempIndex.index >= TEMP_REGISTER_START_INDEX + TEMP_REGISTER_POOL_SIZE) {
            tempIndex = new StackIndex(TEMP_REGISTER_START_INDEX, true, true);
        }
        return tempIndex;
    }
    */

    IRStatement[] rebuild(IRNode header) {
        List<IRNode> nodes = new ArrayList<>();
        while(header != null)
        {
            nodes.add(header);
            header = header.codeChild;
        }
        Map<IRNode, Integer> indexMap = new HashMap<>();
        IntStream.range(0, nodes.size())
                .forEach(idx -> indexMap.put(nodes.get(idx), idx));
        nodes.forEach(n -> {
            if (n.jumpChild != null) {
                int selfIndex = indexMap.get(n);
                int jumpIndex = indexMap.get(n.jumpChild);
                Constant c = (Constant) n.stmt.arguments[n.stmt.arguments.length - 1];
                c.value = String.valueOf(jumpIndex - selfIndex - 1);
            }
        });
        return nodes.stream().map(n -> n.stmt).toArray(IRStatement[]::new);
    }

    List<IRNode> makeGraph(IRStatement[] source) {
        IRNode.idCount = 0;
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
                    IRNode.setJumpLink(n, target);
                    break;
                }
                case "TEST":
                {
                    Constant c = (Constant) stmt.arguments[1];
                    int offset = Integer.valueOf(c.value);
                    IRNode target = nodes.get(i + offset + 1);
                    IRNode.setJumpLink(n, target);
                    // break없음 주의
                }
                default:
                    if (i != source.length - 1) {
                        IRNode next = nodes.get(i + 1);
                        IRNode.setDirectLink(n, next);
                    }
                    break;
            }
            if (i != 0) {
                IRNode prev = nodes.get(i - 1);
                prev.setCodeChild(n);
            }
        }
        return nodes;
    }

    IRStatement[] doOptimizeGlobal(IRStatement[] source) {
        List<IRStatement> optimized = new ArrayList<>();
        List<IRStatement> globalStmts = new ArrayList<>();
        for (int i = 0; i < source.length; ++i)
        {
            IRStatement stmt = source[i];
            if (stmt.command.equals("PROC")){
                optimized.addAll(Arrays.asList(doOptimize(globalStmts.toArray(new IRStatement[globalStmts.size()]), true)));
                globalStmts.clear();
                RawArg size = (RawArg) stmt.arguments[1];
                int procSize = Integer.valueOf(size.arg);
                IRStatement[] procPart = Arrays.copyOfRange(source, i, i + procSize + 1);
                procPart = doOptimize(procPart, false);
                optimized.addAll(Arrays.asList(procPart));
                i += procSize;
            }
            else
                globalStmts.add(stmt);
        }
        if (globalStmts.size() > 0) {
            optimized.addAll(Arrays.asList(doOptimize(globalStmts.toArray(new IRStatement[globalStmts.size()]), true)));
        }
        return optimized.toArray(new IRStatement[optimized.size()]);
    }

    Set<IROptimizer.IRNode> lastReachingDefinitionOut;
    Set<DataFlowAnalysis.ExpressionInfo> lastReachingExpressionOut;
    Map<DataFlowAnalysis.ExpressionInfo, List<IROptimizer.IRNode>> lastReachingExpressionBindInfo;
    IRStatement[] doOptimize(IRStatement[] source, boolean globalScope) {
        if (source.length == 0)
            return source;

        String[] exprOps = {"UMN", "NOT", "ADD", "SUB", "DIV", "MUL", "POW",
                "EQ", "GT", "GE", "LT", "LE", "NEQ"};
        Set<String> exprOpSet = new HashSet<>(Arrays.asList(exprOps));

        List<IRNode> nodes;
        DataFlowAnalysis dfa = new DataFlowAnalysis();
        Map<DataFlowAnalysis.ExpressionInfo, List<IROptimizer.IRNode>> reBindingInfo = null;
        // Optimizing Step
        for (int iteration = 0; iteration < 5; ++iteration) {
            // constant & copy propagation
            nodes = makeGraph(source);
            DataFlowAnalysis.ReachingDefinition rd =
                    dfa.AnalizeReachingDefinition(globalScope ? lastReachingDefinitionOut : new HashSet<>(), nodes);
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
                    Set<IRNode> prevDefSet = rd.out.get(prevDefNode);
                    if (prevDefSet == null)
                        prevDefSet = lastReachingDefinitionOut;
                    Set<IRNode> prevOut = prevDefSet.stream()
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
            source = rebuild(nodes.get(0));

            // common sub-expression elimination
            nodes = makeGraph(source);
            rd = dfa.AnalizeReachingDefinition(globalScope ? lastReachingDefinitionOut : new HashSet<>(), nodes);
            DataFlowAnalysis.ReachingExpression re =
                    dfa.AnalizeReachingExpression(globalScope ? lastReachingExpressionOut : null, nodes);
            reBindingInfo = globalScope ? new HashMap<>(lastReachingExpressionBindInfo) : new HashMap<>();
            for (IRNode node : nodes) {
                if (!exprOpSet.contains(node.stmt.command))
                    // expression이 아님
                    continue;
                IRArgument dest = node.stmt.arguments[0];

                DataFlowAnalysis.ExpressionInfo exprInfo = new DataFlowAnalysis.ExpressionInfo(node.stmt);
                List<IRNode> bindingDefs = reBindingInfo.computeIfAbsent(exprInfo, a -> new ArrayList<>());

                // elimination test
                Set<DataFlowAnalysis.ExpressionInfo> reachingExprs = re.in.get(node);
                if (reachingExprs.contains(exprInfo)) {
                    Set<IRNode> reachingDefs = rd.in.get(node);
                    IRArgument redirectArg = null;
                    for (IRNode bn : bindingDefs) {
                        if (reachingDefs.stream().
                                filter(n -> n != node && n.stmt.arguments[0].equals(bn.stmt.arguments[0]))
                                .count() == 1) {
                            redirectArg = bn.stmt.arguments[0];
                            break;
                        }
                    }
                    if (redirectArg != null) {   // 치환가능한 정의 도달점이 있음
                        node.stmt = new IRStatement("COPY", dest, redirectArg);
                    }
                }
                bindingDefs.add(node);
            }
            source = rebuild(nodes.get(0));
        }

        nodes = makeGraph(source);
        IRNode lastNode = nodes.get(nodes.size() - 1);
        DataFlowAnalysis.ReachingDefinition rd =
                dfa.AnalizeReachingDefinition(globalScope ? lastReachingDefinitionOut : new HashSet<>(), nodes);
        lastReachingDefinitionOut = rd.out.get(lastNode);
        DataFlowAnalysis.ReachingExpression re =
                dfa.AnalizeReachingExpression(globalScope ? lastReachingExpressionOut : null, nodes);
        lastReachingExpressionOut = re.out.get(lastNode);
        lastReachingExpressionBindInfo = reBindingInfo;

        return rebuild(nodes.get(0));
    }
}
