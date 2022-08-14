package client;

import java.io.*;
import java.net.Socket;

public class Client {

    private final String[] args;
    private int port;

    public Client(String[] args) {
        this.args = args;
    }

    public void start() {
        if (!isArgsCorrect()) {
            return;
        }

        try (Socket socket = new Socket("localhost", port)) {
            try (BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                 BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                System.out.println(reader.readLine());

                String signUp = console.readLine();

                while (true) {
                    writer.write(signUp + "\n");
                    writer.flush();

                    String answer = reader.readLine();
                    System.out.println(answer);

                    if (answer.equals("Enter username:")) {
                        break;
                    }

                    signUp = console.readLine();
                }

                String name = console.readLine();

                writer.write(name + "\n");
                writer.flush();

                System.out.println(reader.readLine());

                String password = console.readLine();

                writer.write(password + "\n");
                writer.flush();

                String answer = reader.readLine();
                System.out.println(answer);

                if (answer.equals("Incorrect password") ||
                        answer.equals("Successful") ||
                        answer.equals("User with the name is already exist!")) {
                    writer.write("\n");
                    writer.flush();
                    return;
                }

                Thread thread = new Thread(() -> {
                    try {
                        while (true) {
                            String line = reader.readLine();

                            if (line == null) {
                                break;
                            }

                            if (line.equals("You have left the chat.")) {
                                System.out.println("You have left the chat.");
                                break;
                            }

                            System.out.println(line);
                        }
                    } catch (IOException ignored) {}
                });

                thread.start();

                while (true) {
                    String line = console.readLine();
                    writer.write(line + "\n");
                    writer.flush();
                    if (line.equals("exit")) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error: server is down");
        }
    }

    private boolean isArgsCorrect() {
        if (args.length != 1) {
            System.err.println("Error: wrong number of arguments");
            return false;
        }

        if (!args[0].startsWith("--server-port=")) {
            System.err.println("Error: wrong argument");
            return false;
        }

        try {
            port = Integer.parseInt(args[0].replaceFirst("--server-port=", ""));
        } catch (NumberFormatException e) {
            System.err.println("Error: port is not specified");
            return false;
        }
        return true;
    }
}
