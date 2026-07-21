def add_nums(a, b):
    return a + b
def sub_nums(a, b):
    return a - b
def mul_nums(a, b):
    return a * b
def div_nums(a, b):
    return a / b
a,b=input("Enter two numbers : ").split()
a = int(a)
b = int(b)
print("Sum:", add_nums(a, b))
print("Difference:", sub_nums(a, b))
print("Product:", mul_nums(a, b))
print("Quotient:", div_nums(a, b))