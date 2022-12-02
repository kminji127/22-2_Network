import java.io.*;
import java.net.Socket;

public class ReceivingThread extends Thread {
    Socket socket = null;
    String nickname;


    public ReceivingThread(Socket socket) throws Exception {
        this.socket = socket;
        InputStream input = socket.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        this.nickname = reader.readLine();
    }

    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            while(true) {
                System.out.println(reader.readLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
