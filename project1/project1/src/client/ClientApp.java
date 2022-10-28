package client;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientApp {
    private static Client client;
    private static boolean tcp;

    private static Logger logger = Logger.getLogger(ClientApp.class.getName());

    public static void main(String[] args) throws IllegalArgumentException, IOException {
        if (args.length != 3) {
            System.out.println("Usage: java client.ClientApp localhost 1234");
        }
        //java.util.logging.SimpleFormatter.format=%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS.%1$tL %1$Tp %2$s%n%4$s: %5$s%n
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        String protocol = args[2];
        Scanner sc = new Scanner(System.in);

        logger.info("Client start!");
        if (protocol.equalsIgnoreCase("tcp")){
            client = new TCPClient();
            tcp = true;
            logger.info("Connection type: tcp");
        }
        else if (protocol.equalsIgnoreCase("udp")){
            client = new UDPClient();
            tcp = false;
            logger.info("Connection type: udp");
        }
        else {
            throw new IllegalArgumentException("Invalid Protocol type.\n");
        }
        client.connect(ip, port);
        logger.info("Connected to ip: " + ip + " port: " + port);
        run();
    }

    private static void run() throws IOException {
        //System.out.println("123");
        Scanner sc = new Scanner(System.in);
        while (true) {
            String message = sc.nextLine();
            if (message.equalsIgnoreCase("exit")){
                logger.info("User enter exit");
                client.disconnect();
                break;
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
