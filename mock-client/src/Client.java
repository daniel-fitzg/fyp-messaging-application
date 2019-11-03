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

            while (true) {
                System.out.println("Enter messsage: ");
                sendMessage(socketChannel, scanner.nextLine());
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    void sendMessage(SocketChannel socketChannel, String message) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(message.length() + 1);
            buffer.put(message.getBytes());
            buffer.put((byte) 0x00);
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
            System.out.println("Sent message to server");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main (String[] args) {
        new Client().startClient();
    }
}
