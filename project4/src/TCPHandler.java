

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TCPHandler extends Thread {

    //private ServerSocket server;
    private KeyValue store;
    private List<List<String>> commands;
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private static Logger logger = Logger.getLogger(TCPHandler.class.getName());

    private Paxos paxos;

    public TCPHandler(Socket socket, KeyValue store, Paxos paxos, List<List<String>> commands) {
        this.socket = socket;
        this.store = store;
        this.paxos = paxos;
        this.commands = commands;
    }

    public void run() {
        try {
            new Thread(paxos).start();
            InputStream input = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(input);
            br = new BufferedReader(isr);

            OutputStream out = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            while (true) {
                String request = receive();
                if (request != null) {
                    logger.info("Receive message from client " + socket.getInetAddress().getHostAddress() +
                ":\n" + request);
                }
                boolean exit = processRequest(request);
                if (exit) {
                    logger.info("Thread exiting!");
                    break;
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }


    public String receive() throws IOException{
        String message = br.readLine();
        return message;
    }

    public void send(String message) throws IOException{
        bw.write(message);
        bw.flush();
    }

    public boolean processRequest(String request) throws IOException, InterruptedException {
        boolean exit = false;
        String[] splited = request.split(" ");
        String reply = "";

        if (splited[0].equalsIgnoreCase("put")){
            if (splited.length != 3) {
                logger.info("Illegal put request, usage: put one 1");
                reply = "Illegal put request, usage: put one 1";
            }
            else {
                List<String> command = new ArrayList<>();
                command.add("put");
                command.add(splited[1]);
                command.add(splited[2]);
                commands.add(command);
                reply = "put command added to paxos";
                logger.info("put command added to paxos");
            }
            send(reply + "\n");
        }
        else if (splited[0].equalsIgnoreCase("get")){
            if (splited.length != 2) {
                logger.info("Illegal get request, usage: get one");
                reply = "Illegal get request, usage: get one";
            }
            else {
                reply = store.get(splited[1]);
                logger.info("Send reply to client:\n" + reply);
            }
            send(reply + "\n");
        }
        else if (splited[0].equalsIgnoreCase("delete")){
            if (splited.length != 2) {
                logger.info("Illegal delete request, usage: delete one");
                reply = "Illegal delete request, usage: delete one";
            }
            else {
                List<String> command = new ArrayList<>();
                command.add("delete");
                command.add(splited[1]);
                commands.add(command);
                reply = "Delete command added to paxos";
                logger.info("delete command added to paxos");
            }
            send(reply + "\n");
        }
        else if (splited[0].equalsIgnoreCase("exit")) {
            logger.info("User enter exit!");
            exit = true;
        }
        else if (splited[0].equalsIgnoreCase("prepare")) {
            logger.info("Receive prepare message from other server!");
            if (Integer.parseInt(splited[1]) > paxos.accepted) {
                if (paxos.acceptedCommand == null) {
                    reply = null;
                }
                else{
                    reply = "";
                    for (int i = 0; i < paxos.acceptedCommand.size(); i++) {
                        reply += paxos.acceptedCommand.get(i);
                        reply += " ";
                    }
                }
                paxos.accepted = Integer.parseInt(splited[1]);
                send(reply + "\n");
            }
        }
        else if (splited[0].equalsIgnoreCase("accept")) {
            logger.info("Receive accept message from other server!");

            if (Integer.parseInt(splited[1]) >= paxos.accepted) {
                reply = null;
                send(reply + "\n");

                if (splited[2].equalsIgnoreCase("put")) {
                    logger.info("Put performed after accept!");
                    store.put(splited[3], splited[4]);
                }
                else if (splited[2].equalsIgnoreCase("delete")) {
                    logger.info("Delete performed after accept!");
                    store.delete(splited[3]);
                }
            }
        }
        else if (splited[0].equalsIgnoreCase("sleep")) {
            sleep(Integer.parseInt(splited[1]));
        }
        else {
            reply = "Illegal message sent from client, use put, get or delete.";
            logger.info(reply);
            send(reply + "\n");
        }

        //System.out.println("Return: " + reply);
        return exit;
    }
}
