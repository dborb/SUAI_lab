import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Client {
    private String clientName = "defaultName";
    private String host;
    private Integer port;
    private DatagramSocket datagramSocket;
    private InetAddress address;
    private byte[] dataBuffer;

    public Client(String adress, int port) {
        try {
            this.address = InetAddress.getByName(adress);
            this.port = port;
            datagramSocket = new DatagramSocket();
            dataBuffer = new byte[1024];
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public void setName(String string) {
        clientName = new String(string);
    }

    public String getName() {
        return clientName;
    }

    private void sendMessage() {
        Scanner sc = new Scanner(System.in);


        while (true) {
            System.out.print("Type message : ");
            String message = new String(sc.nextLine());
            if (message.length() > 6) {
                String cmp = new String(message.getBytes(), 0, 6);
                if (cmp.compareTo("@name ") == 0) {
                    this.setName(new String(message.getBytes(), 6, message.length() - 6));
                    continue;
                }
            }
            if (message.compareTo("@quit") == 0) {
                datagramSocket.close();
                System.exit(0);
            }

            try {
                String fullMessage = new String(clientName + ": " + message);
                byte[] data = fullMessage.getBytes();

                DatagramPacket outPacket = new DatagramPacket(data, data.length, address, port);
                datagramSocket.send(outPacket);
            } catch (IOException e) {
                System.out.print(e.getMessage());
            } catch (IllegalArgumentException e){
                System.out.println("port must be > 0");
                System.exit(0);
            }
        }
    }

    private void recieveMessage() {

        try {
            DatagramPacket datagramPacket = new DatagramPacket
                    (dataBuffer, dataBuffer.length);
            while (true) {
                datagramSocket.receive(datagramPacket);
                String answ = new String(datagramPacket.getData(), datagramPacket.getOffset(),
                        datagramPacket.getLength());
                System.out.println("Server response: " + answ);
            }
        } catch (IOException e) {
            if(datagramSocket != null){
                datagramSocket.close();
            }
            System.out.print(e.getMessage());
        }catch (IllegalArgumentException e){
            System.out.println("port must be > 0");
            System.exit(0);
        }

    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            String adr;
            Integer port;
            System.out.print("Enter serverAdress: ");
            adr = scanner.next();
            System.out.print("Enter portNumber: ");
            port = scanner.nextInt();
            System.out.println("Avaliable options: \n@name [your name] to change your name in chat\n" +
                    "[message] + [enter] to send message\n@quit to close chat");

            Client client = new Client(adr, port);


            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.sendMessage();
                }
            }).start();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    client.recieveMessage();
                }
            }).start();
        } catch (InputMismatchException e) {
            System.out.println("Try another port or addres input");
        }


    }
}
