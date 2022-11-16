

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class TCPHandler extends AbstractHandler {

    //private ServerSocket server;
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private static Logger logger = Logger.getLogger(ServerApp.class.getName());
    public TCPHandler(Socket socket, KeyValue store) {
        this.socket = socket;
        this.store = store;
    }
    @Override
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(input);
            br = new BufferedReader(isr);

            OutputStream out = socket.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(out);
            bw = new BufferedWriter(osw);
            while (true) {
                String request = receive();
                if (request != null) {
                    logger.info("Receive message from client " + socket.getInetAddress().getHostAddress() +
                ":\n" + request);
                }
                String reply = processRequest(request);
                send(reply);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public String receive() throws IOException{
        String message = br.readLine();
        return message;
    }

    @Override
    public void send(String message) throws IOException{
        bw.write(message);
        bw.flush();
    }
}
