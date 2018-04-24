import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DoorControl extends Thread {
    private boolean openState;
    private volatile boolean isStopped;
    private boolean emergency;
    private int openPercentage = 0;

    public DoorControl (){
        emergency =false;
        isStopped = false;
        ArrayList request = new ArrayList<Integer>();
    }

    public synchronized void  changeStoppedState(){
        isStopped = !isStopped;
    }

    public synchronized boolean getOpenState() {return this.isStopped;}

    public  boolean getisDoorOpen() {return this.isStopped;}

    public  int getOpenPercentage() {  return this.openPercentage;}



    public synchronized void open() throws InterruptedException {
        Timer timer = new Timer();


        timer.schedule(new DoorMotor(States.DoorStates.Opening), 0, 35);
        Thread.sleep(2500);
        timer.cancel();
        System.out.println("Door Opened");

         if (!emergency) {
             timer = new Timer();
             timer.schedule(new DoorMotor(States.DoorStates.Closing), 0, 35);
             Thread.sleep(2500);
             timer.cancel();
             System.out.println("Door Closed");
         }
    }

    @Override
    public  void run() {
        while (true){
            if (isStopped) {
                try {
                    synchronized (this){
                        open();
                        if (!emergency) {
                            System.out.println("here-----------------------");
                            changeStoppedState();
                            notify();
                        }
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void goToEmergency(){
        System.out.println("Opening executed ----------------------------------------");
        this.isStopped = true;
        this.emergency = true;
    }

    void removeFromEmergency(){
        System.out.println("Opening deexecuted ----------------------------------------");
        this.emergency = false;
    }

    class DoorMotor extends TimerTask
    {
        private States.DoorStates motorState;

        DoorMotor(States.DoorStates s)
        {
            this.motorState = s;

        }

        public void run()
        {
            if (this.motorState == States.DoorStates.Opening)
            {

                openPercentage += 1;
            } else if (this.motorState == States.DoorStates.Closing)
            {

                openPercentage += -1;
            }


        }
    }


}
