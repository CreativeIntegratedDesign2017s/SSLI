package SimpleVM;

enum Operator {
    NOP,	MOVE,	LOAD,	UMN,	NOT,
    ADD,	SUB,	MUL,	DIV,	POW,
    EQ,		NE,		LT,		LE,		GT,		GE,
    JMP,	RET,	TEST,	CALL,
    GET_TABLE,		NEW_TABLE
}

class Operand {
    enum AddrMode { GLOBAL, LOCAL, CONST }
    AddrMode mode;
    Object val;

    static private Object valueOf(String str) {
        char c = str.charAt(0);
        if ('0' <= c && c <= '9')
            return Integer.valueOf(str);
        else if (c == '"' && str.charAt(str.length() - 1) == '"')
            return str.substring(1, str.length() - 1);
        else
            return (c == 't');
    }

    Operand(String str) {
        switch (str.charAt(0)) {
            case '$':
                mode = AddrMode.CONST;
                val = valueOf(str.substring(1));
                break;
            case 'G':
                mode = AddrMode.GLOBAL;
                val = Integer.valueOf(str.substring(1));
                break;
            default:
                mode = AddrMode.LOCAL;
                val = Integer.valueOf(str);
                break;
        }
    }
}

class Instruction {
    Operator op;
    Operand[] oprnd;

    static Instruction valueOf(String str) {
        String[] token = str.split(" ");
        Instruction inst = new Instruction();
        inst.op = Operator.valueOf(token[0]);
        inst.oprnd = new Operand[token.length - 1];
        for (int i = 0; i < inst.oprnd.length; i++)
            inst.oprnd[i] = new Operand(token[i + 1]);
        return inst;
    }
}
