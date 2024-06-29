import pandas as pd

x = int(input("Input number x: "))
y = x
key = 1
n = 0
x_values = []
n_values = []

while key:
    if x % 2 == 0:
        x = x / 2
    else:
        x = 3 * x + 1
    n = n + 1
    x_values.append(x)
    n_values.append(n)
    print("n =", n, ":", "x =", x)
    if x == y:
        print("BIIIIIIIIIIIIIIIIIIIIIIINGO!!!!!!!!!!!!!!!!!!!!!!!")
        key = 0
    if x == 1:
        key = 0

data = {'n': x_values, 'x': x_values}
df = pd.DataFrame(data)
df.to_excel(excel_writer="C:\\Users\\1\\Desktop\\New folder\\test.xlsx", index=False, header=False)

print(df)
with open("C:\\Users\\1\\Desktop\\New folder\\test.dat", "w") as f:
    for i in range(len(n_values)):
        f.write(f"n = {n_values[i]} : x = {x_values[i]}\n")