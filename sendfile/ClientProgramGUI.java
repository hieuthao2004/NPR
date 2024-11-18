package sendfile;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientProgramGUI {
    static DataOutputStream os = null;
    static DataInputStream is = null;
    
    private static JTextArea messageArea;
    private static JButton sendFileButton;
    private static JButton browseButton;
    private static JTextField filePathField;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Client Program");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        messageArea = new JTextArea();
        messageArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        filePathField = new JTextField(20);
        inputPanel.add(filePathField);

        browseButton = new JButton("Browse");
        inputPanel.add(browseButton);

        sendFileButton = new JButton("Send File");
        inputPanel.add(sendFileButton);

        panel.add(inputPanel, BorderLayout.SOUTH);
        frame.add(panel);
        frame.setVisible(true);

        browseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Open file chooser dialog
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select a file to send");
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePathField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        sendFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String filePath = filePathField.getText();
                if (!filePath.isEmpty()) {
                    sendFile(filePath);
                } else {
                    messageArea.append("Please specify a file path.\n");
                }
            }
        });
    }

    private static void sendFile(String path) {
        Socket clientSocket = null;
        
        try {
            clientSocket = new Socket("localhost", 2301);
            is = new DataInputStream(clientSocket.getInputStream());
            os = new DataOutputStream(clientSocket.getOutputStream());

            messageArea.append("Sending file...\n");
            File f = new File(path);
            if (f.exists()) {
                sendFileToServer(f);
            } else {
                messageArea.append("File not found.\n");
            }
            
            is.close();
            os.close();
            clientSocket.close();
        } catch (IOException e) {
            messageArea.append("Error connecting to server.\n");
            e.printStackTrace();
        }
    }

    private static void sendFileToServer(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        os.writeLong(file.length());
        int bytes;
        byte[] buffer = new byte[4 * 1024];
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
            os.flush();
        }
        fis.close();
        messageArea.append("File sent successfully.\n");
    }
}
