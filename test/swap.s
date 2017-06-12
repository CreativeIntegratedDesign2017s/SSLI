proc swap(int& a, int &b) {
    int t = a;
    a = b;
    b = t;
}

int a = 1;
int b = 2;

print("(");
print(a);
print(", ");
print(b);
print(") -> ");

swap(a, b);

print("(");
print(a);
print(", ");
print(b);
print(")\n");