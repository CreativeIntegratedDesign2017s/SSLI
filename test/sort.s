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

// Print int array
proc print(int[] a, int len) {
    int i = 0;
    while (i < len) do
        print(a[i]);
        i = i + 1;
    end
}

int start = time();
// main()
int[5] a;
a[0] = 5; a[1] = 4; a[2] = 3; a[3] = 2; a[4] = 1;
print("before:");
print(a, 5);
sort(a, 5);
print("    after:");
print(a, 5);
print("    time:");
print(time() - start);