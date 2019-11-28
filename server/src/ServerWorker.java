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

        // TODO: Basic user authentication, removed for Christmas demo
//        while (true) {
//            String userInput = serverWorkerHelper.receiveMessage();
//            if (userInput.equalsIgnoreCase("y")) {
//                while (!serverWorkerHelper.registerNewUser()) {
//                    System.out.println("New user registration failed");
//                }
//                break;
//            } else if (userInput.equalsIgnoreCase("n")) {
//                while (!serverWorkerHelper.authenticateUser()) {
//                    System.out.println("User authentication failed");
//                }
//                break;
//            }
//        }

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
