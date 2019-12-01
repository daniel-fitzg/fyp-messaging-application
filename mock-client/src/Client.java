import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;


public class Client extends JFrame {

    private void startClient() {

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
                        //returnTextField.setText(receiveMessage(socketChannel));

                        URL link = new URL("http://localhost:8080/tomcat_server_war_exploded/" + "AddMessage");
                        HttpURLConnection httpUrlConnection = (HttpURLConnection) link.openConnection();
                        httpUrlConnection.setDoOutput(true);
                        httpUrlConnection.setDoInput(true);
                        httpUrlConnection.setRequestProperty("Content-Type", "application/octet_stream");

                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(httpUrlConnection.getOutputStream());
                        objectOutputStream.writeObject(message);

                        ObjectInputStream objectInputStream = new ObjectInputStream(httpUrlConnection.getInputStream());

                        try {
                            returnTextField.setText((String) objectInputStream.readObject());
                        } catch (ClassNotFoundException exception) {
                            exception.printStackTrace();
                        }

                        objectOutputStream.close();
                        objectInputStream.close();
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
