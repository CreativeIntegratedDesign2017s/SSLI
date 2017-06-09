import java.util.ArrayList;
import java.util.List;

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
    public IROptimizer(IRStatement[] source) {
        this.source = source;
    }

    void doOptimize() {
        if (source.length == 0)
            return;

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
                IRNode target = nodes.get(i + offset);
                IRNode.JumpLink(n, target);
                break;
            }
            case "TEST":
            {
                Constant c = (Constant) stmt.arguments[1];
                int offset = Integer.valueOf(c.value);
                IRNode target = nodes.get(i + offset);
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

        DataFlowAnalysis dfa = new DataFlowAnalysis(nodes);
        dfa.DoAnalysis();
    }
}
