str="Hello, World!"
print(str.capitalize()) #capitalizes the first letter of the string
print(str.upper()) # uppercase
print(str.lower()) # lowercase
print(str.isupper()) #checks if the string is in uppercase
print(str.lower().islower()) #checks if the string is in lowercase
print(str.title()) #capitalizes the first letter of each word in the string
str="   Hello, World!   "
print(str)
print(str.strip()) #removes spaces in start and end of the string

print(str.replace("H", "J")) #replaces H with J

print(str.strip().split(",")) #splits the string into a list of strings based on the delimiter provided


#concatination
str1 = "Hello"
str2 = "World"
print(str1 + " " + str2) #concatenation


#F--strings
age = 36
txt = f"My name is John, I am {age}"
print(txt)