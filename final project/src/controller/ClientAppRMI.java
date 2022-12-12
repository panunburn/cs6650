package controller;

import model.Action;
import model.ManipulateData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ClientAppRMI {
    //private static Client client;
    private static ManipulateData m;
    private static Logger logger = Logger.getLogger(ClientAppRMI.class.getName());

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

    public static String processRequest(String request) throws IOException, InterruptedException, NotBoundException {
        //logger.info("Acquiring lock...");
        //semaphore.acquire();
        //logger.info("Got permit!");

        String[] splited = request.split(" ");
        String reply = "";
        if (splited[0].equalsIgnoreCase("put")){
            if (splited.length != 3) {
                //logger.info("Illegal put request, usage: put one 1");
                reply = "Illegal put request, usage: put one 1";
            }
            else {
                try {
                    String s = CompletableFuture.supplyAsync(() -> {
                                try {
                                    return m.commit(Action.PUT, splited[1], splited[2]);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                } catch (MalformedURLException e) {
                                    throw new RuntimeException(e);
                                } catch (NotBoundException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .get(1, TimeUnit.SECONDS);
                    reply = "Put performed!";
                } catch (TimeoutException e) {
                    //System.out.println("Time out has occurred");
                    reply = "Time out!";
                } catch (InterruptedException | ExecutionException e) {
                    // Handle
                }


            }
        }
        else if (splited[0].equalsIgnoreCase("get")){
            if (splited.length != 2) {
                //logger.info("Illegal get request, usage: get one");
                reply = "Illegal get request, usage: get one";
            }
            else {
                reply = m.get(splited[1]);
                //logger.info("Send reply to client:\n" + reply);
            }
        }
        else if (splited[0].equalsIgnoreCase("delete")){
            if (splited.length != 2) {
                //logger.info("Illegal delete request, usage: delete one");
                reply = "Illegal delete request, usage: delete one";
            }
            else {
                try {
                    String ret = CompletableFuture.supplyAsync(() -> {
                                try {
                                    return m.commit(Action.DELETE, splited[1], null);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                } catch (RemoteException e) {
                                    throw new RuntimeException(e);
                                } catch (MalformedURLException e) {
                                    throw new RuntimeException(e);
                                } catch (NotBoundException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .get(1, TimeUnit.SECONDS);
                    reply = "Delete performed!";
                } catch (TimeoutException e) {
                    //System.out.println("Time out has occurred");
                    reply = "Time out!";
                } catch (InterruptedException | ExecutionException e) {
                    // Handle
                }

                //logger.info("Send reply to client:\n" + reply);
            }
        }
        else if (splited[0].equalsIgnoreCase("switch")) {
            if (splited.length != 3) {
                //logger.info("Illegal delete request, usage: delete one");
                reply = "Illegal switch request, usage: switch localhost 1235";
            }
            else {
                m = (ManipulateData) Naming.lookup(
                        "rmi://" + splited[1] + ":" + splited[2] + "/StoreService");
                logger.info("Reconnecting to server ip: " + splited[1] + " port: " + splited[2]);
                //logger.info("Send reply to client:\n" + reply);
                reply = "Reconnect success!";
            }
        }
        else {
            reply = "Illegal message sent from client, use put, get or delete.";
            //logger.info(reply);
        }

        //System.out.println("Return: " + reply);
        //semaphore.release();
        //logger.info("Lock released!");
        return reply;
    }

    private static void run() throws IOException, InterruptedException, NotBoundException {
        //System.out.println("123");
        Scanner sc = new Scanner(System.in);
        while (true) {
            String message = sc.nextLine();
            if (message.equalsIgnoreCase("exit")){
                logger.info("User enter exit");
                break;
            }
            String ret = processRequest(message);
            logger.info("Reply from server!\n" +  ret+ "\n");

            //logger.info("Send message to server!\n" + message);
            //client.send(message);
            //String reply = null;
            //if (reply != null){
                //logger.info("Reply from server!\n" + reply + "\n");
            //}
        }
    }

}
