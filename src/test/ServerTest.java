package test;

import java.io.IOException;
import main.Server;

/**
 * Abstract class with helper methods for server testing
 * @category no_didit
 */
public abstract class ServerTest {
    public final int SERVER_PORT = 4444;
    private Thread backgroundThread;

    /**
     * Starts the server
     * @param server the server to starts
     */
    public void startServer(final Server server) {
        backgroundThread = new Thread(new Runnable() {
            public void run() {
                server.serve();
            }
        });
        backgroundThread.start();
    }

    /**
     * Stops the server
     * @param server the server to stop
     */
    public void stopServer(final Server server) {
        server.stop();
        try {
            backgroundThread.join();
        } catch (InterruptedException e) {
        }
    }    


}
