package Bank;

import java.io.*;
import javax.swing.*;
import javax.net.ssl.*;

public class LoginClient {
    private BankAccount bankAccount = new BankAccount();

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
                String userName = JOptionPane.showInputDialog(null, "Enter User Name (or Cancel to exit):");
                if (userName == null) {
                    JOptionPane.showMessageDialog(null, "Login canceled.");
                    break;
                }

                output.println(userName);

                String password = JOptionPane.showInputDialog(null, "Enter Password (or Cancel to exit):");
                if (password == null) {
                    JOptionPane.showMessageDialog(null, "Login canceled.");
                    break;
                }

                output.println(password);
                output.flush();

                String response = input.readLine();
                JOptionPane.showMessageDialog(null, response);

                if (response.startsWith("Welcome")) {
                    loginSuccessful = true;
                    openBankAccountInterface(); // Open bank account interface after login
                }
            }

            output.close();
            input.close();
            socket.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    private void openBankAccountInterface() {
        while (true) {
            String option = JOptionPane.showInputDialog(null,
                    "Welcome to the Bank Account Interface\n" +
                            "1. Check Balance\n" +
                            "2. Withdraw Money\n" +
                            "3. Exit\n" +
                            "Enter your choice:");

            if (option == null || option.equals("3")) {
                JOptionPane.showMessageDialog(null, "Thank you for using our service!");
                break;
            }

            switch (option) {
                case "1":
                    JOptionPane.showMessageDialog(null, "Your current balance is: " + bankAccount.getBalance() + " VND");
                    break;
                case "2":
                    String amountStr = JOptionPane.showInputDialog(null, "Enter amount to withdraw:");
                    if (amountStr != null) {
                        try {
                            long amount = Long.parseLong(amountStr);
                            new Thread(() -> bankAccount.withdraw(Thread.currentThread().getName(), amount)).start();
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Invalid amount. Please try again.");
                        }
                    }
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Invalid choice. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        new LoginClient();
    }
}
