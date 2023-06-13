package br.org.catolicasc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class GreetServer {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(int port) throws IOException {
        // Inicializar atributos
        serverSocket = new ServerSocket(port); // Escuta na porta port
        clientSocket = serverSocket.accept(); // Espera conexão

        // Handler para escrita de dados
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        // Handler para leitura de dados
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        clientHandler();
    }

    private void clientHandler() throws IOException {
        String message;
        do {
            message = in.readLine();
            if (message != null) {
                System.out.println("Client: " + message);
                if ("!quit".equals(message)) {
                    out.println("Server disconnected.");
                    break;
                } else {
                    System.out.print("Server: ");
                    String response = System.console().readLine();
                    out.println(response);
                }
            }
        } while (true);
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar a conexão.");
        }
    }

    public static void main(String[] args) {
        GreetServer server = new GreetServer();
        try {
            server.start(12345);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }
}
