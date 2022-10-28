import java.io.*;
import java.net.*;

/**
 * The server side of TCP.
 */
public class Server extends Thread {

    public String res;
    private Socket socket;

    private char swapCase(char c) {
        return Character.isUpperCase(c) ?
                Character.toLowerCase(c) :
                Character.toUpperCase(c);
    }

    public Server(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {

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
            for (int i = 0; i < line.length(); i++) {
                ch = line.charAt(i); //extracts each character
                ch = swapCase(ch);
                nstr = ch + nstr; //adds each character in front of the existing string
            }
            out = socket.getOutputStream();
            osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);

            bw.write(nstr + "\n");
            bw.flush();

            res = nstr;
            //System.out.println("Response from server: " + message);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
