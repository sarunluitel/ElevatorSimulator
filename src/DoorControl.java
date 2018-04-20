import java.util.ArrayList;

public class DoorControl extends Thread {
    private boolean openState;
    private volatile boolean isStopped;
    private boolean isDoorOpen;

    public DoorControl (){
        isDoorOpen =false;
        isStopped = false;
        ArrayList request = new ArrayList<Integer>();
    }

    public synchronized void  changeStoppedState(){
        isStopped = !isStopped;
    }

    public synchronized boolean getOpenState() {return this.isStopped;}

    public  boolean getisDoorOpen() {return this.isStopped;}

    public synchronized void open() throws InterruptedException {

        System.out.println("Door Opened");
        Thread.sleep(4000);

        System.out.println("Door Closed");
    }

    @Override
    public  void run() {
        while (true){
            if (isStopped) {
                isDoorOpen=true;
                try {
                    synchronized (this){
                        open();
                        changeStoppedState();
                        notify();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
