package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPClient extends AbstractClient {
    private DatagramSocket socket;
    private byte[] buf;
    private InetAddress address;
    private int port;

    @Override
    public void connect(String host, int port) throws IOException {
        socket = new DatagramSocket();
        socket.setSoTimeout(4000);
        address = InetAddress.getByName(host);
        this.port = port;
        alive = true;
    }

    @Override
    public void send(String message) throws IOException {
        buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }

    @Override
    public String receive() throws IOException {
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        String received = new String(
                packet.getData(), 0, packet.getLength());
        return received;
    }

    @Override
    public void disconnect() throws IOException {
        socket.close();
        alive = false;
    }

    @Override
    public boolean alive() {
        return alive;
    }
}
