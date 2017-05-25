package SimpleVM;

import java.util.*;

class Procedure {
    String name;
    ArrayList<Instruction> inst;
}

public class SimpleVM
{
    private Object[] register;
    private Integer PrgmCnt;
    private Integer InstReg;
    private Integer FlagReg;

    Stack<Procedure> callStack;
    HashMap<String, Procedure> procMap;


    SimpleVM() {
        register = new Object[256];
        PrgmCnt = 0;
        InstReg = 0;
        FlagReg = 0;

        callStack = new Stack<>();
        procMap = new HashMap<>();
    }

    void LoadInst(String inst) {}
    void loadPrgm(String filename) {}
    void step() {} // line by line
    void run() {}  // run the whole file
}
