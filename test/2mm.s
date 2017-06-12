int i; int j; int k;

int alpha = 3;
int beta = 2;
int[128][128] a;
int[128][128] b;
int[128][128] c;
int[128][128] d;
int[128][128] tmp;

i = 0;
while (i < 128) do {
    j = 0;
    while (j < 128) do {
        a[i][j] = 1;
        b[i][j] = 1;
        c[i][j] = 1;
        d[i][j] = 1;
        j = j + 1;
    } end
    i = i + 1;
} end

int start = time();

// pragma scope
i = 0;
while (i < 128) do {
    j = 0;
    while (j < 128) do {
        tmp[i][j] = 0;
        k = 0;
        while (k < 128) do {
            tmp[i][j] = tmp[i][j] + alpha * a[i][k] * b[k][j];
            k = k + 1;
        } end
        j = j + 1;
    } end
    i = i + 1;
} end

i = 0;
while (i < 128) do {
    j = 0;
    while (j < 128) do {
        d[i][j] = beta * d[i][j];
        k = 0;
        while (k < 128) do {
            d[i][j] = d[i][j] + tmp[i][k] * c[k][j];
            k = k + 1;
        } end
        j = j + 1;
    } end
    i = i + 1;
} end

print(time() - start);
