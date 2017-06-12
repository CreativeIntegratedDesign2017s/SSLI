import "test/sort.s";

int[5] a;
a[0] = 5; a[1] = 4; a[2] = 3; a[3] = 2; a[4] = 1;

print(a, 5);
print("\n");

int start = time();
sort(a, 5);
int finish = time();

print(a, 5);
print("\n");

print("Elapsed Time: ");
print(finish - start);
print("ms\n");