import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

public class Client {

    private void startClient() {
        Scanner scanner = new Scanner(System.in);

        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 5000))) {
            System.out.println("Connected to Server");

            while(true) {
                System.out.println("New User? (Press Y/N):");
                while(true) {
                    String userSelection = scanner.nextLine();
                    if (userSelection.equalsIgnoreCase("y")) {
                        // Register User
                        sendMessage(socketChannel, "Registering new user");
                        System.out.println("Registration:");
                        System.out.println("Enter a user name:");
                        sendMessage(socketChannel, scanner.nextLine());
                        System.out.println("Enter a password:");
                        sendMessage(socketChannel, scanner.nextLine());
                        System.out.println("Enter an email address:");
                        sendMessage(socketChannel, scanner.nextLine());
                        System.out.println(receiveMessage(socketChannel));
                        break;
                    } else if (userSelection.equalsIgnoreCase("n")) {
                        sendMessage(socketChannel, "Existing user");
                        break;
                    }
                }

                System.out.println("Enter user name: ");
                sendMessage(socketChannel, scanner.nextLine());
                if (receiveMessage(socketChannel).equals("Username OK")) {
                    System.out.println("Enter password: ");
                    sendMessage(socketChannel, scanner.nextLine());
                    if (receiveMessage(socketChannel).equals("Password OK")) {
                        break;
                    } else {
                        System.out.println("Incorrect password");
                    }
                } else {
                    System.out.println("User name not found");
                }
            }

            System.out.println("Sign-in successful");

            while (true) {
                System.out.println("Enter message: ");
                sendMessage(socketChannel, scanner.nextLine());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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
            System.out.println("Sent message to server");
            System.out.println();
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
        new Client().startClient();
    }
}
