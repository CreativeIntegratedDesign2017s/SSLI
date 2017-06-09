package SVM;

class InstReg {
    Inst[] proc;
    int inst;
    String name;

    InstReg(String name, Inst[] inst) { this.name = name; proc = inst; }

    Inst getInst() {
        if (inst >= proc.length)
            throw new SimpleException(ErrorCode.OutProcRagne);
        else
            return proc[inst++];
    }
}
