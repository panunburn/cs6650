
import view.*;
import controller.*;


import java.io.IOException;
import java.util.logging.Logger;

/**
 * A driver class implements the model, just for this project.
 */
public class clientDriver {

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
        MvcControllerClient controller;

        System.out.println("Usage: This is the GUI version of client, simply start!\n");
        clientView view = new clientView();
        controller = new MvcControllerClient(view);
        controller.start();
    }
}
