import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Logger;

public class ServerAppRMI {
    private static Logger logger = Logger.getLogger(ServerApp.class.getName());

    public static void main(String[] args) throws IOException, IllegalArgumentException {
        logger.info("Server starts");
        int port = Integer.parseInt(args[0]);
        //String protocol = args[1];
        try {
            ManipulateData m = new ManipulateDataImpl();
            LocateRegistry.createRegistry(port);
            Naming.rebind("rmi://localhost:"+ port + "/StoreService", m);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }
        //ServerSocket serverSocket = new ServerSocket(port);
        //serverSocket.setReuseAddress(true);
        //KeyValue store = new KeyValue();
        //while (true) {
            //Socket client = serverSocket.accept();
            //logger.info("New Client : "+ client.getInetAddress().getHostAddress());
            //TCPHandler server = new TCPHandler(client, store);
            //new Thread(server).start();
            //logger.info("Server listen to port: " + port);

        //}
    }
}
