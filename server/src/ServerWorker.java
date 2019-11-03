import java.nio.channels.SocketChannel;

public class ServerWorker implements Runnable {
    // TODO: DB Module added as a dependency, OK?
    private CassandraDataStore cassandraDataStore;
    private SocketChannel socketChannel;
    private ServerWorkerHelper serverWorkerHelper;

    ServerWorker(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.serverWorkerHelper = new ServerWorkerHelper();
        this.cassandraDataStore = new CassandraDataStore();
    }

    @Override
    public void run() {
        System.out.println("Processing request from client...");
        String messageReceived = serverWorkerHelper.receiveMessage(socketChannel);
        System.out.println("Message received from client: " + messageReceived);

        cassandraDataStore.addMessage(messageReceived);
    }

}
