import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;

public class Client extends JFrame {

    Container container = getContentPane();
    JPanel panel = new JPanel();
    JPanel buttonArea = new JPanel();
    JButton submitButton = new JButton("전송");
    JButton exitButton = new JButton("닫기");
    JTextArea chattingDisplay = new JTextArea();
    JScrollPane jScrollPane = new JScrollPane(chattingDisplay);
    JTextField chattingInput = new JTextField(20);

    PrintWriter writer;
    BufferedReader reader;

    String nickname;
    String sendMessage;
    String receiveMessage;

    boolean connection = false;
    actionEvent event = new actionEvent();

    public Client(JFrame frame, String nickName) {
        nickname = nickName;

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("채팅");
        setSize(400, 600);

        chattingDisplay.setEditable(false);
        panel.setLayout(new BorderLayout());
        panel.add("Center", chattingInput);
        buttonArea.add("South", submitButton);
        buttonArea.add("South", exitButton);
        panel.add("South", buttonArea);

        container.setLayout(new BorderLayout());
        container.add("Center", jScrollPane);
        container.add("South", panel);

        submitButton.addActionListener(event);
        exitButton.addActionListener(event);
        chattingInput.addActionListener(event);

        new Thread() {
            @Override
            public void run() {
                try {
                    Socket socket = new Socket("172.30.1.24", 8080);

                    InputStream input = socket.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(input));

                    OutputStream output = socket.getOutputStream();
                    writer = new PrintWriter(output, true);

                    if (connection == false) {
                        writer.println("[" + nickname + " 님이 입장했습니다 ]");
                        connection = true;
                    }

                    // 클라이언트로부터 받은 메시지 출력
                    while(true) {
                        try {
                            receiveMessage = reader.readLine();
                            chattingDisplay.append(receiveMessage + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class actionEvent implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == chattingInput || e.getSource() == submitButton) {
                sendMessage = chattingInput.getText();
                writer.println(nickname + "\n" + sendMessage + "\n");
                writer.flush();
                chattingInput.setText("");
            }
            if(e.getSource() == exitButton) {
                writer.println("[" + nickname + " 님이 퇴장했습니다 ]");
                writer.flush();
                System.exit(0);
            }
        }
    }
}