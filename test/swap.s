proc swap(int& a, int &b) {
    int t = a;
    a = b;
    b = t;
}

int a = 1;
int b = 2;

print("before:");
print(a);
print(",");
print(b);

swap(a, b);

print("    after:");
print(a);
print(",");
print(b);