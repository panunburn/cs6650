package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPHandler extends AbstractHandler {

    private ServerSocket server;
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;

    @Override
    public void start(int port) throws IOException {
        server = new ServerSocket(port);
        socket = server.accept();
        InputStream input = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(input);
        br = new BufferedReader(isr);

        OutputStream out = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(out);
        bw = new BufferedWriter(osw);
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
