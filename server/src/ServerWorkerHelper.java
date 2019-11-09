import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.List;

class ServerWorkerHelper {
    private List<User> users;
    private SocketChannel socketChannel;

    ServerWorkerHelper(CassandraDataStore cassandraDataStore, SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        users = cassandraDataStore.getUsers();
        System.out.println();
    }


    void authenticateUserName(String userName) {
        users.forEach(user -> {
            if (user.getUserName().equals(userName)) {
                sendMessage("Username OK");
            } else {
                sendMessage("Username not found");
            }
        });
    }

    void authenticatePassword(String password) {
        users.forEach(user -> {
            if (user.getPassword().equalsIgnoreCase(password)) {
                sendMessage("Password OK");
            } else {
                sendMessage("Incorrect Password");
            }
        });
    }


    private void sendMessage(String message) {
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

    String receiveMessage(SocketChannel socketChannel) {
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
