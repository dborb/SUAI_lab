public class Deadlock {
    private String name;

    public Deadlock(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void call(Deadlock thread1){
        synchronized(this) {
            System.out.println(this.name + " first function");
            thread1.callBack(this);
        }
    }

    public void callBack(Deadlock thread1){
        //synchronized (this) {
            System.out.println(thread1.getName() + " second function");
        //}
    }
}
