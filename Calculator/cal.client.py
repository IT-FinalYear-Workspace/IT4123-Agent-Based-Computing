import socket

def main():
    host = input("Enter server hostname or IP: ")
    port = 8080

    client_socket = socket.socket()
    client_socket.connect((host, port))
    print("Connected to the server")

    while True:
        print("\nMenu:")
        print("1. Add")
        print("2. Subtract")
        print("3. Multiply") 
        print("4. Divide")
        print("5. Power")
        print("0. Exit")

        choice = input("Choose an option: ")
        if choice == '0':
            client_socket.send("exit".encode())
            print("Exiting client")
            break

        operations = {'1': 'add', '2': 'subtract', '3': 'multiply', '4': 'divide', '5': 'power'}
        if choice not in operations:
            print("Invalid choice")
            continue
        
        try:
            num1 = float(input("Enter first number: "))
            num2 = float(input("Enter second number: "))
        except ValueError:
            print("Invalid input. Please enter numbers.")
            continue

        message = f"{operations[choice]},{num1},{num2}"
        client_socket.send(message.encode())

        result = client_socket.recv(1024).decode()
        print("Result:", result)

    client_socket.close()

if __name__ == "__main__":
    main()
