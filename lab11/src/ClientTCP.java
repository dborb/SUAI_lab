import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ClientTCP {
    private Socket socket;
    private InetAddress address;
    private int port;
    private BufferedReader bufferedReader;

    public ClientTCP(String adress, int port) {
        try {
            this.address = InetAddress.getByName(adress);
            this.port = port;
            socket = new Socket(InetAddress.getByName("localhost"), port);
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public String readFile(String pathName) {
        StringBuilder stringBuilder = new StringBuilder("");
        try (BufferedReader br = new BufferedReader(new FileReader(pathName))) {
            String s;
            while ((s = br.readLine()) != null) {
                if(stringBuilder.length() > 1){
                    stringBuilder.append("\n");
                }
                stringBuilder.append(s);
            }
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
        return stringBuilder.toString();
    }

    private void sendMessage() {
        try {
            PrintWriter out = new
                    PrintWriter(socket.getOutputStream(), true);
            BufferedReader inu = new
                    BufferedReader(new InputStreamReader(System.in));
            String fuser;
            while (true) {
                fuser = inu.readLine();


                String msg = fuser.substring(0, 5);
                if(msg.equals("@exit")){
                    break;
                }
                if (msg.equals("@send")) {
                    String filename = fuser.substring(6, fuser.length());
                    StringBuilder stringBuilder = new StringBuilder("");
                    stringBuilder.append(fuser + "\n");
                    stringBuilder.append(readFile(filename));
                    stringBuilder.append("\n" + "EOF");
                    out.println(stringBuilder.toString());
                } else {
                    out.println(fuser);
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public void getMessage() {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true) {
                String msg = bufferedReader.readLine();
                if(msg.equals("@exit")){
                    break;
                }
                System.out.println("Server response : " + msg);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public static void main(String[] args) {
        ClientTCP client = new ClientTCP("localhost", 9000);

        new Thread(() -> client.sendMessage()).start();

        new Thread(() -> client.getMessage()).start();

    }
}
