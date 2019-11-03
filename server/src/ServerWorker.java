import java.nio.channels.SocketChannel;

public class ServerWorker implements Runnable {
    private SocketChannel socketChannel;
    private ServerWorkerHelper serverWorkerHelper;

    ServerWorker(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.serverWorkerHelper = new ServerWorkerHelper();
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread());
        System.out.println("Processing request from client...");

        System.out.println(serverWorkerHelper.receiveMessage(socketChannel));
    }

}
