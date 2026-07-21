class Calculator:
    def __init__(self):
        pass

    def add(self, a, b):
        return a + b

    def subtract(self, a, b):
        return a - b

    def multiply(self, a, b):
        return a * b

    def divide(self, a, b):
        if b != 0:
            return a/b
        else :
            return "Divide by 0 not valid"
cl=Calculator()
print(cl.add(10,20))
print(cl.subtract(10,20))
print(cl.multiply(10,20))
print(cl.divide(10,20))
print(cl.divide(10,0))