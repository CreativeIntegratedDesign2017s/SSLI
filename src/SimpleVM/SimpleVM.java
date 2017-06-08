package SimpleVM;
import java.util.*;
import java.util.function.*;

/* Singleton Class */
public class SimpleVM  {
    static final int size = 256;
    private static SimpleVM vm;

    private DataReg dataReg;
    private InstReg instReg;
    private CallStack callStk;
    private HashMap<String, Inst[]> procMap;

    private SimpleVM() {
        dataReg = new DataReg();
        instReg = new InstReg();
        callStk = new CallStack();
        procMap = new HashMap<>();
    }

    private boolean stepInst() {
        Inst inst = instReg.getInst();
        switch (inst.code) {
            case NOP_:
                break;
            case MOVE_RR: {
                Reg dst = (Reg) inst.opd[0];
                Reg src = (Reg) inst.opd[1];
                dataReg.write(dst, Data.copy(dataReg.read(src)));
            }
            break;
            case MOVE_RI: {
                Reg dst = (Reg) inst.opd[0];
                Data src = (Data) inst.opd[1];
                Data.vchg(dataReg.read(dst), src);
            }
            break;
            case LOAD_RR: {
                Reg dst = (Reg) inst.opd[0];
                Reg src = (Reg) inst.opd[1];
                Data.vchg(dataReg.read(dst), dataReg.read(src));
            }
            break;
            case LOAD_RI: {
                Reg dst = (Reg) inst.opd[0];
                Data src = (Data) inst.opd[1];
                dataReg.write(dst, Data.copy(src));
            }
            break;
            case GET_TABLE_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Reg src = (Reg) inst.opd[1];
                int idx = ((Int) inst.opd[2]).v;
                dataReg.readTable(dst, src, idx);
            }
            break;
            case GET_TABLE_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Reg src = (Reg) inst.opd[1];
                int idx = ((Int) dataReg.read((Reg) inst.opd[2])).v;
                dataReg.readTable(dst, src, idx);
            }
            break;
            case NEW_TABLE_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Reg src = (Reg) inst.opd[1];
                int dim = ((Int) inst.opd[2]).v;
                dataReg.loadTable(dst, src, dim);
            }
            break;
            case JMP_I: {
                int dst = ((Int) inst.opd[0]).v;
                instReg.inst += dst;
            }
            break;
            case TEST_RI: {
                boolean cond = ((Bool) dataReg.read((Reg) inst.opd[0])).v;
                int dst = ((Int) inst.opd[1]).v;
                if (cond) instReg.inst += dst;
            }
            break;
            case CALL_RI: {
                Reg src = (Reg) inst.opd[0];
                String proc = ((Str) dataReg.read(src)).v;
                switch (proc) {
                    case "print":
                        System.out.println(dataReg.data[dataReg.base + src.v + 1]);
                        break;
                    case "concat@str@str": {
                        Str str1 = (Str) dataReg.data[dataReg.base + src.v + 1];
                        Str str2 = (Str) dataReg.data[dataReg.base + src.v + 2];
                        dataReg.write(src, Str.concat(str1, str2));
                    }
                    break;
                    case "substr@str@int@int": {
                        Str str = (Str) dataReg.data[dataReg.base + src.v + 1];
                        Int from = (Int) dataReg.data[dataReg.base + src.v + 2];
                        Int to = (Int) dataReg.data[dataReg.base + src.v + 3];
                        dataReg.write(src, Str.substr(str, from, to));
                    }
                    break;
                    default: {
                        if (!callStk.push(instReg.proc, instReg.inst, dataReg.base))
                            throw new SimpleException(ErrorCode.StackOverflow);
                        instReg.proc = procMap.get(proc);
                        instReg.inst = 0;
                        dataReg.base = src.v;
                    }
                    break;
                }
            }
            break;
            case RET_R: {
                Reg dst = new Reg(true, 0);
                Reg src = (Reg) inst.opd[0];
                dataReg.write(dst, Data.copy(dataReg.read(src)));
                if (!callStk.pop())
                    return false;
                instReg.proc = callStk.topPR();
                instReg.inst = callStk.topIR();
                dataReg.base = callStk.topBR();
            }
            break;
            case UMN_RR: {
                Reg dst = (Reg) inst.opd[0];
                Int src = (Int) dataReg.read((Reg) inst.opd[1]);
                dataReg.write(dst, Int.umn(src));
            }
            break;
            case UMN_RI: {
                Reg dst = (Reg) inst.opd[0];
                Int src = (Int) inst.opd[1];
                dataReg.write(dst, Int.umn(src));
            }
            break;
            case NOT_RR: {
                Reg dst = (Reg) inst.opd[0];
                Bool src = (Bool) dataReg.read((Reg) inst.opd[1]);
                dataReg.write(dst, Bool.not(src));
            }
            break;
            case NOT_RI: {
                Reg dst = (Reg) inst.opd[0];
                Bool src = (Bool) inst.opd[1];
                dataReg.write(dst, Bool.not(src));
            }
            break;
            case ADD_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.add(src1, src2));
            }
            break;
            case ADD_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.add(src1, src2));
            }
            break;
            case ADD_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.add(src1, src2));
            }
            break;
            case ADD_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.add(src1, src2));
            }
            break;
            case SUB_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.sub(src1, src2));
            }
            break;
            case SUB_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.sub(src1, src2));
            }
            break;
            case SUB_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.sub(src1, src2));
            }
            break;
            case SUB_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.sub(src1, src2));
            }
            break;
            case MUL_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.mul(src1, src2));
            }
            break;
            case MUL_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.mul(src1, src2));
            }
            break;
            case MUL_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.mul(src1, src2));
            }
            break;
            case MUL_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.mul(src1, src2));
            }
            break;
        }
        return true;
    }

    public static void
    init() {
        vm = new SimpleVM();

        List<String> inst = new LinkedList<>();
        inst.add("LOAD 2 $3");
        inst.add("LOAD 3 $3");
        inst.add("NEW_TABLE 1 2 $2");
        inst.add("GET_TABLE 5 1 $1");
        inst.add("GET_TABLE 6 5 $2");
        inst.add("MOVE 6 $7");
        inst.add("LOAD 0 $\"print\"");
        inst.add("CALL 0 $1");
        inst.add("RET 0");

        loadInst(null, inst);
        execInst();
    }

    /*
    SimpleVM(String fileName) throws IOException {
        super();
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
                loadInst(procName, insts);
                insts = new ArrayList<>();
                procName = inst.split(" ")[1].split("@")[0];
            }
            else {
                insts.add(inst);
            }
        }
        loadInst(procName, insts);
        fileReader.close();
        // End of ProcMap construction

        instReg = new InstReg(procMap.get(null));
        run();
    }
    */

    public static void
    loadInst(String proc, List<String> ir) {
        Inst[] inst = ir.stream()
                .map(Inst::valueOf)
                .toArray((IntFunction<Inst[]>) Inst[]::new);
        vm.procMap.put(proc, inst);
        vm.instReg.proc = inst;
    }

    public static void
    execInst() { while (vm.stepInst()); }

    public static void
    printReg(String str) { System.out.println(vm.dataReg.read(Reg.valueOf(str))); }
}
