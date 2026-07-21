class Person:
  def __init__(self, name, age):
    self.name = name
    self.age = age

  def greet(self):
    print("Hello, my name is " + self.name + " and I am " + str(self.age) + " years old.")

p1 = Person("Apuroop", 25)
p1.greet()