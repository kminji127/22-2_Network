import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ClientMain extends JFrame implements ActionListener {
    JPanel nicknamePanel;
    JTextField nicknameField;
    JLabel alert;
    JButton confirm;

    public ClientMain() {
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("채팅 입장");
        setResizable(false);
        setLocationRelativeTo(null);

        Font font_b = new Font("맑은 고딕", Font.BOLD, 20);
        Font font_r = new Font("맑은 고딕", Font.PLAIN, 15);

        nicknamePanel = new JPanel();
        nicknamePanel.setBackground(new Color(255,255,255,100));
        nicknamePanel.setBorder(new EmptyBorder(40, 50, 40, 50));
        nicknamePanel.setLayout(null);

        alert = new JLabel("닉네임을 입력해주세요.");
        alert.setFont(font_b);
        nicknameField = new JTextField(20);
        confirm = new JButton("확인");
        confirm.setFont(font_r);

        alert.setBounds(140, 50, 300, 40);
        nicknameField.setBounds(100, 100, 300, 40);
        confirm.setBounds(180, 170, 120, 50);
        confirm.setBackground(new Color(242, 216, 189,255));

        nicknamePanel.add(alert);
        nicknamePanel.add(nicknameField);
        nicknamePanel.add(confirm);

        add(nicknamePanel);
        confirm.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        String nickname = nicknameField.getText();
        Client client = new Client(this, nickname);
        client.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String[] args) {
        ClientMain clientMain = new ClientMain();
        clientMain.setVisible(true);
    }
}