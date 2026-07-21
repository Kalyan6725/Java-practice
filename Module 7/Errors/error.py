try:
    num1 = int(input("Enter the first number: "))
    num2 = int(input("Enter the second number: "))

    result = num1 / num2
    print(f"The result of {num1} divided by {num2} is: {result}")
except ValueError:
    print("Invalid input! Please enter valid integers.")
except ZeroDivisionError:
    print("Error! Division by zero is not allowed.")
finally:
    print("Execution completed.")


#Custome Exception
class CustomError(Exception):
    pass
amount = -500
try :
    if amount < 0:
        raise CustomError("Amount cannot be negative!")
except CustomError as e:
    print(f"Custom Error: {e}")
