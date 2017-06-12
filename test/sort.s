// Algorithm: Selection Sort
// Input: int array, length
// Description: Sorting int array in ascending order
proc sort(int[] a, int len) {
    int i = 0;

    while (i < len) do {
        int min = i;
        int j = i + 1;
        while (j < len) do {
            if (a[j] < a[min]) then
                min = j;
            end
            j = j + 1;
        } end

        int tmp = a[i];
        a[i] = a[min];
        a[min] = tmp;

        i = i + 1;
    } end
}

proc print(int[] a, int len) {
    int i = 0;
    print("{ ");
    while (i < len) do
        print(a[i]);
        print(" ");
        i = i + 1;
    end
    print("}");
}