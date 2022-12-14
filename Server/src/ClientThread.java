import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientThread extends Thread {
    Server server;
    Socket socket;
    InputStream input;
    OutputStream output;
    BufferedReader reader;
    PrintWriter writer;
    String readValue;
    static String nickname;
    static Scanner scanner = new Scanner(System.in);

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
            e.printStackTrace();
        } finally {
            try {
                System.out.println("[ " + socket.getInetAddress() +  " Exited ]");
                Server.userList.remove(this);
                System.out.println("There are(is) " + Server.userList.size() + " people.");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            Socket clientSoc = new Socket("192.168.35.15", 8080);

            System.out.print("닉네임을 입력해주세요: ");
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String data = reader.readLine();
            DataOutputStream output = new DataOutputStream(clientSoc.getOutputStream());
            PrintWriter writer = new PrintWriter(output, true);
            writer.println(data);

            ReceivingThread receivingThread = new ReceivingThread(clientSoc);
            SendingThread sendingThread = new SendingThread(clientSoc);

            receivingThread.start();
            sendingThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
