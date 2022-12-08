import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Paxos extends Thread {
    private Proposer proposer;
    private static Logger logger = Logger.getLogger(Paxos.class.getName());
    public int timestamp;
    public int accepted;

    public List<String> acceptedCommand;
    private List<List<String>> commands;

    private KeyValue store;

    public Paxos(List<List<String>> commands, Proposer proposer, KeyValue store) {
        this.commands = commands;
        this.proposer = proposer;
        this.store = store;
        timestamp = 0;
        accepted = 0;
    }

    @Override
    public void run() {
        logger.info("Paxos running!");
        while (true) {
            //logger.info("running!");
            if (!commands.isEmpty()) {
                logger.info("Pop command from command list!");
                List<String> command = commands.get(0);
                while (true) {
                    timestamp++;
                    try {
                        if (proposer.prepare(timestamp)) {
                            break;
                        }
                        else {
                            sleep(100);
                        }
                    } catch (Exception e) {
                        logger.info("Error! " + e);
                    }
                }
                try {
                    if (proposer.accept(timestamp, command)) {
                        if (command.get(0) == "put"){
                            store.put(command.get(1), command.get(2));
                        }
                        else if (command.get(0) == "delete") {
                            store.delete(command.get(1));
                        }
                        commands.remove(0);
                    }
                }
                catch (Exception e) {
                    logger.info("Error! " + e);
                }
            }
            else {
                try {
                    sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }
}
