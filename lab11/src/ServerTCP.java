import java.io.*;
import java.net.*;

public class ServerTCP extends Thread{

    private Socket socket;
    private BufferedReader bufferedReader;

    public ServerTCP(Socket socket) throws IOException {
        this.socket = socket;
        bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        start();
    }

    @Override
    public void run() {
        try {
            String comand;
            while (true) {
                String msg = bufferedReader.readLine();

                    comand = msg.substring(0, 5);

                    if(comand.equals("@send")){
                        String fileName = "ServerSaving_" + msg.substring(6, msg.length());
                        StringBuilder stringBuffer = new StringBuilder("");
                        String s;

                        while(!(s = bufferedReader.readLine()).equals("EOF")){
                            stringBuffer.append(s + "\n");
                        }
                        write(stringBuffer.toString(), fileName);
                    }

                else {
                    System.out.println("Test : " + msg);
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void write(String text, String pathName){
        try (BufferedWriter br = new BufferedWriter(new FileWriter(pathName))){
            br.write(text);
            br.flush();
        }
        catch (IOException e){
            System.out.print(e.getMessage());
        }
    }

    private void sendMessage(){
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader inu = new
                    BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String fserver;
                while ((fserver = inu.readLine()) != null) {
                    out.println(fserver);

                }
                if(fserver.equals("@exit")){
                    break;
                }
            }
        }catch (IOException e){
            e.getStackTrace();
        }
    }

    public static void main(String[] args) {
        ServerSocket s = null;
        try {
            s = new ServerSocket(9000);
            System.out.println("Server Started");

            Socket socket = s.accept();

            ServerTCP server = new ServerTCP(socket);

            new Thread(() -> server.sendMessage()).start();

        } catch (IOException e) {
            e.getStackTrace();
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                e.getStackTrace();
            }
        }

    }
}
