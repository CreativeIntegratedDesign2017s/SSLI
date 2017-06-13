int startTime = time();
int i = 0;
int count = 1000000;
int x = 99;
int y = 99;
int z = x ^ y;
int sum = 0;
// --- test input starts after here
while i < count do
    sum = sum + (x ^ y);
    i = i + 1;
end
print(sum);
print("  time:");
print(time() - startTime);