import java.io.*;
import java.net.*;

/**
 * The client side of TCP.
 */
public class Client {
    private Socket socket = null;
    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);


            InputStream input = System.in;
            OutputStream out = socket.getOutputStream();
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(isr);
            OutputStreamWriter osw = new OutputStreamWriter(out);
            BufferedWriter bw = new BufferedWriter(osw);

            System.out.println("Enter you string\n");
            String line = br.readLine();
            System.out.println("You entered " + line);
            bw.write(line + "\n");
            bw.flush();

            input = socket.getInputStream();
            isr = new InputStreamReader(input);
            br = new BufferedReader(isr);
            String message = br.readLine();

            System.out.println("Response from server: " + message);
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Wrong Input arguments! e.g.\nClient 127.0.0.1 32000");
            System.exit(1);
        }
        try {
            Client client = new Client(args[0], Integer.parseInt(args[1]));
        }
        catch (Exception e){
            System.out.println("Wrong Input arguments! e.g.\nClient 127.0.0.1 32000");
        }
    }
}
