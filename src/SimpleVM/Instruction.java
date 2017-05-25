package SimpleVM;

class Instruction {
    enum Operator {
        NOP,
        MOVE,
        LOAD,
        LEA,
        UMN,
        NOT,
        ADD,
        SUB,
        MUL,
        DIV,
        POW,
        EQ,
        LT,
        LE,
        GT,
        GE,
        NE,
        JMP,
        TEST,
        CALL,
        RET,
        GET_TABLE,
        NEW_TABLE
    }
    Operator op;
}

class Operand {
    enum AddrMode {
        REGISTER,
        CONSTANT
    }
    AddrMode mode;
    Object val;
}

class InstAddr1 extends Instruction {
    Operand A;
}

class InstAddr2 extends Instruction {
    Operand A;
    Operand B;
}

class InstAddr3 extends Instruction {
    Operand A;
    Operand B;
    Operand C;
}
