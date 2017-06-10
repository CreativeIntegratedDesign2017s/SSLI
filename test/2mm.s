int i; int j; int k;

int alpha = 3;
int beta = 2;
int[32][32] a;
int[32][32] b;
int[32][32] c;
int[32][32] d;
int[32][32] tmp;

i = 0;
while (i < 32) do {
    j = 0;
    while (j < 32) do {
        a[i][j] = 1;
        b[i][j] = 1;
        c[i][j] = 1;
        d[i][j] = 1;
        j = j + 1;
    } end
    i = i + 1;
} end

// pragma scope
i = 0;
while (i < 32) do {
    j = 0;
    while (j < 32) do {
        tmp[i][j] = 0;
        k = 0;
        while (k < 32) do {
            tmp[i][j] = tmp[i][j] + alpha * a[i][k] * b[k][j];
            k = k + 1;
        } end
        j = j + 1;
    } end
    i = i + 1;
} end

i = 0;
while (i < 32) do {
    j = 0;
    while (j < 32) do {
        d[i][j] = beta * d[i][j];
        k = 0;
        while (k < 32) do {
            d[i][j] = d[i][j] + tmp[i][k] * c[k][j];
            k = k + 1;
        } end
        j = j + 1;
    } end
    i = i + 1;
} end
