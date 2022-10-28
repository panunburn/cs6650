package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPHandler extends AbstractHandler {
    private DatagramSocket socket;
    private byte[] buf;
    private InetAddress address;
    private int port;

    @Override
    public void start(int port) throws IOException {
        socket = new DatagramSocket(port);
    }

    @Override
    public String receive() throws IOException {
        buf = new byte[500];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
        this.address = packet.getAddress();
        this.port = packet.getPort();
        String received = new String(
                packet.getData(), 0, packet.getLength());

        return received;
    }

    @Override
    public void send(String message) throws IOException {
        buf = message.getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }
}
