package test;

import java.io.IOException;
import main.Server;

public class ServerTest {
    public final int SERVER_PORT = 4444;
    
    /**
     * Starts the server
     * @param server the server to starts
     */
    public void startServer(final Server server) {
        Thread backgroundThread = new Thread(new Runnable() {
            public void run() {
                try {
                    server.serve();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        backgroundThread.start();
    }

    /**
     * Stops the server
     * @param server the server to stop
     * @param out the User thread to kill
     */
    public void stopServer(final Server server) {
        try {
            server.stop();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }    
    
    
}
