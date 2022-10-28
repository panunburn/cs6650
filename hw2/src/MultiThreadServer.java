import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class MultiThreadServer {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Wrong Input arguments! e.g.\nSingleThreadServer 32000");
            System.exit(1);
        }

        ServerSocket server = null;
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket client = serverSocket.accept();
                System.out.println("New Client :" + client.getInetAddress().getHostAddress() + "\n");
                Server clientSock = new Server(client);
                new Thread(clientSock).start();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
