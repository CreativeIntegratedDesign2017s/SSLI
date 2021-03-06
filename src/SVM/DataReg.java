package SVM;

class DataReg {
    Object[] data = new Object[256];
    int base;

    Object read(Reg src) {
        int idx = (src.local) ? (base + src.v) : src.v;
        if (idx >= data.length)
            throw new SimpleException(ErrorCode.OutRegRange);
        else
            return data[idx];
    }

    void write(Reg dst, Object val) {
        int idx = (dst.local) ? (base + dst.v) : dst.v;
        if (idx >= data.length)
            throw new SimpleException(ErrorCode.OutRegRange);
        else
            data[idx] = val;
    }

    static private Object talloc(int[] size, int idx) {
        if (size.length == idx) return new Int(0);
        Object[] arr = new Object[size[idx]];
        for (int i = 0; i < arr.length; i++)
            arr[i] = talloc(size, idx + 1);
        return arr;
    }

    void loadTable(Reg dst, Reg src, int dim) {
        int idx = (src.local) ? (base + src.v) : src.v;
        if (idx + dim - 1 >= data.length)
            throw new SimpleException(ErrorCode.OutRegRange);
        if (dim < 0)
            throw new SimpleException(ErrorCode.ArrNegDim);
        int[] size = new int[dim];
        for (int n = 0; n < dim; n++) {
            size[n] = ((Int) data[idx + n]).v;
            if (size[n] < 0)
                throw new SimpleException(ErrorCode.ArrNegLen);
        }
        write(dst, talloc(size, 0));
    }

    void readTable(Reg dst, Reg src, int idx) {
        Object[] arr = (Object[]) read(src);
        if (idx >= arr.length)
            throw new SimpleException(ErrorCode.OutArrRange);
        write(dst, arr[idx]);
    }
}
