import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {
    private ThreadPoolExecutor threadPoolExecutor;

    private Server(int maxThreads) {
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(maxThreads);
    }

    public static void main (String[] args) {
        new Server(10);
    }
}
