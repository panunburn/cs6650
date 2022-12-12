import controller.MvcControllerClient;
import controller.MvcControllerServer;
import view.*;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * A driver class implements the model, just for this project.
 */
public class serverDriver {

    private static Logger logger = Logger.getLogger(clientDriver.class.getName());

    /**
     * The main function to start the game, play as a view and controller role for this time.
     */
    public static void main(String[] args) throws IOException {
    /*System.out.println("Argument count: " + args.length);
    for (int i = 0; i < args.length; i++) {
      System.out.println("Argument " + i + ": " + args[i]);
    }
    System.out.println("Welcome to the dungeon!");*/
        MvcControllerServer controller;

        System.out.println("Usage: This is the GUI version of server, simply start!\n");
        serverView view = new serverView();
        controller = new MvcControllerServer(view);
        controller.start();
    }
}