package database;

import java.util.ArrayList;

public class UserInfo {
    private String name;
    private ArrayList<String> phoneNumbers;


    public UserInfo(String name){
        phoneNumbers = new ArrayList<>();
        this.name = name;
        phoneNumbers.add("empty");
    }

    public synchronized void addPhone(String phone){
        if(phoneNumbers.get(0).compareTo("empty") == 0){
            phoneNumbers.remove(0);
        }
        phoneNumbers.add(phone);
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumbers() {
        StringBuilder stringBuilder = new StringBuilder("");
        for(String i : phoneNumbers){
            stringBuilder.append(i).append(" ");
        }
        return stringBuilder.toString();
    }
}
