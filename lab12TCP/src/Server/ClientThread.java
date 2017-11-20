package Server;

import Server.Server;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ClientThread extends Thread {
    private String threadName;
    private Socket socket;
    private BufferedReader bufferedReader;

    ClientThread(Socket s, String name) throws IOException {
        threadName = name;
        socket = s;
        bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }


    private void returnBook(String bookName){
        try{
            PrintWriter pr = new PrintWriter(Server.hashMap.get(threadName).getOutputStream(), true);
            if (Server.books.containsKey(bookName) && Server.books.get(bookName).compareTo(threadName) == 0) {
                Server.books.remove(bookName);
                Server.books.put(bookName, "vacant");
                pr.println("You successefully return book!");
            } else if (!Server.books.containsKey(bookName)) {
                pr.println("Sorry, there is no such book :(");
            } else {
                pr.println("You can't return untaked book");
            }
        }catch (IOException e){
            e.getStackTrace();
        }
    }

    private void takeBook(String bookName) throws IOException{
        try {
            PrintWriter pr = new PrintWriter(Server.hashMap.get(threadName).getOutputStream(), true);
            if (Server.books.containsKey(bookName) && Server.books.get(bookName).compareTo("vacant") == 0) {
                Server.books.remove(bookName);
                Server.books.put(bookName, threadName);
                pr.println("You take " + bookName + " book");
            } else if (!Server.books.containsKey(bookName)) {
                pr.println("Sorry, there is no such book :(");
            } else {
                pr.println("Sorry, this book already taken by " + Server.books.get(bookName));
            }
        }catch (IOException e){
            e.getStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            while (true) {
                    String msg;
                    try(Scanner scanner = new Scanner(msg = bufferedReader.readLine())) {

                        String comand = scanner.next();
                        synchronized (this) {
                            if (comand.compareTo("@exit") == 0) {
                                break;
                            }

                            if (comand.compareTo("@senduser") == 0) {
                                String user = scanner.next();
                                sendPrivate(user, scanner.nextLine());
                                continue;
                            }
                            if (comand.compareTo("@name") == 0) {
                                name();
                                continue;
                            }

                            if (comand.compareTo("@list") == 0) {
                                books();
                                continue;
                            }
                            //todo warnings
                            //todo try-with-res
                            //todo javadoc
                            if (comand.compareTo("@return") == 0) {
                                StringBuilder book = new StringBuilder(scanner.nextLine());
                                book.deleteCharAt(0);
                                returnBook(book.toString());
                                continue;
                            }

                            if (comand.compareTo("@take") == 0) {
                                StringBuilder book = new StringBuilder(scanner.nextLine());
                                book.deleteCharAt(0);
                                takeBook(book.toString());
                                continue;
                            }
                        }
                    }

                    sendAll(msg);
                }
            } catch(IOException e){
                e.getStackTrace();
            } finally{
                try {
                    socket.close();
                } catch (IOException e) {
                    e.getStackTrace();
                }
            }

    }

    private synchronized void books() {
        try {
            StringBuilder stringBuilder = new StringBuilder("");
            for (Map.Entry<String, String> entry : Server.books.entrySet()) {
                stringBuilder.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
            }
            PrintWriter pr = new PrintWriter(Server.hashMap.get(threadName).getOutputStream(), true);
            pr.println(stringBuilder.toString());
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    synchronized void name() {
        PrintWriter pr = null;
        try {
            pr = new PrintWriter(Server.hashMap.get(threadName).getOutputStream(), true);
            pr.println("Your name is: " + threadName);
        } catch (IOException e) {
            e.getStackTrace();
        }finally {
            if(pr != null){
                pr.close();
            }
        }
    }

    private synchronized void sendPrivate(String user, String msg) {
        try {
            PrintWriter pr = new PrintWriter(Server.hashMap.get(user).getOutputStream(), true);
            pr.println("Private chat with " + threadName + " : " + msg);
        } catch (IOException e) {
            e.getStackTrace();
        }
    }

    private synchronized void sendAll(String msg) {
        try {
            for (HashMap.Entry<String, Socket> entry : Server.hashMap.entrySet()) {
                PrintWriter pr = new PrintWriter(entry.getValue().getOutputStream(), true);
                pr.println(threadName + " says in Common chat: " + msg);
            }
        } catch (IOException e) {
            e.getStackTrace();
        }
    }
}