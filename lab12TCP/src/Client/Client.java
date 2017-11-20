package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {
    private String clientName = "defaultName";
    private Socket socket;

    private Client(String adress, int port) {
        try {
            socket = new Socket(InetAddress.getByName(adress), port);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void getMessage() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String fromserver;
            while (true) {
                    fromserver = in.readLine();
                    if(fromserver == null)
                        continue;
                    if(fromserver.compareTo("@exit") == 0){
                        break;
                    }
                    System.out.println(fromserver);

            }
        } catch (IOException e) {
            e.getStackTrace();
        }
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
                if (fuser.equals("exit")) {
                    break;
                }
                out.println(fuser);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    public void setName(String string) {
        clientName = string;
    }

    public String getName() {
        return clientName;
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 9000);
        System.out.println("Client.Client started");

        new Thread(new Runnable() {
            @Override
            public void run() {
                client.sendMessage();
            }
        }).start();

        System.out.println();
        System.out.println();

        new Thread(new Runnable() {
            @Override
            public void run() {
                client.getMessage();
            }
        }).start();

    }
}
