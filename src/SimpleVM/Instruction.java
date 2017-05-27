package SimpleVM;

class Operand {
    enum AddrMode {
        REGISTER,
        CONSTANT
    }

    AddrMode mode;
    Object val;

    Operand(String str) {
        if (str.charAt(0) == '$') {
            this.mode = AddrMode.CONSTANT;
            switch(str.charAt(1)) {
                case '"':
                    this.val = str.substring(1, str.length() - 1);
                    break;
                case '0':
                    /* Special Case Optimization */
                    this.val = 0;
                    break;
                case '1': case '2': case '3':
                case '4': case '5': case '6':
                case '7': case '8': case '9':
                    this.val = Integer.valueOf(str.substring(0, str.length() - 1));
                    break;
                case 't':
                    this.val = true;
                    break;
                case 'f':
                    this.val = false;
                    break;
            }
        }
        else {
            this.mode = AddrMode.REGISTER;
            this.val = Integer.valueOf(str.substring(0, str.length() - 1));
        }
    }

    /*
     Get value of an operand regardless of its mode(Register or Constant
     */
    Object getValue(Object[] Register) {
        if(this.mode == AddrMode.REGISTER) {
            return Register[(int) this.val];
        }
        return this.val;
    }
}

class Instruction {
    enum Operator {
        NOP,
        MOVE, LOAD, LEA,
        UMN, NOT,
        ADD, SUB, MUL, DIV, POW,
        EQ, NE, LT, LE, GT, GE,
        JMP, RET,
        TEST, CALL,
        GET_TABLE,
        NEW_TABLE
    }

    Operator op;

    static Instruction valueOf(String str) {
        Instruction inst = null;
        String[] token = str.split(" ");
        Operator op = Operator.valueOf(token[0]);
        switch (op) {
            case NOP:
                InstAddr0 addr0 = new InstAddr0();
                addr0.op = op;
                inst = addr0;
                break;
            case JMP:
            case RET:
                InstAddr1 addr1 = new InstAddr1();
                addr1.op = op;
                addr1.A = (token.length == 1) ? null : new Operand(token[1]);
                inst =  addr1;
                break;
            case MOVE: case LOAD: case LEA:
            case UMN: case NOT:
            case TEST: case CALL:
                InstAddr2 addr2 = new InstAddr2();
                addr2.op = op;
                addr2.A = new Operand(token[1]);
                addr2.B = new Operand(token[2]);
                inst = addr2;
                break;
            case ADD: case SUB: case MUL: case DIV: case POW:
            case LE: case LT: case EQ: case NE: case GT: case GE:
            case GET_TABLE: case NEW_TABLE:
                InstAddr3 addr3 = new InstAddr3();
                addr3.op = op;
                addr3.A = new Operand(token[1]);
                addr3.B = new Operand(token[2]);
                addr3.C = new Operand(token[3]);
                inst = addr3;
                break;
        }
        return inst;
    }

    void run(Object[] Register) {}
}

class InstAddr0 extends Instruction { }
class InstAddr1 extends Instruction { Operand A; }
class InstAddr2 extends Instruction { Operand A, B; }
class InstAddr3 extends Instruction {
    Operand A, B, C;

    @Override
    void run(Object[] Register) {
        try {
            if (A.mode == Operand.AddrMode.REGISTER) {
                switch (this.op) {
                    case ADD:
                        Register[(int) A.val] = Math.addExact((int) B.getValue(Register), (int) C.getValue(Register));
                        break;
                    case SUB:
                        Register[(int) A.val] = Math.subtractExact((int) B.getValue(Register), (int) C.getValue(Register));
                        break;
                    case MUL:
                        Register[(int) A.val] = Math.multiplyExact((int) B.getValue(Register), (int) C.getValue(Register));
                        break;
                    case DIV:
                        Register[(int) A.val] = Math.floorDiv((int) B.getValue(Register), (int) C.getValue(Register));
                        break;
                    case POW:
                        Register[(int) A.val] = Math.pow((int) B.getValue(Register), (int) C.getValue(Register));
                        break;
                }
            }
            else {
                throw new Exception("Wrong Destination Type");
            }
        }
        catch(Exception exc) {
            if(exc instanceof ArithmeticException) { /* TODO */ }   // catches overflow, underflow and div by 0s
            else if(exc instanceof ArrayIndexOutOfBoundsException) { /* TODO */ }
            else { /* TODO */ }
        }
    }

}
