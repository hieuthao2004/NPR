import java.io.*;
import javax.swing.*;
import javax.net.ssl.*;

public class LoginClient {
    // LoginClient constructor
    public LoginClient() {
        System.setProperty("javax.net.ssl.trustStore", "./SSLStore");
        System.setProperty("javax.net.ssl.trustStorePassword", "hanu123");

        try {
            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) socketFactory.createSocket("localhost", 7070);
            PrintWriter output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            boolean loginSuccessful = false;
            while (!loginSuccessful) {
                // Prompt user for username
                String userName = JOptionPane.showInputDialog(null, "Enter User Name (or Cancel to exit):");
                if (userName == null) {
                    JOptionPane.showMessageDialog(null, "Login canceled.");
                    break;
                }

                // Send username to server
                output.println(userName);

                // Prompt user for password
                String password = JOptionPane.showInputDialog(null, "Enter Password (or Cancel to exit):");
                if (password == null) {
                    JOptionPane.showMessageDialog(null, "Login canceled.");
                    break;
                }

                // Send password to server
                output.println(password);
                output.flush();

                // Read response from server
                String response = input.readLine();
                JOptionPane.showMessageDialog(null, response);

                if (response.startsWith("Welcome")) {
                    loginSuccessful = true;
                }
            }

            // Clean up
            output.close();
            input.close();
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new LoginClient();
    }
}
