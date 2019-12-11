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

        JFrame welcomeFrame = new JFrame("Welcome Frame");
        welcomeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        welcomeFrame.setSize(250, 200);
        welcomeFrame.setLayout(new FlowLayout());
        JLabel welcomeHeader = new JLabel("Welcome Screen");
        welcomeHeader.setFont(new Font("Calibri", Font.PLAIN, 24));
        welcomeFrame.add(welcomeHeader);
        JButton registerButton = new JButton("Register");
        welcomeFrame.add(registerButton);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame registerFrame = new JFrame("Welcome Frame");
                registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                registerFrame.setSize(280, 200);
                registerFrame.setLayout(new FlowLayout());
                JLabel registerHeader = new JLabel("Register New User");
                registerHeader.setFont(new Font("Calibri", Font.PLAIN, 24));
                registerFrame.add(registerHeader);

                JLabel firstNameLabel = new JLabel("First name: ");
                registerFrame.add(firstNameLabel);
                JTextArea firstNameTextArea = new JTextArea(1, 15);
                registerFrame.add(firstNameTextArea);

                JLabel lastNameLabel = new JLabel("Last name: ");
                registerFrame.add(lastNameLabel);
                JTextArea lastNameTextArea = new JTextArea(1, 15);
                registerFrame.add(lastNameTextArea);

                JLabel emailLabel = new JLabel("Email: ");
                registerFrame.add(emailLabel);
                JTextArea emailTextArea = new JTextArea(1, 15);
                registerFrame.add(emailTextArea);

                JButton registerButton = new JButton("Register");
                registerFrame.add(registerButton);
                registerButton.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                         // TODO: Input validation needed here for fields
                         RegisterUser user = new RegisterUser(firstNameTextArea.getText(), lastNameTextArea.getText(), emailTextArea.getText());

                         try {
                             URL link = new URL("http://localhost:8080/tomcat_server_war_exploded/" + "RegisterUser");
                             HttpURLConnection httpUrlConnection = (HttpURLConnection) link.openConnection();
                             httpUrlConnection.setDoOutput(true);
                             httpUrlConnection.setDoInput(true);
                             httpUrlConnection.setRequestProperty("Content-Type", "application/octet_stream");

                             ObjectOutputStream objectOutputStream = new ObjectOutputStream(httpUrlConnection.getOutputStream());
                             objectOutputStream.writeObject(user);

                             ObjectInputStream objectInputStream = new ObjectInputStream(httpUrlConnection.getInputStream());
                              try {
                                 if (objectInputStream.readObject().equals("true")) {
                                     JOptionPane.showMessageDialog(registerFrame, "Registration successful");
                                 } else if (objectInputStream.readObject().equals("false")) {
                                     JOptionPane.showMessageDialog(registerFrame, "Registration failed");
                                 }
                              } catch (ClassNotFoundException exception) {
                                 exception.printStackTrace();
                              }

                             registerFrame.dispose();

                             objectOutputStream.close();
                             objectInputStream.close();
                         } catch (IOException exception) {
                             exception.printStackTrace();
                         }

                     }
                 });

                // TODO: Not working
                //setBounds( 400, 400, 0, 0);

                registerFrame.setVisible(true);
            }
        });

        welcomeFrame.setVisible(true);

//        JFrame messageFrame = new JFrame("User Conversation");
//        messageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        messageFrame.setSize(400, 200);
//        messageFrame.setLayout(new FlowLayout());
//        JLabel messageHeader = new JLabel("Messaging Application Demo");
//        messageHeader.setFont(new Font("Calibri", Font.PLAIN, 24));
//        messageFrame.add(messageHeader);
//        JLabel userEntryLabel = new JLabel("Enter text: ");
//        messageFrame.add(userEntryLabel);
//        JTextArea userEntryArea = new JTextArea(1, 15);
//        messageFrame.add(userEntryArea);
//        JButton sendTextButton = new JButton("SEND");
//        messageFrame.add(sendTextButton);
//        JLabel returnTextLabel = new JLabel("Text returned from server: ");
//        messageFrame.add(returnTextLabel);
//        JTextField returnTextField = new JTextField(15);
//        messageFrame.add(returnTextField);
//
//        sendTextButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    String message = userEntryArea.getText();
//
//                    if (!message.equalsIgnoreCase("")) {
//                        URL link = new URL("http://localhost:8080/tomcat_server_war_exploded/" + "AddMessage");
//                        HttpURLConnection httpUrlConnection = (HttpURLConnection) link.openConnection();
//                        httpUrlConnection.setDoOutput(true);
//                        httpUrlConnection.setDoInput(true);
//                        httpUrlConnection.setRequestProperty("Content-Type", "application/octet_stream");
//
//                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(httpUrlConnection.getOutputStream());
//                        objectOutputStream.writeObject(message);
//
//                        ObjectInputStream objectInputStream = new ObjectInputStream(httpUrlConnection.getInputStream());
//                        try {
//                            returnTextField.setText((String) objectInputStream.readObject());
//                        } catch (ClassNotFoundException exception) {
//                            exception.printStackTrace();
//                        }
//
//                        objectOutputStream.close();
//                        objectInputStream.close();
//                    } else if (message.equalsIgnoreCase("")) {
//                        JOptionPane.showMessageDialog(messageFrame, "Please enter valid text");
//                    }
//                } catch (IOException exception) {
//                  exception.printStackTrace();
//                }
//            }
//        });
//
//        messageFrame.setVisible(true);
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
