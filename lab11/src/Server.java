import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Server {

    private DatagramSocket datagramSocket;
    private InetAddress address;
    private Integer port;
    private String receivedData;
    private byte[] dataBuffer;
    private Integer clientPort;

    public Server(int portNumber){
        try {
            datagramSocket = new DatagramSocket(portNumber);
            port = portNumber;
            dataBuffer = new byte[1024];
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    public void listener() {
        try {
            DatagramPacket datagramPacket = new DatagramPacket(dataBuffer, dataBuffer.length);

            while (true) {


                datagramSocket.receive(datagramPacket);

                address = datagramPacket.getAddress();
                clientPort = datagramPacket.getPort();

                receivedData = new String(datagramPacket.getData(),
                        datagramPacket.getOffset(), datagramPacket.getLength());

                System.out.println(receivedData);
            }
        }catch (IOException e) {
                System.out.print(e.getMessage());
        }

    }

    private void respond() {
        while (true) {
            try {
                Scanner sc = new Scanner(System.in);
                String answ;
                answ = sc.nextLine();
                byte[] sendData = answ.getBytes();
                DatagramPacket outServerPacket = new DatagramPacket(sendData, sendData.length,
                        address, clientPort);
                datagramSocket.send(outServerPacket);
                        } catch (IOException e) {
                System.out.print(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(Integer.parseInt(args[0]));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    server.listener();
                }
            }).start();


            new Thread(new Runnable() {
                @Override
                public void run() {
                    server.respond();
                }
            }).start();

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }
}
