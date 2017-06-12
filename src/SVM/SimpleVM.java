package SVM;
import java.util.*;

/* Singleton Class */
public class SimpleVM  {
    // Virtual Hardwares
    private static int instReg;
    private static Inst[] procReg;
    private static DataReg dataReg;
    private static CallStk callStk;
    private static HashMap<String, Inst[]> procMap;
    private static final String regex = " (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

    static {
        dataReg = new DataReg();
        callStk = new CallStk();
        procMap = new HashMap<>();
    }

    private static boolean stepInst() {
        if (instReg > procReg.length)
            throw new SimpleException(ErrorCode.OutProcRagne);
        Inst inst = procReg[instReg++];

        switch (inst.code) {
            case NOP_:
                break;
            case MOVE_RR: {
                Reg dst = (Reg) inst.opd[0];
                Data src = (Data) dataReg.read((Reg) inst.opd[1]);
                Data.vchg((Data) dataReg.read(dst), src);
            }
            break;
            case MOVE_RI: {
                Reg dst = (Reg) inst.opd[0];
                Data src = (Data) inst.opd[1];
                Data.vchg((Data) dataReg.read(dst), src);
            }
            break;
            case LOAD_RR: {
                Reg dst = (Reg) inst.opd[0];
                Reg src = (Reg) inst.opd[1];
                dataReg.write(dst, dataReg.read(src));
            }
            break;
            case LOAD_RI: {
                Reg dst = (Reg) inst.opd[0];
                Data src = (Data) inst.opd[1];
                dataReg.write(dst, Data.copy(src));
            }
            break;
            case COPY_RR: {
                Reg dst = (Reg) inst.opd[0];
                Data src = (Data) dataReg.read((Reg) inst.opd[1]);
                dataReg.write(dst, Data.copy(src));
            }
            break;
            case COPY_RI: {
                Reg dst = (Reg) inst.opd[0];
                Data src = (Data) inst.opd[1];
                dataReg.write(dst, Data.copy(src));
            }
            break;
            case GETTABLE_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Reg src = (Reg) inst.opd[1];
                int idx = ((Int) inst.opd[2]).v;
                dataReg.readTable(dst, src, idx);
            }
            break;
            case GETTABLE_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Reg src = (Reg) inst.opd[1];
                int idx = ((Int) dataReg.read((Reg) inst.opd[2])).v;
                dataReg.readTable(dst, src, idx);
            }
            break;
            case NEWTABLE_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Reg src = (Reg) inst.opd[1];
                int dim = ((Int) inst.opd[2]).v;
                dataReg.loadTable(dst, src, dim);
            }
            break;
            case JMP_I: {
                int dst = ((Int) inst.opd[0]).v;
                instReg += dst;
            }
            break;
            case TEST_RI: {
                boolean cond = ((Bool) dataReg.read((Reg) inst.opd[0])).v;
                int dst = ((Int) inst.opd[1]).v;
                if (cond) instReg += dst;
            }
            break;
            case CALL_RI: {
                Reg src = (Reg) inst.opd[0];
                String proc = ((Str) dataReg.read(src)).v;
                switch (proc) {
                    case "time": {
                        Int time = new Int((int)System.currentTimeMillis());
                        dataReg.write(src, time);
                    }
                    break;
                    case "print@bool":
                        System.out.print(((Bool)dataReg.data[dataReg.base + src.v + 1]).v);
                        break;
                    case "print@int":
                        System.out.print(((Int)dataReg.data[dataReg.base + src.v + 1]).v);
                        break;
                    case "print@str":
                        System.out.print(((Str)dataReg.data[dataReg.base + src.v + 1]).v);
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
                        if (!callStk.push(procReg, instReg, dataReg.base))
                            throw new SimpleException(ErrorCode.StackOverflow);
                        procReg = procMap.get(proc);
                        instReg = 0;
                        dataReg.base = src.v;
                    }
                    break;
                }
            }
            break;
            case RET_R: {
                Reg dst = new Reg(true, 0);
                Reg src = (Reg) inst.opd[0];
                dataReg.write(dst, Data.copy((Data) dataReg.read(src)));
                if (!callStk.pop())
                    return false;
                procReg = callStk.topPR();
                instReg = callStk.topIR();
                dataReg.base = callStk.topBR();
            }
            break;
            case RET_I: {
                Reg dst = new Reg(true, 0);
                Data src = (Data) inst.opd[0];
                dataReg.write(dst, Data.copy(src));
                if (!callStk.pop())
                    return false;
                procReg = callStk.topPR();
                instReg = callStk.topIR();
                dataReg.base = callStk.topBR();
            }
            break;
            case RET_: {
                if (!callStk.pop())
                    return false;
                procReg = callStk.topPR();
                instReg = callStk.topIR();
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
            case DIV_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.div(src1, src2));
            }
            break;
            case DIV_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.div(src1, src2));
            }
            break;
            case DIV_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.div(src1, src2));
            }
            break;
            case DIV_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.div(src1, src2));
            }
            break;
            case POW_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.pow(src1, src2));
            }
            break;
            case POW_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.pow(src1, src2));
            }
            break;
            case POW_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.pow(src1, src2));
            }
            break;
            case POW_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.pow(src1, src2));
            }
            break;
            case AND_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Bool src1 = (Bool) dataReg.read((Reg) inst.opd[1]);
                Bool src2 = (Bool) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Bool.and(src1, src2));
            }
            break;
            case AND_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Bool src1 = (Bool) dataReg.read((Reg) inst.opd[1]);
                Bool src2 = (Bool) inst.opd[2];
                dataReg.write(dst, Bool.and(src1, src2));
            }
            break;
            case AND_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Bool src1 = (Bool) inst.opd[1];
                Bool src2 = (Bool) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Bool.and(src1, src2));
            }
            break;
            case AND_RII: {
                Reg dst = (Reg) inst.opd[0];
                Bool src1 = (Bool) inst.opd[1];
                Bool src2 = (Bool) inst.opd[2];
                dataReg.write(dst, Bool.and(src1, src2));
            }
            break;
            case OR_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Bool src1 = (Bool) dataReg.read((Reg) inst.opd[1]);
                Bool src2 = (Bool) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Bool.or(src1, src2));
            }
            break;
            case OR_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Bool src1 = (Bool) dataReg.read((Reg) inst.opd[1]);
                Bool src2 = (Bool) inst.opd[2];
                dataReg.write(dst, Bool.or(src1, src2));
            }
            break;
            case OR_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Bool src1 = (Bool) inst.opd[1];
                Bool src2 = (Bool) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Bool.or(src1, src2));
            }
            break;
            case OR_RII: {
                Reg dst = (Reg) inst.opd[0];
                Bool src1 = (Bool) inst.opd[1];
                Bool src2 = (Bool) inst.opd[2];
                dataReg.write(dst, Bool.or(src1, src2));
            }
            break;
            case EQ_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.eq(src1, src2));
            }
            break;
            case EQ_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.eq(src1, src2));
            }
            break;
            case EQ_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.eq(src1, src2));
            }
            break;
            case EQ_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.eq(src1, src2));
            }
            break;
            case NE_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.ne(src1, src2));
            }
            break;
            case NE_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.ne(src1, src2));
            }
            break;
            case NE_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.ne(src1, src2));
            }
            break;
            case NE_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.ne(src1, src2));
            }
            break;
            case LT_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.lt(src1, src2));
            }
            break;
            case LT_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.lt(src1, src2));
            }
            break;
            case LT_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.lt(src1, src2));
            }
            break;
            case LT_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.lt(src1, src2));
            }
            break;
            case LE_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.le(src1, src2));
            }
            break;
            case LE_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.le(src1, src2));
            }
            break;
            case LE_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.le(src1, src2));
            }
            break;
            case LE_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.le(src1, src2));
            }
            break;
            case GT_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.gt(src1, src2));
            }
            break;
            case GT_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.gt(src1, src2));
            }
            break;
            case GT_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.gt(src1, src2));
            }
            break;
            case GT_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.gt(src1, src2));
            }
            break;
            case GE_RRR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.ge(src1, src2));
            }
            break;
            case GE_RRI: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) dataReg.read((Reg) inst.opd[1]);
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.ge(src1, src2));
            }
            break;
            case GE_RIR: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) dataReg.read((Reg) inst.opd[2]);
                dataReg.write(dst, Int.ge(src1, src2));
            }
            break;
            case GE_RII: {
                Reg dst = (Reg) inst.opd[0];
                Int src1 = (Int) inst.opd[1];
                Int src2 = (Int) inst.opd[2];
                dataReg.write(dst, Int.ge(src1, src2));
            }
            break;
        }
        return true;
    }

    private static void runThrough() {
        try { while (true) if (!stepInst()) break; }
        catch (SimpleException e) {
            e.proc = ((Str)dataReg.data[dataReg.base]).v;
            e.line = instReg;
            throw e;
        }
        catch (Exception e) {
            String proc;
            if (dataReg.base == 0) proc = "@main";
            else if (dataReg.data[dataReg.base] == null) proc = "@broken";
            else if (dataReg.data[dataReg.base].getClass() == Str.class)
                proc = ((Str)dataReg.data[dataReg.base]).v;
            else proc = "@broken";
            throw new SimpleException(e, ErrorCode.Unknown, proc, instReg);
        }
    }

    private static int
    loadProc(String[] inst, int idx, String name, String size) {
        Inst[] proc = new Inst[Integer.parseInt(size)];
        for (int i = 0; i < proc.length; i++)
            proc[i] = Inst.valueOf(inst[idx + i].split(regex));
        procMap.put(name, proc);
        return proc.length;
    }

    public static void
    loadInst(String[] inst) {
        List<Inst> main = new ArrayList<>();
        for (int i = 0; i < inst.length; i++) {
            String[] token = inst[i].split(regex);
            if (token[0].equals("PROC"))
                i += loadProc(inst, i + 1, token[1], token[2]);
            else
                main.add(Inst.valueOf(token));
        }
        main.add(Inst.valueOf(new String[]{"RET"}));

        Inst[] proc = main.toArray(new Inst[main.size()]);
        procMap.put("", proc);
        procReg = proc;
        instReg = 0;
        runThrough();
    }

    public static void
    printReg(String str) { System.out.println(dataReg.read(Reg.valueOf(str))); }
}
