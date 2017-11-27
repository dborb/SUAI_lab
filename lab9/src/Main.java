public class Main {
    public static void main(String []args){
        Deadlock deadlock1 = new Deadlock("Lou");
        Deadlock deadlock2 = new Deadlock("Reed");
        new Thread(new Runnable() {
            @Override
            public void run() {
                deadlock1.call(deadlock2);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                deadlock2.call(deadlock1);
            }
        }).start();
    }
}
