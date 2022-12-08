

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientApp {
    private static Client client;
    private static boolean tcp;

    private static Logger logger = Logger.getLogger(ClientApp.class.getName());

    public static void main(String[] args) throws IllegalArgumentException, IOException {
        if (args.length != 2) {
            System.out.println("Usage: java client.ClientApp localhost 1234");
        }
        //java.util.logging.SimpleFormatter.format=%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS.%1$tL %1$Tp %2$s%n%4$s: %5$s%n
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        //String protocol = args[2];
        Scanner sc = new Scanner(System.in);

        logger.info("Client start!");
        client = new TCPClient();
        tcp = true;
        logger.info("Connection type: tcp");

        client.connect(ip, port);
        logger.info("Connected to ip: " + ip + " port: " + port);
        run();
    }

    private static void run() throws IOException {
        //System.out.println("123");
        Scanner sc = new Scanner(System.in);
        while (true) {
            logger.info("Please enter command");
            String message = sc.nextLine();
            if (message.equalsIgnoreCase("exit")){
                logger.info("User enter exit");
                client.send("exit");
                client.disconnect();
                break;
            }
            else if (message.equalsIgnoreCase("switch")) {
                logger.info("Please enter new ip and port");
                message = sc.nextLine();
                String[] splited = message.split(" ");
                while (splited.length != 2) {
                    logger.info("Wrong input. Usage: localhost 1234");
                }
                Client newClient = new TCPClient();
                try {
                    newClient.connect(splited[0], Integer.parseInt(splited[1]));
                    //client.disconnect();
                    client.send("exit");
                    client = newClient;
                    logger.info("Connected to ip: " + splited[0] + " port: " + Integer.parseInt(splited[1]));
                }
                catch (Exception e) {
                    logger.info("Error: " + e);
                }
                continue;
            }
            logger.info("Send message to server!\n" + message);
            client.send(message);
            String reply = null;
            try {
                reply = client.receive();
            }
            catch (SocketTimeoutException e) {
                logger.info("Time out for waiting server response!");
            }
            if (reply != null){
                logger.info("Reply from server!\n" + reply + "\n");
            }
        }
    }


}
