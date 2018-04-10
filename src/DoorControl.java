import java.util.ArrayList;

public class DoorControl extends Thread {
    private boolean openState;

    public DoorControl (){
        openState = false;
        ArrayList request = new ArrayList<Integer>();
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
    public synchronized void run() {
        try {
            open();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
