package SimpleVM;

import java.util.*;

public class SimpleVM
{
    private Object[] DataMem;
    private Instruction[] InstMem;

    private Integer InstReg;
    private Integer FlagReg;

    SimpleVM() {
        DataMem = new Object[256];
        InstMem = null;
        InstReg = 0;
        FlagReg = 0;
    }
}
