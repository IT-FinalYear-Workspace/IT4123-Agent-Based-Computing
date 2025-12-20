# write a python program using to create a simple calculator.

# the program should include a class named calculator with methods to perforn basic arithmatic operations such as addition,substraction,multiplication,division, and power.

# the division method should display the message"can't divided by 0" if the user attemmpts to divide by zero.

# create an object of calculator class and use a menu driven approach inside while loop to allow the user to shoose an operartion.

# the program should display opyions for addition,substraction, multiplication, and divisio and allow the user exts by entering 0.

# the user should be prompted to enter two numbers and the program should display the results of the selected operations

# the program should continue running until the user chooses to end it.

class Calculator:
    def add(self,a,b) :
        return a+b
    
    def sub(self,a,b) :
        return a-b
    
    def div(self,a,b) :
        if b ==0:
            return "Can't divide by 0"
        return a/b
    
    def mul(self,a,b):
        return a*b
    
    def pow(self,a,b):
        return a**b
    
calc=Calculator()

while True:
    print("\nMenu:")
    print("1. Add")
    print("2. Subtract")
    print("3. Multiply")
    print("4. Divide")
    print("5. Power")
    print("0. Exit")
    
    
    choice= input("Choose an Option: ")
    if choice == '0' :
        print("Bye Bye")
        break
    
    if choice not in ['1','2','3','4','5']:
        print("Invalid Choice Try again")
        continue
    
    
    num1= float(input("Enter Number 01: "))
    num2= float(input("Enter Number 02: "))
    
    if choice =='1':
        print("Result: ", calc.add(num1,num2))
    elif choice == '2':
        print("Result: ", calc.sub(num1,num2))
    elif choice == '3':
        print("Result: ", calc.mul(num1,num2))
    elif choice == '4':
        print("Result: ", calc.div(num1,num2))
    elif choice == '5':
        print("Result: ", calc.pow(num1,num2))

    
    
    