class Person:
   def __init__(self, name, age):
     self.name = name
     self.age = age
 
   def greet(self):
     print("Hello, my name is " + self.name + " and I am " + str(self.age) + " years old.")

class Employee(Person):
    def __init__(self,name,age,department):
       super().__init__(name,age)
       self.department = department
    def greet(self):
        print("Hello, my name is " + self.name + " and I am " + str(self.age) + " years old. I work in the " + self.department + " department.")

employee1 = Employee("John", 30, "Sales")
employee1.greet()