import java.io.*;
import java.net.*;

/**
 * The server side of TCP.
 */
public class Server {


    private char swapCase(char c)
    {
        return Character.isUpperCase(c) ?
                Character.toLowerCase(c) :
                Character.toUpperCase(c);
    }

    public Server(int port) {
        try {

            ServerSocket server = new ServerSocket(port);
            Socket socket = server.accept();


            InputStream input = socket.getInputStream();
            OutputStream out = System.out;
            InputStreamReader isr = new InputStreamReader(input);
            BufferedReader br = new BufferedReader(isr);
            OutputStreamWriter osw = new OutputStreamWriter(out);
            BufferedWriter bw = new BufferedWriter(osw);

            System.out.println("Waiting for client inputs\n");
            String line = br.readLine();
            System.out.println("Client entered " + line);

            String nstr = "";
            char ch;
            for (int i = 0; i < line.length(); i++)
            {
                ch = line.charAt(i); //extracts each character
                ch = swapCase(ch);
                nstr= ch + nstr; //adds each character in front of the existing string
            }
            out = socket.getOutputStream();
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);

            bw.write(nstr + "\n");
            bw.flush();

            //System.out.println("Response from server: " + message);
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Wrong Input arguments! e.g.\nServer 32000");
            System.exit(1);
        }
        try {
            Server server = new Server(Integer.parseInt(args[0]));
        }
        catch (Exception e){
            System.out.println("Wrong Input arguments! e.g.\nServer 32000");
        }
    }
}
