package SVM;

class CallStack {
    private Inst[][] proc = new Inst[SimpleVM.size][];
    private String[] name = new String[SimpleVM.size];
    private int[] inst = new int[SimpleVM.size];
    private int[] base = new int[SimpleVM.size];
    private int top = 0;

    boolean push(Inst[] pr, String pid, int ir, int br) {
        if (top == SimpleVM.size) return false;
        proc[top] = pr;
        name[top] = pid;
        inst[top] = ir;
        base[top] = br;
        top++;
        return true;
    }

    boolean pop() {
        if (top == 0) return false;
        top--;
        return true;
    }

    Inst[] topPR() { return proc[top]; }
    int topIR() { return inst[top]; }
    int topBR() { return base[top]; }
    String topProcName() { return name[top]; }
}