import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


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
                    try {
                        String message = userEntryArea.getText();

                        if (!message.equalsIgnoreCase("")) {
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
                        } else if (message.equalsIgnoreCase("")) {
                            JOptionPane.showMessageDialog(messageFrame, "Please enter valid text");
                        }
                    } catch (IOException exception) {
                      exception.printStackTrace();
                    }
                }
            });

            messageFrame.setVisible(true);
    }

    public static void main (String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Client().startClient();
            }
        });
    }
}
