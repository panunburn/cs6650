package view;

import model.ManipulateData;
import model.ManipulateDataImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * The Dungeon view. This is a GUI for the dungeon.
 */
public class serverView extends JFrame {
    private final JMenuBar menuBar;
    private final JButton commandButton;
    private final JButton refreshButton;
    private final JTextField input;

    private final JLabel message;
    private  JPanel storePanel;

    /**
     * A constructor construct the UI.
     */
    public serverView()  {
        super();
        this.setTitle("kvStore-Server");
        this.setSize(700, 700);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Server");
        gameMenu.add(new JMenuItem("new Server"));

        JMenu exitMenu = new JMenu("exit");
        exitMenu.add(new JMenuItem("Exit Server"));

        menuBar.add(gameMenu);
        menuBar.add(exitMenu);
        setJMenuBar(menuBar);

        this.setLayout(new BorderLayout());


        message = new JLabel("Server starts");
        //button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        this.add(buttonPanel, BorderLayout.SOUTH);

        //input text field
        this.input = new JTextField(15);
        buttonPanel.add(input);

        //buttons
        this.commandButton = new JButton("Set port");
        this.refreshButton = new JButton("Refresh");
        buttonPanel.add(commandButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(message);


    }

    /**
     * Make the view visible. This is usually called
     * after the view is constructed
     */
    public void makeVisible() {
        this.setVisible(true);
    }

    /**
     * Signal the view to draw itself.
     */
    public void refresh() {
        System.out.println("Refresh is called");
        this.repaint();
    }

    /**
     * Provide the view with an action listener.
     *
     * @param actionEvent the listener.
     */
    public void setListener(ActionListener actionEvent) {
        for (int i = 0; i < menuBar.getMenuCount(); i++) {
            JMenu currentMenu1 = menuBar.getMenu(i);
            for (int j = 0; j < currentMenu1.getItemCount(); j++) {
                JMenuItem currentItem = currentMenu1.getItem(j);
                if (currentItem != null) {
                    currentItem.addActionListener(actionEvent);
                }
            }
        }
        this.commandButton.addActionListener(actionEvent);
        this.refreshButton.addActionListener(actionEvent);
    }

    /**
     * Get the dungeon setting with input box.
     * @return a string contains dungeon setting.
     */
    public String getServerSettings() {
        String command = this.input.getText();
        this.input.setText("");
        System.out.println(command);
        return command;
    }

    /**
     * Transmit an error message to the view, in case
     * the command could not be processed correctly.
     *
     * @param error the error message.
     */
    public void showErrorMessage(String error) {
        System.out.println("Error: " + error);
        JOptionPane.showMessageDialog(this,error,"Error",JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Transmit a message to the view, display to the user.
     *
     * @param message the message.
     */
    public void showMessage(String message) {
        System.out.println("Message: " + message);
        //System.out.println("showMessage: called");
        JOptionPane.showMessageDialog(this, message,"Notice",JOptionPane.INFORMATION_MESSAGE);
        resetFocus();
        this.makeVisible();
    }


    /**
     * Display the dungeon with model given.
     *
     * @param model the model to be used.
     */
    public void displayServer(ManipulateData model) {
        System.out.println("display");
        if (model == null) {
            return;
        }
            storePanel = new JPanel();
            JScrollPane scrollPane = new JScrollPane(storePanel);
            scrollPane.setAutoscrolls(true);
            scrollPane.setPreferredSize(new Dimension(500, 500));
            this.add(scrollPane);

        storePanel.removeAll();
        String display = "<html>Data:<br/>";


        for (Map.Entry<String, String> set :
                ((ManipulateDataImpl) model).store.store.entrySet()) {

            // Printing all elements of a Map
            System.out.println(set.getKey() + " = "
                    + set.getValue());
            display += set.getKey() + " : "
                    + set.getValue() + "<br/>";
        }

        display += "</html>";
        System.out.println(display);
        JLabel label = new JLabel(display);
        storePanel.add(label);

        resetFocus();
        this.makeVisible();
    }

    public void resetFocus() {
        this.setFocusable(true);
        this.requestFocus();
    }
}