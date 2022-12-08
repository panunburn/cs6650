

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * A tcp client can connect to a server and send messages, can be used by anyone including a server.
 */
public class TCPClient extends AbstractClient {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;


    @Override
    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        socket.setSoTimeout(3000);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        alive = true;
    }

    @Override
    public void send(String message) throws IOException {
        out.println(message);
    }

    @Override
    public String receive() throws IOException {
        String resp = in.readLine();
        return resp;
    }

    @Override
    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
        alive = false;
    }

    @Override
    public boolean alive() {
        return alive;
    }
}
