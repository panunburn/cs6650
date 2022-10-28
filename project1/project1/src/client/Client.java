package client;

import java.io.IOException;

/*
 * A client Interface, enable user to connect to a server, send and receive messages.
 */
public interface Client {

    /**
     * Connect to server with host address and port number.
     * @param host server ip address.
     * @param port port number that listen and send to.
     * @throws IOException throws exception when connection fails.
     */
    void connect(String host, int port) throws IOException;

    /**
     * Send message to server.
     * @param message message string send to server.
     * @throws IOException throws exception when send fails.
     */
    void send(String message) throws IOException;

    /**
     * Receive message from server.
     * @return message sent from server.
     * @throws IOException throws exception when receive fails.
     */
    String receive() throws IOException;

    /**
     * Disconnect to serve.
     * @throws IOException throws exception when disconnection fails.
     */
    void disconnect() throws IOException;

    /**
     * Get the status of client.
     * @return true if alive, false if not.
     */
    boolean alive();
}
