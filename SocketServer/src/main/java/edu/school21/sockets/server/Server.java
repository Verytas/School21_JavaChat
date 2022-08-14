package edu.school21.sockets.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

    private final String[] args;
    private int port;
    public static final ArrayList<MultiServer> servers = new ArrayList<>();

    public Server(String[] args) {
        this.args = args;
    }

    public void start() {
        if (!isArgsCorrect()) {
            return;
        };

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started");

            while (true) {
                Socket socket = serverSocket.accept();
                new MultiServer(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isArgsCorrect() {
        if (args.length != 1) {
            System.err.println("Error: wrong number of arguments");
            return false;
        }

        if (!args[0].startsWith("--port=")) {
            System.err.println("Error: wrong argument");
            return false;
        }

        try {
            port = Integer.parseInt(args[0].replaceFirst("--port=", ""));
        } catch (NumberFormatException e) {
            System.err.println("Error: port is not specified");
            return false;
        }
        return true;
    }
}
