package database;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LocalDB {
    private ArrayList<UserInfo> dataBase;

    public LocalDB(){
        dataBase = new ArrayList<>();
        //empty constructor
    }

    public void loadDB(){
        dataBase.clear();
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/sergei/Desktop/java/SUAI_lab/l13/src/database/localdb"));
            String string;
            String name;
            UserInfo userInfo = null;
            int i = 0;
            while((string = bufferedReader.readLine()) != null){
                if(i % 2 == 0){
                    name = string;
                    userInfo = new UserInfo(name);
                    i++;
                }
                else if(i % 2 == 1){
                    try(Scanner scanner = new Scanner(string)){
                        while (scanner.hasNext()){
                            userInfo.addPhone( scanner.next());
                        }
                    }catch (NullPointerException e){
                        e.getStackTrace();
                    }
                    dataBase.add(userInfo);
                    i++;
                }
            }
        }catch (IOException e){
            e.getStackTrace();
        }
    }

    public void addName(String name){
        dataBase.add(new UserInfo(name));
    }

    public void addPhone(String name, String phone){
        for(UserInfo i : dataBase){
            if(name.equals(i.getName())){
                i.addPhone(phone);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        for(UserInfo i : dataBase){
            stringBuilder.append("<p>");
            stringBuilder.append(i.getName());
            stringBuilder.append("</p>\n");
            stringBuilder.append("<p>").append(i.getPhoneNumbers()).append("</p>\n\n\n");
        }
        return stringBuilder.toString();
    }

    public List<String> getName(){
        List<String> ret = new ArrayList<>();
        for(UserInfo i : dataBase){
            ret.add(i.getName());
        }
        return ret;
    }

    public String getPhones(String name){
        for(UserInfo i : dataBase){
            if(name.equals(i.getName())){
                return i.getPhoneNumbers();
            }
        }
        return null;
    }

    public void saveDB(){
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/home/sergei/Desktop/java/SUAI_lab/l13/src/database/localdb", false))){
            for(UserInfo ui : dataBase){
                bufferedWriter.write(ui.getName());
                bufferedWriter.write("\n");
                bufferedWriter.write(ui.getPhoneNumbers());
                bufferedWriter.write("\n");
            }
        }catch (IOException e){
            e.getStackTrace();
        }
    }

}
