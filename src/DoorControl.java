import java.util.ArrayList;

public class DoorControl extends Thread {
    private boolean openState;
    private volatile boolean isStopped;

    public DoorControl (){
        openState = false;
        isStopped = false;
        ArrayList request = new ArrayList<Integer>();
    }

    public synchronized void  changeStoppedState(){
        isStopped = !isStopped;
    }

    public synchronized boolean getOpenState() {return this.openState;}

    public synchronized void open() throws InterruptedException {
        openState = true;
        System.out.println("Door Opened");
        Thread.sleep(4000);
        System.out.println("Door Closed");
        openState = false;
    }

    @Override
    public  void run() {
        while (true){
            if (isStopped) {
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
