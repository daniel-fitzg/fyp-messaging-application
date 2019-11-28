import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class Client extends JFrame {

    private void startClient() {

            // TODO: Basic user authentication, removed for Christmas demo
//            while(true) {
//                System.out.println("New User? (Press Y/N):");
//
//                String userInput = scanner.nextLine();
//
//                if (userInput.equalsIgnoreCase("y")) {
//                    sendMessage(socketChannel, userInput);
//
//                    System.out.println("Registration:");
//                    System.out.println("Enter a user name:");
//                    sendMessage(socketChannel, scanner.nextLine());
//                    System.out.println("Enter a password:");
//                    sendMessage(socketChannel, scanner.nextLine());
//                    System.out.println("Enter an email address:");
//                    sendMessage(socketChannel, scanner.nextLine());
//
//                    if (receiveMessage(socketChannel).equalsIgnoreCase("Registration successful")) {
//                        System.out.println("Registration complete");
//                        break;
//                    }
//                } else if (userInput.equalsIgnoreCase("n")) {
//                    sendMessage(socketChannel, userInput);
//
//                    System.out.println("Enter username: ");
//                    sendMessage(socketChannel, scanner.nextLine());
//                    if (receiveMessage(socketChannel).equalsIgnoreCase("User name OK")) {
//                        System.out.println("Enter password: ");
//                        sendMessage(socketChannel, scanner.nextLine());
//                        if (receiveMessage(socketChannel).equalsIgnoreCase("Password OK")) {
//                            System.out.println("Sign-in successful");
//                            break;
//                        } else {
//                            System.out.println("Incorrect password");
//                        }
//                    } else {
//                        System.out.println("User name not found");
//                    }
//                }
//            }

            JFrame messageFrame = new JFrame("User Conversation");
            messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            messageFrame.setSize(400, 200);
            messageFrame.setLayout(new FlowLayout());
            JLabel header = new JLabel("Messaging Application Demo");
            header.setFont(new Font("Calibri", Font.PLAIN, 24));
            messageFrame.add(header);
            JLabel userEntryLabel = new JLabel("Enter text: ");
            messageFrame.add(userEntryLabel);
            JTextArea userEntryArea = new JTextArea(1, 15);
            messageFrame.add(userEntryArea);
            JButton sendTextButton = new JButton("SEND");
            messageFrame.add(sendTextButton);
            JLabel returnTextLabel = new JLabel("Text returned from server: ");
            messageFrame.add(returnTextLabel);
            JTextField returnTextField = new JTextField(15);
            messageFrame.add(returnTextField);

            sendTextButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 5000))) {
                        String message = userEntryArea.getText();
                        sendMessage(socketChannel, message);
                        returnTextField.setText(receiveMessage(socketChannel));
                    } catch (IOException exception) {
                      exception.printStackTrace();
                    }
                }
            });

            messageFrame.setVisible(true);
    }

    private void sendMessage(SocketChannel socketChannel, String message) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(message.length() + 1);
            buffer.put(message.getBytes());
            buffer.put((byte) 0x00);
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private String receiveMessage(SocketChannel socketChannel) {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            String message = "";

            while (socketChannel.read(byteBuffer) > 0) {
                char byteRead = 0x00;
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    byteRead = (char) byteBuffer.get();
                    if (byteRead == 0x00) {
                        break;
                    }
                    message += byteRead;
                }
                if (byteRead == 0x00) {
                    break;
                }
                byteBuffer.clear();
            }

            return message;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return "";
    }

    public static void main (String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Client client = new Client();
                client.startClient();
            }
        });
    }
}
