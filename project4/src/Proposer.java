import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Proposer {
    private ArrayList<Client> servers;
    private static Logger logger = Logger.getLogger(Proposer.class.getName());
    private int myport, startport, endport;
    public Proposer(int myPort, int startPort, int endPort) throws IOException {
        this.myport = myPort;
        this.startport = startPort;
        this.endport = endPort;
    }

    public void init() {
        servers = new ArrayList<Client>();
        for (int i = startport; i <= endport; i++) {
            if (i == myport) {
                continue;
            }

            try {
                Client newServer = new TCPClient();
                newServer.connect("localhost", i);
                logger.info("Connected to server at port: " + i);
                servers.add(newServer);
            }
            catch (Exception e) {
                logger.info("Failed to Connect to server at port: " + i);
            }
        }
    }

    public boolean prepare(int timestamp) throws IOException {
        boolean ret = false;
        if (servers == null) {
            init();
        }
        int total = 0;
        for (int i = 0; i < servers.size(); i++) {
            logger.info("Send prepare message to server " + i);
            servers.get(i).send("prepare " + timestamp);
            String reply = null;
            try {
                reply = servers.get(i).receive();
                total++;
            }
            catch (SocketTimeoutException e) {
                logger.info("Time out for waiting server response!");
            }
            String[] replies = reply.split(" ");
            if (replies[0] == "put" || replies[0] == "delete"){
                logger.info("Reply from server!\n" + reply);
            }
            else {
                logger.info("Reply is null\n");
            }

            if (total > (endport-startport+1)/2) {
                logger.info("Majority wins! total response is " + total);
                ret = true;
            }
        }
        return ret;
    }

    public boolean accept(int timestamp, List<String> command) throws IOException {
        boolean ret = false;
        if (servers == null) {
            init();
        }
        int total = 0;
        for (int i = 0; i < servers.size(); i++) {
            logger.info("Send accept message to server " + i);
            String send = "";
            for (int j = 0; j < command.size(); j++) {
                send += command.get(j);
                send += " ";
            }
            servers.get(i).send("accept " + timestamp + " " + send);
            String reply = null;
            try {
                reply = servers.get(i).receive();
                total++;
            }
            catch (SocketTimeoutException e) {
                logger.info("Time out for waiting server response!");
            }
            if (reply != null){
                logger.info("Reply from server!\n" + reply + "\n");
            }

            if (total > (endport-startport+1)/2) {
                logger.info("Majority wins! total response is " + total);
                ret = true;
            }
        }
        return ret;
    }
}