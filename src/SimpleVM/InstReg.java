package SimpleVM;

class InstReg {
    Inst[] proc;
    int inst;

    InstReg() { }
    InstReg(Inst[] inst) { proc = inst; }

    Inst getInst() {
        if (inst >= proc.length)
            throw new SimpleException(ErrorCode.OutProcRagne);
        return proc[inst++];
    }
}
