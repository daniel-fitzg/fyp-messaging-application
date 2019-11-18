import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.List;
import java.util.UUID;

class ServerWorkerHelper {
    private CassandraDataStore cassandraDataStore;
    private SocketChannel socketChannel;
    private List<User> users;

    ServerWorkerHelper(CassandraDataStore cassandraDataStore, SocketChannel socketChannel) {
        this.cassandraDataStore = cassandraDataStore;
        this.socketChannel = socketChannel;
        users = cassandraDataStore.getUsers();
    }

    boolean registerNewUser() {
        String userName = receiveMessage();
        String password = receiveMessage();
        String email = receiveMessage();

        User user = cassandraDataStore.addUser(userName, password, new Date(), email);
        if (user.getUserId() == null) {
            sendMessage("Registration successful");
            return true;
        }

        return false;
    }

    boolean authenticateUser() {
        // if username not found return false
        String userName = receiveMessage();

        for (User user : users) {
            if (user.getUserName().equalsIgnoreCase(userName)) {
                System.out.println("User name OK");
                sendMessage("User name OK");
                if (receiveMessage().equalsIgnoreCase(user.getPassword())) {
                    System.out.println("Password OK");
                    sendMessage("Password OK");
                    return true;
                }
            }
        }

        sendMessage("User name not found");
        return false;
    }

    void sendMessage(String message) {
        try {
            ByteBuffer buffer = ByteBuffer.allocate(message.length() + 1);
            buffer.put(message.getBytes());
            buffer.put((byte) 0x00);
            buffer.flip();
            while (buffer.hasRemaining()) {
                socketChannel.write(buffer);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    String receiveMessage() {
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            String message = "";

            while (socketChannel.read(byteBuffer) > 0) {
                char byteRead = 0x00;
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    byteRead = (char) byteBuffer.get();
                    if (byteRead == 0x00) {
                        break;
                    }
                    message += byteRead;
                }
                if (byteRead == 0x00) {
                    break;
                }
                byteBuffer.clear();
            }
            return message;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "";
    }

}
