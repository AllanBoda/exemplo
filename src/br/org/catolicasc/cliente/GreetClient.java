package br.org.catolicasc.cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class GreetClient {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void start(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        // Handler para escrita de dados
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        // Handler para leitura de dados
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        clientHandler();
    }

    private void clientHandler() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String message;
        String response;
        do {
            System.out.print("Client: ");
            message = scanner.nextLine();
            out.println(message); // Manda a mensagem para o socket
            response = in.readLine(); // Recebe a resposta do socket
            System.out.println("Server: " + response);
        } while (!message.equals("!quit"));
    }

    public void stop() {
        try {
            in.close();
            out.close();
            clientSocket.close();
        } catch (IOException ex) {
            System.out.println("Erro ao fechar a conexão.");
        }
    }

    public static void main(String[] args) {
        GreetClient client = new GreetClient();
        try {
            client.start("127.0.0.1", 12345);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            client.stop();
        }
    }
}
