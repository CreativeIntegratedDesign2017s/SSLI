int i; int j; int k;

int alpha = 3;
int beta = 2;
int[1024][1024] a;
int[1024][1024] b;
int[1024][1024] c;
int[1024][1024] d;
int[1024][1024] tmp;

i = 0;
while (i < 1024) do {
    j = 0;
    while (j < 1024) do {
        a[i][j] = 1;
        b[i][j] = 1;
        c[i][j] = 1;
        d[i][j] = 1;
        j = j + 1;
    } end
    i = i + 1;
} end

int start = time();
i = 0;
while (i < 1024) do {
    j = 0;
    while (j < 1024) do {
        tmp[i][j] = 0;
        k = 0;
        while (k < 1024) do {
            tmp[i][j] = tmp[i][j] + alpha * a[i][k] * b[k][j];
            k = k + 1;
        } end
        j = j + 1;
    } end
    i = i + 1;
} end

i = 0;
while (i < 1024) do {
    j = 0;
    while (j < 1024) do {
        d[i][j] = beta * d[i][j];
        k = 0;
        while (k < 1024) do {
            d[i][j] = d[i][j] + tmp[i][k] * c[k][j];
            k = k + 1;
        } end
        j = j + 1;
    } end
    i = i + 1;
} end
int finish = time();
print("Elapsed Time: ");
print(finish - start);
print("ms\n");