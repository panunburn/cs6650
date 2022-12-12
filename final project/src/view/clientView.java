package view;

import model.ManipulateData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The Dungeon view. This is a GUI for the dungeon.
 */
public class clientView extends JFrame {
    private final JMenuBar menuBar;
    private final JButton commandButton;
    private final JTextField input;

    private final JButton commandButton2;
    private final JTextField input2;

    private final JButton commandButton3;
    private final JTextField input3;

    private final JButton commandButton4;
    private final JTextField input4;
    //private PicturePanel picturePanel;

    private JLabel display;

    /**
     * A constructor construct the UI.
     */
    public clientView()  {
        super();
        this.setTitle("kvStore-Client");
        this.setSize(700, 200);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //wrap, row, col, interConnect, percentage, seed, difficulty
        this.menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Client");
        gameMenu.add(new JMenuItem("new client"));

        JMenu exitMenu = new JMenu("exit");
        exitMenu.add(new JMenuItem("Exit Client"));

        menuBar.add(gameMenu);
        menuBar.add(exitMenu);
        setJMenuBar(menuBar);

        this.setLayout(new BorderLayout());



        //button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        this.add(buttonPanel, BorderLayout.SOUTH);

        //input text field
        this.input = new JTextField(15);
        buttonPanel.add(input);

        //buttons
        this.commandButton = new JButton("Connect/Switch");
        buttonPanel.add(commandButton);

        //button panel
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new FlowLayout());
        this.add(buttonPanel2, BorderLayout.NORTH);

        //input text field
        this.input2 = new JTextField(15);
        buttonPanel2.add(input2);

        //buttons
        this.commandButton2 = new JButton("PUT");
        buttonPanel2.add(commandButton2);

        //input text field
        this.input3 = new JTextField(15);
        buttonPanel2.add(input3);

        //buttons
        this.commandButton3 = new JButton("DELETE");
        buttonPanel2.add(commandButton3);


        //button panel
        JPanel buttonPanel3 = new JPanel();
        buttonPanel3.setLayout(new FlowLayout());
        this.add(buttonPanel3, BorderLayout.CENTER);
        //input text field
        this.input4 = new JTextField(15);
        buttonPanel3.add(input4);

        //buttons
        this.commandButton4 = new JButton("GET");
        buttonPanel3.add(commandButton4);

        this.display = new JLabel();
        buttonPanel3.add(display);
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
        this.commandButton2.addActionListener(actionEvent);
        this.commandButton3.addActionListener(actionEvent);
        this.commandButton4.addActionListener(actionEvent);
    }

    /**
     * Get the dungeon setting with input box.
     * @return a string contains dungeon setting.
     */
    public String getClientSettings() {
        String command = this.input.getText();
        this.input.setText("");
        System.out.println(command);
        return command;
    }

    /**
     * Get the dungeon setting with input box.
     * @return a string contains dungeon setting.
     */
    public String getPutSettings() {
        String command = this.input2.getText();
        this.input2.setText("");
        System.out.println(command);
        return command;
    }

    /**
     * Get the dungeon setting with input box.
     * @return a string contains dungeon setting.
     */
    public String getDeleteSettings() {
        String command = this.input3.getText();
        this.input3.setText("");
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
     * Display the dungeon with model given.
     *
     * @param message the message to be displayed.
     */
    public void displayClient(String message) {
        System.out.println("display");
        this.display.setText(message);
        resetFocus();
        this.makeVisible();
    }

    public void resetFocus() {
        this.setFocusable(true);
        this.requestFocus();
    }
}

