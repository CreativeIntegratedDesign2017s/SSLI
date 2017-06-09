package SVM;

enum Code {
    NOP_,
    MOVE_RR, MOVE_RI, LOAD_RR, LOAD_RI, COPY_RR, COPY_RI,
    UMN_RR, UMN_RI, NOT_RR, NOT_RI,
    ADD_RRR, ADD_RRI, ADD_RIR, ADD_RII,
    SUB_RRR, SUB_RRI, SUB_RIR, SUB_RII,
    MUL_RRR, MUL_RRI, MUL_RIR, MUL_RII,
    DIV_RRR, DIV_RRI, DIV_RIR, DIV_RII,
    POW_RRR, POW_RRI, POW_RIR, POW_RII,
    AND_RRR, AND_RRI, AND_RIR, AND_RII,
    OR_RRR, OR_RRI, OR_RIR, OR_RII,
    EQ_RRR, EQ_RRI, EQ_RIR, EQ_RII,
    NE_RRR, NE_RRI, NE_RIR, NE_RII,
    LT_RRR, LT_RRI, LT_RIR, LT_RII,
    LE_RRR, LE_RRI, LE_RIR, LE_RII,
    GT_RRR, GT_RRI, GT_RIR, GT_RII,
    GE_RRR, GE_RRI, GE_RIR, GE_RII,
    JMP_I, RET_R, RET_I, RET_,
    TEST_RI, CALL_RI,
    GETTABLE_RRR, GETTABLE_RRI,
    NEWTABLE_RRI,
}

class Reg {
    boolean local;
    int v;

    Reg(boolean local, int v) { this.local = local; this.v = v; }

    static Reg valueOf(String str) {
        if (Character.isDigit(str.charAt(0)))
            return new Reg(true, Integer.parseInt(str));
        else if (str.charAt(0) == 'G')
            return new Reg(false, Integer.parseInt(str.substring(1)));
        throw new IllegalArgumentException();
    }

    public String toString() {
        return (local ? "L" : "G") + String.valueOf(v);
    }
}

class Inst {
    Code code;
    Object[] opd;

    private Inst(Code c, Object[] d) { code = c; opd = d; }

    static Inst valueOf(String[] token) {
        StringBuilder codeStr = new StringBuilder(token[0]).append("_");
        Object[] opd = new Object[token.length - 1];
        for (int i = 0; i < opd.length; i++) {
            if (token[i + 1].charAt(0) == '$') {
                codeStr.append("I");
                opd[i] =  Data.valueOf(token[i + 1]);
            }
            else {
                codeStr.append("R");
                opd[i] = Reg.valueOf(token[i + 1]);
            }
        }
        Code code = Code.valueOf(codeStr.toString());
        return new Inst(code, opd);
    }

    public String toString() {
        StringBuilder build = new StringBuilder(code.toString());
        for (Object item : opd) {
            build.append(" ").append(item.toString());
        }
        return build.toString();
    }
}
