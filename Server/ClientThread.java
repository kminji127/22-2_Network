import java.io.*;
import java.net.Socket;

public class ClientThread {
    public static void main(String[] args) throws Exception {

        try {
            Socket clientSoc = new Socket("172.30.1.24", 8080);
            System.out.println("[Client] Connected to Server");

            ReceivingThread receivingThread = new ReceivingThread(clientSoc);
            SendingThread sendingThread = new SendingThread(clientSoc);

            receivingThread.start();
            sendingThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
