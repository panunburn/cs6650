

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
 * A serverApp, enable user to start a server, can be connected by clients.
 */
public class ServerApp {
    private static Logger logger = Logger.getLogger(ServerApp.class.getName());
    //private static ArrayList<Client> servers;

    public static void main(String[] args) throws IOException, IllegalArgumentException {

        int port = Integer.parseInt(args[0]);
        int startport = Integer.parseInt(args[1]);
        int endport = Integer.parseInt(args[2]);
        logger.info("Server starts at port: " + port);

        ServerSocket serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        KeyValue store = new KeyValue();
        Proposer proposer = new Proposer(port, startport, endport);
        List<List<String>> commands = new ArrayList<>();
        Paxos paxos = new Paxos(commands, proposer, store);

        while (true) {
            Socket client = serverSocket.accept();
            logger.info("New Client : " + client.getInetAddress().getHostAddress());
            TCPHandler server = new TCPHandler(client, store, paxos, commands);
            new Thread(server).start();
            logger.info("Server listen to port: " + port);
        }
    }
}
