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
        String newOrExistingUser = serverWorkerHelper.receiveMessage();
        if (newOrExistingUser.equalsIgnoreCase("Registering new user")) {
            userId = serverWorkerHelper.registerNewUser();
        } else if (newOrExistingUser.equalsIgnoreCase("Existing user")) {
            // TODO Process user login details
            while (true) {
                System.out.println("Waiting for client user name:");
                userId = serverWorkerHelper.authenticateUserName(serverWorkerHelper.receiveMessage());
                serverWorkerHelper.authenticatePassword(serverWorkerHelper.receiveMessage());
                break;
            }
        }

        while(true) {
            String messageReceived = serverWorkerHelper.receiveMessage();

            if (messageReceived.equalsIgnoreCase("quit")) {
                System.out.println("Message received from client: " + messageReceived);
                cassandraDataStore.close();
                break;
            }

            cassandraDataStore.addMessage(UUID.randomUUID(), userId, new Date(), messageReceived);
        }
    }

}
