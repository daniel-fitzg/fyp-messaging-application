import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.UUID;

public class ServerWorker implements Runnable {
    private CassandraDataStore cassandraDataStore;
    private SocketChannel socketChannel;
    private ServerWorkerHelper serverWorkerHelper;

    ServerWorker(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.cassandraDataStore = new CassandraDataStore();
        this.serverWorkerHelper = new ServerWorkerHelper(cassandraDataStore, socketChannel);
    }

    @Override
    public void run() {

        System.out.println("Processing request from client...");

        String messageReceived = serverWorkerHelper.receiveMessage();

        if (!messageReceived.equalsIgnoreCase("")) {
            System.out.println("Text received: " + messageReceived);

            // Simulates a user and message ID
            UUID userId = UUID.randomUUID();
            UUID messageId = UUID.randomUUID();

            String returnedMessage = cassandraDataStore.addMessage(messageId, userId, new Date(), messageReceived);
            serverWorkerHelper.sendMessage(returnedMessage);
        }
    }
}
