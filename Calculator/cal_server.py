import socket

class Calculator:
    def add(self, a, b):
        return a + b
    
    def subtract(self, a, b):
        return a - b
    
    def multiply(self, a, b):
        return a * b
    
    def divide(self, a, b):
        if b == 0:
            return "Can't divide by 0"
        return a / b
    
    def power(self, a, b):
        return a ** b

def handle_client(conn):
    calc = Calculator()
    while True:
        data = conn.recv(1024).decode()
        if not data:
            break
        
        if data == 'exit':
            break
        
        try:
            operation, num1, num2 = data.split(',')
            num1 = float(num1)
            num2 = float(num2)
        except ValueError:
            conn.send("Invalid input format.".encode())
            continue
        
        if operation == 'add':
            result = calc.add(num1, num2)
            op_name = "Addition"
        elif operation == 'subtract':
            result = calc.subtract(num1, num2)
            op_name = "Subtraction"
        elif operation == 'multiply':
            result = calc.multiply(num1, num2)
            op_name = "Multiplication"
        elif operation == 'divide':
            result = calc.divide(num1, num2)
            op_name = "Division"
        elif operation == 'power':
            result = calc.power(num1, num2)
            op_name = "Power"
        else:
            result = "Invalid operation"
            op_name = "Unknown"
        
        # Show the received data and result on the server console
        print(f"Client requested {op_name} on numbers {num1} and {num2}. Result: {result}")
        
        conn.send(str(result).encode())

def main():
    host = socket.gethostname()
    port = 8080

    server_socket = socket.socket()
    server_socket.bind((host, port))
    server_socket.listen(1)
    print(f"Server listening on {host}:{port}")

    conn, addr = server_socket.accept()
    print(f"Connection from {addr} established")

    handle_client(conn)

    conn.close()
    server_socket.close()
    print("Server closed")

if __name__ == "__main__":
    main()
