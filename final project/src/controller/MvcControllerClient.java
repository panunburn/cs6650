package controller;

import model.Action;
import model.ManipulateData;
import view.clientView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

/**
 * This is a controller that take control of model and view.
 * It supports GUI but not command line version.
 */
public class MvcControllerClient implements ActionListener {
    private clientView view;
    private ManipulateData model;
    private String[] args;
    private static Logger logger = Logger.getLogger(MvcControllerClient.class.getName());

    /**
     * A constructor set default model and view.
     *
     * @param view the view that want to be started.
     */
    public MvcControllerClient(clientView view) {
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

        String ret = "";
        try {
            if (actionEvent.getSource() instanceof JMenuItem) {
                JMenuItem item = (JMenuItem) actionEvent.getSource();
                System.out.println(item.getText());
                if (item.getText().equals("Exit Client")) {
                    System.exit(0);
                }
                else if (item.getText().equals("new client")) {
                    //Process process = Runtime.getRuntime().exec("java clientDriver /c start");
                    //view = new clientView();
                    //start();
                }

            }
            if (actionEvent.getSource() instanceof JButton) {
                if (((JButton) actionEvent.getSource()).getActionCommand().equals("Connect/Switch")) {
                    String input = view.getClientSettings();
                    String[] args = input.split("\\s+");
                    System.out.println(input);
                    ret = processSetting(args);

                }
                else if (((JButton) actionEvent.getSource()).getActionCommand().equals("PUT")) {
                    String input = view.getPutSettings();
                    System.out.println("Put called");
                    System.out.println(input);
                    String[] args = input.split("\\s+");
                    processPut(args);
                    ret = "Put performed! " + "\n";
                }
                else if (((JButton) actionEvent.getSource()).getActionCommand().equals("DELETE")) {
                    String input = view.getDeleteSettings();
                    System.out.println("Delete called");
                    System.out.println(input);
                    String[] args = input.split("\\s+");
                    ret = processDelete(args);
                }
                else if (((JButton) actionEvent.getSource()).getActionCommand().equals("GET")) {
                    String input = view.getGetSettings();
                    System.out.println("Get called");
                    System.out.println(input);
                    String[] args = input.split("\\s+");
                    ret = "Get! " + processGet(args) + "\n";
                }
                view.displayClient(ret);
                view.refresh();
            }
        }
        catch (Exception e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    private String processSetting(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println(Arrays.toString(args));
            String error = "wrong args! please enter by following order\n"
                    + "ip port\n";
            throw new IllegalArgumentException(error);
        }
        String ip = args[0];
        int port = Integer.parseInt(args[1]);
        model = (ManipulateData) Naming.lookup(
                "rmi://" + ip + ":" + port + "/StoreService");
        logger.info("Using RMI connected to ip: " + ip + " port: " + port);
        return "Using RMI connected to ip: " + ip + " port: " + port;
    }

    private String processPut(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println(Arrays.toString(args));
            String error = "wrong args! please enter by following order\n"
                    + "one 1\n";
            throw new IllegalArgumentException(error);
        }
        String key = args[0];
        String value = args[1];
        String reply = "";
        try {
            String s = CompletableFuture.supplyAsync(() -> {
                        try {
                            return model.commit(Action.PUT, args[0], args[1]);
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
        return reply;

    }

    private String processDelete(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println(Arrays.toString(args));
            String error = "wrong args! please enter by following order\n"
                    + "one\n";
            throw new IllegalArgumentException(error);
        }
        String key = args[0];
        //String value = args[1];
        String reply = "";
        try {
            String ret = CompletableFuture.supplyAsync(() -> {
                        try {
                            return model.commit(Action.DELETE, args[0], null);
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
        return reply;
    }

    private String processGet(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println(Arrays.toString(args));
            String error = "wrong args! please enter by following order\n"
                    + "one\n";
            throw new IllegalArgumentException(error);
        }
        String key = args[0];
        //String value = args[1];
        return model.get(key);
    }
}
