package SVM;

class CallStk {
    private static final int size = 100;
    private Inst[][] proc = new Inst[size][];
    private int[] inst = new int[size];
    private int[] base = new int[size];
    private int top = 0;

    boolean push(Inst[] pr, int ir, int br) {
        if (top == size) return false;
        proc[top] = pr;
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
}