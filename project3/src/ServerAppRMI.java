import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.logging.Logger;

public class ServerAppRMI {
    private static Logger logger = Logger.getLogger(ServerAppRMI.class.getName());

    private static ManipulateData coordinator;
    public static void main(String[] args) throws IOException, IllegalArgumentException {
        logger.info("Server starts");
        int port = Integer.parseInt(args[0]);
        String coord_ip = null;
        int coord_port = 0;
        if (args.length == 3) {
            coord_ip = args[1];
            coord_port = Integer.parseInt(args[2]);
        }
        try {
            //m = (ManipulateData) Naming.lookup(
            //        "rmi://" + ip + ":" + port + "/StoreService");
            //logger.info("Using RMI connected to ip: " + ip + " port: " + port);
            ManipulateData m = new ManipulateDataImpl(port, coord_ip, coord_port);
            LocateRegistry.createRegistry(port);
            Naming.rebind("rmi://localhost:"+ port + "/StoreService", m);
            logger.info("Server starts in ip: localhost, port: " + port);
        } catch (Exception e) {
            System.out.println("Trouble: " + e);
        }

        if (coord_port != 0) {
            try {
                logger.info("Using RMI connected to coordinator, ip: " + coord_ip + " port: " + coord_port);
                coordinator = (ManipulateData) Naming.lookup(
                        "rmi://" + coord_ip + ":" + coord_port + "/StoreService");

                coordinator.register("localhost", port);
            } catch (Exception e) {
                logger.info("Exception throwed!" + e);
                logger.info("Connection to coordinator failed!");
                System.exit(0);
            }
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
