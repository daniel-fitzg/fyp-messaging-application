

public class Server {
    private Server() {
        new ServerThreadPool();
    }

    public static void main (String[] args) {
        new Server();
    }
}
