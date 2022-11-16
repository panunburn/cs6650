

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.rmi.Naming;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public class ClientAppRMI {
    //private static Client client;
    private static ManipulateData m;
    private static Logger logger = Logger.getLogger(ClientApp.class.getName());

    public static void main(String[] args) throws IllegalArgumentException, IOException {
        if (args.length != 2) {
            System.out.println("Usage: java client.ClientAppRMI localhost 1234");
        }
        //java.util.logging.SimpleFormatter.format=%1$tb %1$td, %1$tY %1$tl:%1$tM:%1$tS.%1$tL %1$Tp %2$s%n%4$s: %5$s%n
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        //String protocol = args[2];
        Scanner sc = new Scanner(System.in);

        logger.info("Client start!");

        try {
            m = (ManipulateData) Naming.lookup(
                    "rmi://" + ip + ":" + port + "/StoreService");
            logger.info("Using RMI connected to ip: " + ip + " port: " + port);
            run();
        }
        catch (Exception e){
            logger.info("Exception throwed!" + e);
        }
    }

    public static String processRequest(String request) throws IOException, InterruptedException {
        //logger.info("Acquiring lock...");
        //semaphore.acquire();
        //logger.info("Got permit!");

        String[] splited = request.split(" ");
        String reply = "";
        if (splited[0].equalsIgnoreCase("put")){
            if (splited.length != 3) {
                logger.info("Illegal put request, usage: put one 1");
                reply = "Illegal put request, usage: put one 1";
            }
            else {
                m.put(splited[1], splited[2]);
                reply = "put performed";
            }
        }
        else if (splited[0].equalsIgnoreCase("get")){
            if (splited.length != 2) {
                logger.info("Illegal get request, usage: get one");
                reply = "Illegal get request, usage: get one";
            }
            else {
                reply = m.get(splited[1]);
                logger.info("Send reply to client:\n" + reply);
            }
        }
        else if (splited[0].equalsIgnoreCase("delete")){
            if (splited.length != 2) {
                logger.info("Illegal delete request, usage: delete one");
                reply = "Illegal delete request, usage: delete one";
            }
            else {
                m.delete(splited[1]);
                reply = "Delete performed!";
                logger.info("Send reply to client:\n" + reply);
            }
        }
        else {
            reply = "Illegal message sent from client, use put, get or delete.";
            logger.info(reply);
        }

        //System.out.println("Return: " + reply);
        //semaphore.release();
        //logger.info("Lock released!");
        return reply + "\n";
    }

    private static void run() throws IOException, InterruptedException {
        //System.out.println("123");
        Scanner sc = new Scanner(System.in);
        while (true) {
            String message = sc.nextLine();
            if (message.equalsIgnoreCase("exit")){
                logger.info("User enter exit");
                break;
            }
            logger.info("Reply from server!\n" + processRequest(message) + "\n");

            //logger.info("Send message to server!\n" + message);
            //client.send(message);
            //String reply = null;
            //if (reply != null){
                //logger.info("Reply from server!\n" + reply + "\n");
            //}
        }
    }

}
