int start = time();
int x = 3;
int z = 6;
int i = 0;
while i < 100000000 do
    i = i + 1;
    int y = x + x;
end
print(time() - start);