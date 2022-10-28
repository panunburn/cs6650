import java.net.ServerSocket;
import java.net.Socket;

public class SingleThreadServer {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Wrong Input arguments! e.g.\nServer 32000");
            System.exit(1);
        }
        try {
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            Socket socket = serverSocket.accept();
            Server server = new Server(socket);
            server.start();
            try {
                server.join();
                System.out.println("Single Thread Server Exit!\n");
            }
            catch (InterruptedException IntExp){
                System.out.println(IntExp);
            }
        }
        catch (Exception e){
            System.out.println("Wrong Input arguments! e.g.\nServer 32000");
        }
    }
}
