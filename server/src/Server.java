import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private ThreadPoolExecutor threadPoolExecutor;

    private Server(int maxThreads) {
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThreads);
    }

    private void serveRequests() {
        // Channel that listens for incoming TCP connections
        ServerSocketChannel serverSocketChannel;

        try {
            serverSocketChannel = ServerSocketChannel.open();
            // TODO: Current hostname address allows only local clients to connect to server
            serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 5000));

            while(true) {
                System.out.println("Waiting for client connection...");
                SocketChannel socketChannel = serverSocketChannel.accept();
                System.out.println("Connection successful");
                threadPoolExecutor.execute(new ServerWorker(socketChannel));
            }

        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static void main (String[] args) {
        System.out.println("Starting server...");
        new Server(10).serveRequests();
    }
}
