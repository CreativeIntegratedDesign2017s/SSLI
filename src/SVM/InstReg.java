package SVM;

class InstReg {
    Inst[] proc;
    int inst;

    InstReg(Inst[] inst) { proc = inst; }

    Inst getInst() {
        if (inst >= proc.length)
            throw new SimpleException(ErrorCode.OutProcRagne);
        else
            return proc[inst++];
    }
}
