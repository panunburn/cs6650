package controller;

import model.ManipulateData;
import model.ManipulateDataImpl;
import view.clientView;
import view.serverView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Logger;

/**
 * This is a controller that take control of model and view.
 * It supports GUI but not command line version.
 */
public class MvcControllerServer implements ActionListener {
    private serverView view;
    private String[] args;
    private ManipulateData model;
    private ManipulateData coordinator;
    private HashMap<String, String> store;

    private static Logger logger = Logger.getLogger(MvcControllerClient.class.getName());

    /**
     * A constructor set default model and view.
     *
     * @param view the view that want to be started.
     */
    public MvcControllerServer(serverView view) {
        this.view = view;
    }

    /**
     * Start the game with model provided.
     *
     */
    public void start() throws IOException {
        this.view.setListener(this);
        this.view.makeVisible();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        System.out.println(actionEvent.getSource());
        try {
            if (actionEvent.getSource() instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) actionEvent.getSource();
                System.out.println(item.getText());
                if (item.getText().equals("Exit Server")) {
                    System.exit(0);
                }
                else if (item.getText().equals("new server")) {
                    //Process process = Runtime.getRuntime().exec("java clientDriver /c start");
                    //view = new clientView();
                    //start();
                }

            }
            if (actionEvent.getSource() instanceof JButton) {
                if (!((JButton) actionEvent.getSource()).getActionCommand().equals("Refresh")){
                    String input = view.getServerSettings();
                    String[] args = input.split("\\s+");
                    processSetting(args);

                }
                //System.out.println(((JButton) actionEvent.getSource()).getActionCommand());
                view.displayServer(model);
                view.refresh();
            }
        }
        catch (Exception e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    private void processSetting(String[] args) throws Exception {
        int port;
        String coord_ip = null;
        int coord_port = 0;

        if (args.length == 1) {
            System.out.println(Arrays.toString(args));
            port = Integer.parseInt(args[0]);
            model = new ManipulateDataImpl(port, coord_ip, coord_port, view);
        }
        else if (args.length == 3) {
            System.out.println(Arrays.toString(args));
            port = Integer.parseInt(args[0]);
            coord_ip = args[1];
            coord_port = Integer.parseInt(args[2]);
            if (port == coord_port) {
                model = new ManipulateDataImpl(port, null, 0, view);
            }
            else {
                model = new ManipulateDataImpl(port, coord_ip, coord_port, view);
            }
        }
        else {
            System.out.println(Arrays.toString(args));
            String error = "wrong args! usage:\n"
                    + "<port> <coord_ip=localhost> <coord_port=1234>\n";
            throw new IllegalArgumentException(error);
        }
        LocateRegistry.createRegistry(port);
        Naming.rebind("rmi://localhost:"+ port + "/StoreService", model);
        logger.info("Server starts in ip: localhost, port: " + port);
        view.showMessage("Server starts in ip: localhost, port: " + port);

        if (coord_ip != null && coord_port != port) {
            logger.info("Using RMI connected to coordinator, ip: " + coord_ip + " port: " + coord_port);
            coordinator = (ManipulateData) Naming.lookup(
                    "rmi://" + coord_ip + ":" + coord_port + "/StoreService");
            coordinator.register("localhost", port);
        }
    }
}
