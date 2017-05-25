package SimpleVM;
import java.util.*;

class InstRegister {
    ArrayList<Instruction> proc;
    Integer idx;

    InstRegister() {
        proc = new ArrayList<>();
        idx = 0;
    }

    InstRegister(ArrayList<Instruction> inst) {
        proc = inst;
        idx = 0;
    }
}

enum FlagRegister {
    Default,
    Invalid_IR,
    Invalid_Reg,
    Overflow,
    Underflow,
    Div_Zero,
    Out_Range,
    Out_Proc
}

public class SimpleVM  {
    private Object[] Register;
    private InstRegister InstReg;
    private FlagRegister FlagReg;
    private Stack<InstRegister> CallStk;
    private HashMap<String, ArrayList<Instruction>> ProcMap;

    SimpleVM() {
        Register = new Object[256];
        InstReg = new InstRegister();
        FlagReg = FlagRegister.Default;
        CallStk = new Stack<>();
        ProcMap = new HashMap<>();
    }

    SimpleVM(String filename) { /* Future Work */ }

    void LoadIR(String proc, List<String> ir) {
        LinkedList<Instruction> inst = new LinkedList<>();
        for (String x : ir)
            inst.add(Instruction.valueOf(x));
        ProcMap.get(proc).addAll(inst);
    }

    void step() {} // line by line
    void run() {}  // run the whole file
}
