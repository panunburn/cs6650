package server;

import java.io.IOException;
import java.util.logging.Logger;

public abstract class AbstractHandler {
    private KeyValue store;
    private Logger logger = Logger.getLogger(AbstractHandler.class.getName());
    private int port;
    public AbstractHandler() {
        this.store = new KeyValue();
    }

    public String processRequest(String request) throws IOException {
        String[] splited = request.split(" ");
        String reply = "";
        if (splited[0].equalsIgnoreCase("put")){
            if (splited.length != 3) {
                logger.info("Illegal put request, usage: put one 1");
                reply = "Illegal put request, usage: put one 1";
            }
            else {
                store.put(splited[1], splited[2]);
                reply = "put performed";
            }
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
        }
        else if (splited[0].equalsIgnoreCase("delete")){
            if (splited.length != 2) {
                logger.info("Illegal delete request, usage: delete one");
                reply = "Illegal delete request, usage: delete one";
            }
            else {
                store.delete(splited[1]);
                reply = "Delete performed!";
                logger.info("Send reply to client:\n" + reply);
            }
        }
        else {
            reply = "Illegal message sent from client, use put, get or delete.";
            logger.info(reply);
        }

        //System.out.println("Return: " + reply);
        return reply + "\n";
    }

    public abstract void start(int port) throws IOException;

    public abstract String receive() throws IOException;

    public abstract void send(String message) throws IOException;
}
