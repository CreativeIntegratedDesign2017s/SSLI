void swap(int& a, int &b) {
    int t = a;
    a = b;
    b = t;
}

int a = 1;
int b = 2;

swap(a, b);

print(a);
print(b);