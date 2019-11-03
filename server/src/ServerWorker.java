import java.nio.channels.SocketChannel;

public class ServerWorker implements Runnable {
    private SocketChannel socketChannel;

    ServerWorker(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread());
        System.out.println("Processing request from client...");
    }

}
