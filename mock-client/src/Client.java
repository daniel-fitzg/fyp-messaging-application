import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {

    private void startClient() {
        try (SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 5000))) {
            System.out.println("Connected to Server");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main (String[] args) {
        new Client().startClient();


    }
}
