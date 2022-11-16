

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerApp {
    private static Logger logger = Logger.getLogger(ServerApp.class.getName());

    public static void main(String[] args) throws IOException, IllegalArgumentException {
        logger.info("Server starts");
        int port = Integer.parseInt(args[0]);
        //String protocol = args[1];

        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        KeyValue store = new KeyValue();
        while (true) {
            Socket client = serverSocket.accept();
            logger.info("New Client : "+ client.getInetAddress().getHostAddress());
            TCPHandler server = new TCPHandler(client, store);
            new Thread(server).start();
            logger.info("Server listen to port: " + port);
        }
    }
}
