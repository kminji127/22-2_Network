import java.io.*;
import java.net.Socket;

public class ClientThread extends Thread {
    Server server;
    Socket socket;
    InputStream input;
    OutputStream output;
    BufferedReader reader;
    PrintWriter writer;
    String readValue;
    String nickname;

    public ClientThread(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        System.out.println("[ " + socket.getInetAddress() + " Entered ]");
    }

    public void run() {
        try {
            input = socket.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));

            output = socket.getOutputStream();
            writer = new PrintWriter(output, true);

            while ((readValue = reader.readLine()) != null) {
                for(int i=0; i<Server.userList.size(); i++) {
                    ClientThread clientThread = (ClientThread) Server.userList.get(i);
                    clientThread.writer.println(readValue);
                    writer.flush();
                }
            }
        } catch (Exception e) {
            System.out.println("[ " + socket.getInetAddress() +  "Exited ]");
            Server.userList.remove(this);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            Socket clientSoc = new Socket("172.30.1.24", 8080);

            ReceivingThread receivingThread = new ReceivingThread(clientSoc);
            SendingThread sendingThread = new SendingThread(clientSoc);

            receivingThread.start();
            sendingThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
