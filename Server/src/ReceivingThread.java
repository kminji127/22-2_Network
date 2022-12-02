import java.io.*;
import java.net.Socket;

public class ReceivingThread extends Thread {
    Socket socket = null;
    String nickname;


    public ReceivingThread(Socket socket) throws Exception {
        this.socket = socket;
//        DataInputStream input = new DataInputStream(socket.getInputStream());
//        this.nickname = input.readUTF();
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while(true) {
                System.out.println(reader.readLine() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
