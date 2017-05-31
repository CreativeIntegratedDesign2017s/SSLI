package SimpleVM;
import java.io.*;
import java.util.*;

class DataRegister {
    Object[] data;
    int base;

    DataRegister() {
        data = new Object[256];
    }

    Object read(Operand idx) {
        switch (idx.mode) {
            case CONST:
                return idx.val;
            case LOCAL:
                return data[base + (int)idx.val];
            case GLOBAL:
                return data[(int)idx.val];
            default:
                return null;
        }
    }

    void write(Operand idx, Object val) {
        switch (idx.mode) {
            case LOCAL:
                data[base + (int)idx.val] = val;
                break;
            case GLOBAL:
                data[(int)idx.val] = val;
                break;
            default:
                break;
        }
    }

    private Object[] recursiveTable(List<Integer> size) {
        return null;
    }

    void makeTable(Operand a, Operand b, Operand c) {
        return;
    }

    void readTable(Operand a, Operand b, Operand c) {
        data[base + (int)a.val] = ((Object[])data[base + (int)b.val])[(int)data[base + (int)c.val]];
    }
}

class InstRegister {
    Instruction[] proc;
    int idx;

    InstRegister() {
        proc = new Instruction[0];
    }

    InstRegister(Instruction[] inst) {
        proc = inst;
    }

    Instruction getInst() {
        return proc[idx++];
    }
}

enum FlagRegister {
    Default,	Overflow,	Underflow,
    Div_Zero,	Out_Arr,	Out_Proc,
    Unhandled
}

class CallStack {
    static class ProcContext {
        Instruction[] proc;
        int idx;
        int base;
    }

    private Stack<ProcContext> stack;

    CallStack() {
        stack = new Stack<>();
    }

    void push(Instruction[] proc, int idx, int base) {
        ProcContext ctx = new ProcContext();
        ctx.proc = proc;
        ctx.idx = idx;
        ctx.base = base;
        stack.push(ctx);
    }

    ProcContext pop() {
        return stack.pop();
    }
}

public class SimpleVM  {
    private DataRegister dataReg;
    private InstRegister instReg;
    private FlagRegister flagReg;
    private CallStack callStk;
    private HashMap<String, Instruction[]> procMap;

    SimpleVM() {
        dataReg = new DataRegister();
        instReg = new InstRegister();
        flagReg = FlagRegister.Default;
        callStk = new CallStack();
        procMap = new HashMap<>();
    }

    SimpleVM(String fileName) throws IOException {
        File IRCodeFile = new File(fileName);
        FileReader fileReader = new FileReader(IRCodeFile);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line;
        List<String> insts = new ArrayList<>();
        String procName = null;         // null for main method

        // Constructs a ProcMap
        while ((line = bufferedReader.readLine()) != null) {
            String inst = line;
            if(inst.split(" ")[0] == "PROC") {
                loadIR(procName, insts);
                insts = new ArrayList<>();
                procName = inst.split(" ")[1].split("@")[0];
            }
            else {
                insts.add(inst);
            }
        }
        loadIR(procName, insts);
        fileReader.close();
        // End of ProcMap construction

    }

    void loadIR(String pid, List<String> ir) {
        Instruction[] inst = ir.stream()
                .map(Instruction::valueOf)
                .toArray(size -> new Instruction[size]);
        procMap.put(pid, inst);
    }

    boolean step() {
        Instruction inst = instReg.getInst();
        try {
            switch (inst.op) {
                case NOP:
                    break;
                case MOVE:
                    // TODO: Deep Copy
                    break;
                case LOAD:
                    dataReg.write(
                            inst.oprnd[0],
                            dataReg.read(inst.oprnd[1])
                    );
                    break;
                case UMN:
                    dataReg.write(
                            inst.oprnd[0],
                            -(Integer) dataReg.read(inst.oprnd[1])
                    );
                    break;
                case NOT:
                    dataReg.write(
                            inst.oprnd[0],
                            !(Boolean) dataReg.read(inst.oprnd[1])
                    );
                    break;
                case ADD:
                    dataReg.write(
                            inst.oprnd[0],
                            ((Integer) dataReg.read(inst.oprnd[1]) + (Integer) dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case SUB:
                    dataReg.write(
                            inst.oprnd[0],
                            ((Integer) dataReg.read(inst.oprnd[1]) - (Integer) dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case MUL:
                    dataReg.write(
                            inst.oprnd[0],
                            ((Integer) dataReg.read(inst.oprnd[1]) * (Integer) dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case DIV:
                    dataReg.write(
                            inst.oprnd[0],
                            ((Integer) dataReg.read(inst.oprnd[1]) / (Integer) dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case POW:
                    dataReg.write(
                            inst.oprnd[0],
                            Math.pow((Integer) dataReg.read(inst.oprnd[1]), (Integer) dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case EQ:
                    dataReg.write(
                            inst.oprnd[0],
                            Objects.equals(dataReg.read(inst.oprnd[1]), dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case NE:
                    dataReg.write(
                            inst.oprnd[0],
                            !Objects.equals(dataReg.read(inst.oprnd[1]), dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case LT:
                    dataReg.write(
                            inst.oprnd[0],
                            ((Integer) dataReg.read(inst.oprnd[1]) < (Integer) dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case LE:
                    dataReg.write(
                            inst.oprnd[0],
                            ((Integer) dataReg.read(inst.oprnd[1]) <= (Integer) dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case GT:
                    dataReg.write(
                            inst.oprnd[0],
                            ((Integer) dataReg.read(inst.oprnd[1]) > (Integer) dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case GE:
                    dataReg.write(
                            inst.oprnd[0],
                            ((Integer) dataReg.read(inst.oprnd[1]) >= (Integer) dataReg.read(inst.oprnd[2]))
                    );
                    break;
                case JMP:
                    instReg.idx += (Integer) dataReg.read(inst.oprnd[0]);
                    break;
                case TEST:
                    if ((Boolean) dataReg.read(inst.oprnd[0]))
                        instReg.idx += (Integer) dataReg.read(inst.oprnd[1]);
                    break;
                case CALL:
                    callStk.push(instReg.proc, instReg.idx, dataReg.base);
                    instReg = new InstRegister(procMap.get((String) dataReg.read(inst.oprnd[0])));
                    dataReg.base = (Integer) inst.oprnd[1].val;
                    break;
                case RET:
                    if (inst.oprnd.length != 0)
                        dataReg.data[dataReg.base] = dataReg.read(inst.oprnd[0]);
                    CallStack.ProcContext ctx = callStk.pop();
                    instReg.proc = ctx.proc;
                    instReg.idx = ctx.idx;
                    dataReg.base = ctx.base;
                    break;
                case GET_TABLE:
                    dataReg.readTable(inst.oprnd[0], inst.oprnd[1], inst.oprnd[2]);
                    break;
                case NEW_TABLE:
                    dataReg.makeTable(inst.oprnd[0], inst.oprnd[1], inst.oprnd[2]);
                    break;
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    boolean run() {
        return step();
    }
}
