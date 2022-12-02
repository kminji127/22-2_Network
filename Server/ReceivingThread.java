import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReceivingThread extends Thread {
    Socket socket = null;

    public ReceivingThread(Socket socket) {
        this.socket = socket;
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
