import "test/sort.s";

proc print(int[] a, int len) {
    int i = 0;
    while (i < len) do
        print(a[i]);
        i = i + 1;
    end
}

// main()
int[5] a;
a[0] = 5; a[1] = 4; a[2] = 3; a[3] = 2; a[4] = 1;

print("before:");
print(a, 5);

int start = time();
sort(a, 5);
int finish = time();

print("    after:");
print(a, 5);

print("    time:");
print(finish - start);