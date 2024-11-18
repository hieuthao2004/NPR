package sendfile;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ServerProgramGUI {
    private static DataInputStream is = null;
    private static DataOutputStream os = null;
    private static JTextArea messageArea;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Server Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);

        startServer();
    }

    private static void startServer() {
        ServerSocket listener = null;
        Socket connectionSocket = null;

        try {
            listener = new ServerSocket(2301);
            messageArea.append("Server started. Waiting for client...\n");

            connectionSocket = listener.accept();
            messageArea.append("Client connected.\n");

            is = new DataInputStream(connectionSocket.getInputStream());
            os = new DataOutputStream(connectionSocket.getOutputStream());

            receiveFile();

            is.close();
            os.close();
            listener.close();
            messageArea.append("Connection closed.\n");

        } catch (IOException e) {
            messageArea.append("Error in server.\n");
            e.printStackTrace();
        }
    }

    private static void receiveFile() {
        try {
            int bytes;
            long size = is.readLong();
            FileOutputStream fos = new FileOutputStream("received_file.jpg");
            byte[] buffer = new byte[4 * 1024];
            while (size > 0 && (bytes = is.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                fos.write(buffer, 0, bytes);
                size -= bytes;
            }
            fos.close();
            messageArea.append("File received successfully.\n");

        } catch (IOException e) {
            messageArea.append("Error receiving file.\n");
            e.printStackTrace();
        }
    }
}
