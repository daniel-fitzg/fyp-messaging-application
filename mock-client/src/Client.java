import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.UUID;


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
        JButton signInButton = new JButton("Sign-in");
        welcomeFrame.add(signInButton);

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

                JLabel passwordLabel = new JLabel("Password: ");
                registerFrame.add(passwordLabel);
                JTextArea passwordTextArea = new JTextArea(1, 15);
                registerFrame.add(passwordTextArea);

                JButton registerButton = new JButton("Register");
                registerFrame.add(registerButton);
                registerButton.addActionListener(new ActionListener() {
                     @Override
                     public void actionPerformed(ActionEvent e) {
                         // TODO: Input validation needed here for fields
                         RegisterUser user = new RegisterUser(firstNameTextArea.getText(),
                                 lastNameTextArea.getText(),
                                 emailTextArea.getText(),
                                 passwordTextArea.getText());

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
                                 String serverResponse = (String) objectInputStream.readObject();
                                 if (serverResponse.equalsIgnoreCase("true")) {
                                     JOptionPane.showMessageDialog(registerFrame, "Registration successful");
                                 } else if (serverResponse.equalsIgnoreCase("false")) {
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

        signInButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame signInFrame = new JFrame("Sign-in Frame");
                signInFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                signInFrame.setSize(200, 200);
                signInFrame.setLayout(new FlowLayout());
                JLabel signInHeader = new JLabel("Sign-in Screen");
                signInHeader.setFont(new Font("Calibri", Font.PLAIN, 24));
                signInFrame.add(signInHeader);

                JLabel enterEmailLabel = new JLabel("Email: ");
                signInFrame.add(enterEmailLabel);
                JTextArea enterEmailTextArea = new JTextArea(1, 15);
                signInFrame.add(enterEmailTextArea);

                JLabel enterPasswordLabel = new JLabel("Password: ");
                signInFrame.add(enterPasswordLabel);
                JTextArea enterPasswordTextArea = new JTextArea(1, 15);
                signInFrame.add(enterPasswordTextArea);

                JButton signInSubmitButton = new JButton("Sign-in");
                signInFrame.add(signInSubmitButton);
                signInSubmitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // TODO: input validation needed here
                        User userToAuthenticate = new User();
                        userToAuthenticate.setEmail(enterEmailTextArea.getText());
                        userToAuthenticate.setPassword(enterPasswordTextArea.getText());

                        try {
                            URL link = new URL("http://localhost:8080/tomcat_server_war_exploded/" + "AuthenticateUser");
                            HttpURLConnection httpUrlConnection = (HttpURLConnection) link.openConnection();
                            httpUrlConnection.setDoOutput(true);
                            httpUrlConnection.setDoInput(true);
                            httpUrlConnection.setRequestProperty("Content-Type", "application/octet_stream");

                            ObjectOutputStream objectOutputStream = new ObjectOutputStream(httpUrlConnection.getOutputStream());
                            objectOutputStream.writeObject(userToAuthenticate);

                            ObjectInputStream objectInputStream = new ObjectInputStream(httpUrlConnection.getInputStream());

                            try {
                                User returnedUser = (User) objectInputStream.readObject();
                                if (returnedUser != null) {
                                    JOptionPane.showMessageDialog(signInFrame, "Sign-in successful");
                                    signInFrame.dispose();

                                    JFrame conversationListFrame = createConversationListFrame(returnedUser);

                                    if (conversationListFrame != null) {
                                        welcomeFrame.dispose();
                                        conversationListFrame.setVisible(true);
                                    }
                                } else {
                                    JOptionPane.showMessageDialog(signInFrame, "Sign-in failed");
                                    signInFrame.dispose();
                                }
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

                // TODO: Not working
                //setBounds( 400, 400, 0, 0);

                signInFrame.setVisible(true);
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
//
//        sendTextButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    String content = userEntryArea.getText();
//
//                    ConversationEntry newConversationEntry = new ConversationEntry(UUID.randomUUID(), UUID.randomUUID(),
//                            new Date(), content);
//
//                    if (!content.equalsIgnoreCase("")) {
//                        URL link = new URL("http://localhost:8080/tomcat_server_war_exploded/" + "AddConversationEntry");
//                        HttpURLConnection httpUrlConnection = (HttpURLConnection) link.openConnection();
//                        httpUrlConnection.setDoOutput(true);
//                        httpUrlConnection.setDoInput(true);
//                        httpUrlConnection.setRequestProperty("Content-Type", "application/octet_stream");
//
//                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(httpUrlConnection.getOutputStream());
//                        objectOutputStream.writeObject(newConversationEntry);
//
//                        ObjectInputStream objectInputStream = new ObjectInputStream(httpUrlConnection.getInputStream());
//
//                        String operationResult = null;
//                        try {
//                            operationResult = (String) objectInputStream.readObject();
//                        } catch (ClassNotFoundException exception) {
//                            exception.printStackTrace();
//                        }
//
//                        if (operationResult != null) {
//                            JOptionPane.showMessageDialog(messageFrame, "Message stored successfully");
//                        }
//
//                        objectOutputStream.close();
//                        objectInputStream.close();
//                    } else if (content.equalsIgnoreCase("")) {
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

    private JFrame createConversationListFrame(User user) throws IOException {
        URL link = new URL("http://localhost:8080/tomcat_server_war_exploded/" + "GetUserConversations");
        HttpURLConnection httpUrlConnection = (HttpURLConnection) link.openConnection();
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setDoInput(true);
        httpUrlConnection.setRequestProperty("Content-Type", "application/octet_stream");

        ObjectOutputStream objectOutputStream = new ObjectOutputStream(httpUrlConnection.getOutputStream());
//        objectOutputStream.writeObject(user.getUserId());
        objectOutputStream.writeObject(UUID.fromString("51dca0e3-f008-4dd6-baf8-63b60348a119"));

        ObjectInputStream objectInputStream = new ObjectInputStream(httpUrlConnection.getInputStream());

        List<Conversation> conversationList;
        try {
            conversationList = (List<Conversation>) objectInputStream.readObject();
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            return null;
        }

        JFrame conversationListFrame = new JFrame();
        conversationListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        conversationListFrame.setSize(270, 350);
        conversationListFrame.setLayout(new FlowLayout());
        JLabel signInHeader = new JLabel("Conversations");
        signInHeader.setFont(new Font("Calibri", Font.PLAIN, 24));
        conversationListFrame.add(signInHeader);

        conversationList.forEach(conversation -> {
            conversationListFrame.add(new JLabel(conversation.getSecondaryUserName()));
            JButton button = new JButton("View Messages");
            conversationListFrame.add(button);

            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    UUID conversationId = conversation.getConversationId();
                    UUID authorId = conversation.getUserId();
                    UUID secondaryAuthorId = conversation.getSecondaryUserId();

                    try {
                        URL link = new URL("http://localhost:8080/tomcat_server_war_exploded/" + "GetConversationEntries");
                        HttpURLConnection httpUrlConnection = (HttpURLConnection) link.openConnection();
                        httpUrlConnection.setDoOutput(true);
                        httpUrlConnection.setDoInput(true);
                        httpUrlConnection.setRequestProperty("Content-Type", "application/octet_stream");

                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(httpUrlConnection.getOutputStream());
                        objectOutputStream.writeObject(conversationId);
                        objectOutputStream.writeObject(authorId);
                        objectOutputStream.writeObject(secondaryAuthorId);

                        ObjectInputStream objectInputStream = new ObjectInputStream(httpUrlConnection.getInputStream());

                        List<ConversationEntry> conversationEntries = null;
                        try {
                            conversationEntries = (List<ConversationEntry>) objectInputStream.readObject();
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
        });

        objectOutputStream.close();
        objectInputStream.close();

        return conversationListFrame;
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
