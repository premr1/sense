package com.chookie.sense.twitter;

import com.chookie.sense.infrastructure.BroadcastingServerEndpoint;
import com.chookie.sense.infrastructure.DaemonThreadFactory;
import com.chookie.sense.infrastructure.WebSocketServer;
import com.chookie.sense.twitter.connector.TwitterConnection;

import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class LiveTweetsService implements Runnable {
    private static final Logger LOGGER = Logger.getLogger("com.chookie.sense.twitter");
    private static final int PORT = 8081;
    private static final String URI = "/tweets/";

    private final ExecutorService executor = newSingleThreadExecutor(new DaemonThreadFactory());
    private final BroadcastingServerEndpoint<String> tweetsEndpoint = new BroadcastingServerEndpoint<>();
    private final WebSocketServer server = new WebSocketServer(URI, PORT, tweetsEndpoint);
    private TwitterConnection twitterConnection;

    public static void main(String[] args) {
        new LiveTweetsService().run();
    }

    public void run() {
        LOGGER.setLevel(Level.FINE);
        executor.submit(server);

        twitterConnection = new TwitterConnection(tweetsEndpoint::onMessage);
        twitterConnection.run();
    }

    public void stop() throws Exception {
        server.stop();
        twitterConnection.stop();
        executor.shutdownNow();
    }
}