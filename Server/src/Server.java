import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    static ArrayList<Thread> userList;

    public Server() {
        userList = new ArrayList<Thread>();
    }

    public void connect() throws Exception {
        try {
            ServerSocket serverSoc = new ServerSocket(8080);
            System.out.println("[Server] Talk Server ready for chatting");

            while (true) {
                Socket clientSoc = serverSoc.accept();
                ClientThread clientThread = new ClientThread(this, clientSoc);
                userList.add(clientThread);
                System.out.println("There are(is) " + userList.size() + " people.");
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        server.connect();
    }
}
