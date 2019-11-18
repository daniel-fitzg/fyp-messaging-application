import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.UUID;

public class ServerWorker implements Runnable {
    // TODO: DB Module added as a dependency, OK?
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
        UUID userId = null;

        System.out.println("Processing request from client...");

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

        while (true) {
            System.out.println("Processing messages...");

            String messageReceived = serverWorkerHelper.receiveMessage();

            if (messageReceived.equalsIgnoreCase("quit")) {
                System.out.println("Message received from client: " + messageReceived);

                try {
                    socketChannel.close();
                    cassandraDataStore.close();
                    System.out.println("Client disconnected");
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("Message sent to DB: " + messageReceived);

            //cassandraDataStore.addMessage(UUID.randomUUID(), userId, new Date(), messageReceived);
        }
    }

}
