package Server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Server {

    static HashMap<String, String> books;
    static HashMap<String, Socket> hashMap;


    private HashMap<String, String> loadBooks() {
        HashMap<String, String> tmp = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader("Books"))) {
            String s;
            while ((s = br.readLine()) != null) {
                tmp.put(s, "vacant");
            }
        } catch (IOException e) {
            e.getStackTrace();
        }

        return tmp;
    }

    public static void main(String[] args) {
        ServerSocket s = null;
        int socketCount = 0;
        String string = new String(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()).toString());
        try {
            s = new ServerSocket(9000);
            System.out.println("Server.Server Started");
            Server server = new Server();
            hashMap = new HashMap<>();
            books = server.loadBooks();
            while (true) {
                Socket socket = s.accept();

                ClientThread clientThread = new ClientThread(socket, "User_" + socketCount);
                hashMap.put("User_" + socketCount, socket);
                clientThread.start();
                clientThread.name();
                socketCount++;

                if (socketCount >= 100) {
                    break;
                }
            }
        } catch (IOException e) {
            e.getStackTrace();
        } finally {
            try {
                if (s != null) {
                    s.close();
                }
            } catch (IOException e) {
                e.getStackTrace();
            }
        }

    }
}
