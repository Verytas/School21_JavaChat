package edu.school21.sockets.server;

import edu.school21.sockets.config.SocketsApplicationConfig;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.services.MessagesService;
import edu.school21.sockets.services.UsersService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MultiServer extends Thread {

    private final BufferedReader reader;
    private final BufferedWriter writer;

    public MultiServer(Socket socket) throws IOException {
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        start();
    }

    @Override
    public void run() {
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext(SocketsApplicationConfig.class);
            UsersService usersService = context.getBean(UsersService.class);
            MessagesService messagesService = context.getBean(MessagesService.class);

            writer.write("Hello from Server!\n");
            writer.flush();

            String answer;

            while (true) {
                answer = reader.readLine();

                if (!answer.equals("signUp") && !answer.equals("signIn")) {
                    writer.write("Error: command not found\n");
                    writer.flush();
                } else {
                    break;
                }
            }

            writer.write("Enter username:\n");
            writer.flush();

            String name = reader.readLine();

            writer.write("Enter password:\n");
            writer.flush();

            String password = reader.readLine();

            if (answer.equals("signUp")) {
                writer.write(usersService.signUp(name, password));
                writer.flush();
            } else {
                if (!usersService.isUserValid(name, password)) {
                    writer.write("Incorrect password\n");
                    writer.flush();
                    return;
                }

                Server.servers.add(this);

                writer.write("Start messaging\n");
                writer.flush();

                while(true) {
                    answer = reader.readLine();

                    if (!answer.equals("exit")) {
                        messagesService.saveMessage(new Message(answer, Timestamp.valueOf(LocalDateTime.now())));
                    }

                    if (answer.equals("exit")) {
                        writer.write("You have left the chat.\n");
                        writer.flush();
                        Server.servers.removeIf(server -> server == this);
                        break;
                    }

                    for (MultiServer server : Server.servers) {
                        server.sendMessage(name + ": " + answer);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String answer) {
        try {
            writer.write(answer + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
