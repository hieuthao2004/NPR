import javax.swing.*;
import javax.net.ssl.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class LoginClient extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JTextArea responseArea;

    public LoginClient() {
        // Thiết lập SSL
        System.setProperty("javax.net.ssl.trustStore", "./SSLStore");
        System.setProperty("javax.net.ssl.trustStorePassword", "hanu123");

        // Cấu hình giao diện JFrame
        setTitle("Login Client");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Tạo các thành phần giao diện
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("Password:"));
        inputPanel.add(passwordField);

        loginButton = new JButton("Login");
        responseArea = new JTextArea();
        responseArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(responseArea);

        add(inputPanel, BorderLayout.NORTH);
        add(loginButton, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        // Xử lý sự kiện khi bấm nút "Login"
        loginButton.addActionListener(e -> attemptLogin());

        setVisible(true);
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Both fields must be filled.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Kết nối đến server
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 7070);
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Gửi thông tin đăng nhập
            output.println(username);
            output.println(password);

            // Nhận phản hồi từ server
            String response = input.readLine();
            responseArea.append("Server: " + response + "\n");

            if (response.startsWith("Welcome")) {
                JOptionPane.showMessageDialog(this, "Login successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                socket.close();
                System.exit(0);
            }

            // Đóng kết nối nếu không thành công
            socket.close();
        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error connecting to server.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LoginClient::new);
    }
}
