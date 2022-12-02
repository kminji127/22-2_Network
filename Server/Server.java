import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Thread {
    static ArrayList<Socket> userList = new ArrayList<Socket>();
    static Socket socket = null;

    public Server(Socket socket) {
        this.socket = socket; // 유저 소켓 할당
        userList.add(socket); // 유저 리스트에 추가
    }

    public void run() {
        try {
            System.out.println("[Server] Connected to IP " + socket.getInetAddress());

            // 클라이언트로부터 메시지 수신
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // 클라이언트에게 메시지 전송
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);
            writer.print("[Server] 서버에 연결되었습니다. 닉네임을 입력해주세요 :");

            String readValue;
            String nickname = null;
            boolean identify = false;
            while ((readValue = reader.readLine()) != null) {
                if(!identify) {
                    nickname = readValue; // 닉네임 등록
                    identify = true;
                    writer.println("[Server] " + nickname + " 님이 입장했습니다.");
                    continue;
                }
                // 각 클라이언트에 메시지 전송
                for(int i=0; i<userList.size(); i++) {
                    output = userList.get(i).getOutputStream();
                    writer = new PrintWriter(output, true);
                    writer.println(nickname + " : " + readValue);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        try {
            ServerSocket serverSoc = new ServerSocket(8080);
            System.out.println("[Server] Talk Server ready for chatting");

            while (true) {
                // 클라이언트 접속 시 Thread 안에 클라이언트 정보를 담음
                Socket clientSoc = serverSoc.accept();
                Thread thread = new Server(clientSoc);
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
