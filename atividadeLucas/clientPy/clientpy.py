import socket
import threading

HOST = "localhost"
PORT = 8089

def receber(sock):
    while True:
        try:
            msg = sock.recv(1024).decode()
            if not msg:
                print("\nServidor desconectou")
                break
            print("\nServidor:", msg.strip())
        except:
            break

def main():
    cliente = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    cliente.connect((HOST, PORT))

    print("Conectado")

    t = threading.Thread(target=receber, args=(cliente,))
    t.daemon = True
    t.start()

    try:
        while True:
            msg = input("> ")
            cliente.sendall((msg + "\n").encode())

            if msg.upper() == "SAIR":
                break
    finally:
        cliente.close()
        print("Fim")

if __name__ == "__main__":
    main()