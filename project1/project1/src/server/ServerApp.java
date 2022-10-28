package server;

import java.io.IOException;
import java.util.logging.Logger;

public class ServerApp {
    private static boolean tcp;
    private static AbstractHandler server;
    private static Logger logger = Logger.getLogger(ServerApp.class.getName());
    public static void main(String[] args) throws IOException, IllegalArgumentException {
        logger.info("Server starts");
        int port = Integer.parseInt(args[0]);
        String protocol = args[1];

        if (protocol.equalsIgnoreCase("tcp")){
            tcp = true;
            server = new TCPHandler();
            logger.info("Connection type: tcp");
        }
        else if (protocol.equalsIgnoreCase("udp")){
            tcp = false;
            server = new UDPHandler();
            logger.info("Connection type: udp");
        }
        else {
            logger.warning("Protocol type error!");
            throw new IllegalArgumentException("Protocol type error!");
        }
        logger.info("Server listen to port: " + port);
        server.start(port);
        run();
    }

    private static void run() throws IOException {
        //System.out.println("123");
        while (true) {
            String request = server.receive();
            if (request != null) {
                logger.info("Receive message from client:\n" + request);
            }
            String reply = server.processRequest(request);
            server.send(reply);
        }

    }
}
