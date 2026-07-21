x = input("Enter Marks: ")
print("You entered:", x)

if float(x) >= 90:
    print("Grade: A")
elif float(x) >= 80:
    print("Grade: B")
elif float(x) >= 70:
    print("Grade: C")
elif float(x) >= 60:
    print("Grade: D")
else:
    print("Grade: F")