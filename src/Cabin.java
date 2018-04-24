import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Cabin extends Thread {

    private int id ;
    private MotionControl motion;
    private States.CabinStates cabinState;
    private ArrayList request = new ArrayList<Integer>();
    private int currentFloor=0;
    boolean busy = false;
    DoorControl door;

    public int getcabinId() {
        return id;
    }

    @Override
    public  void run() {


        class SayHello extends TimerTask
        {
            public void run()
            {
                if (!request.isEmpty()) changeState();
                while (!request.isEmpty()) {
                    try {
                        move();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (request.contains(motion.upComingFloor)) {
                        try {
                            executeStopped();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    else
                    {
                        try {
                            continueMoving();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                    if (cabinState != States.CabinStates.Ideal){
                        cabinState = States.CabinStates.Ideal;
                        System.out.println("Cabin " + id + " is ideal now");
                    }

                }

            }


// And From your main() method or any other method
        Timer timer = new Timer();
        timer.schedule(new SayHello(), 0, 500);



    }

    private void changeState(){
        if ((int)request.get(0) > currentFloor) this.cabinState = States.CabinStates.UP;
        else this.cabinState = States.CabinStates.Down;
    }


    public States.CabinStates getCabinState() {return this.cabinState;}
    public  void setCabinState(States.CabinStates newState) { this.cabinState = newState;}

    public  int getCurrentFloor() {return this.currentFloor;}

    public  void move() throws InterruptedException {

        if (cabinState == States.CabinStates.UP) {
            //this.sleep(500);
            System.out.println("changing current floor");
            motion.moveUp();
            this.sleep(4500);

            System.out.println("THe current height is ::: " + motion.currentHeight);
            motion.setUpComingFloor(this.currentFloor + 1);

            //this.currentFloor ++;
        }

        else {
           // this.sleep(500);
            this.currentFloor --;
        }



    }

    public  void executeStopped() throws InterruptedException {
        motion.stop();
        this.currentFloor = motion.upComingFloor;
        System.out.println("Stopped Cabin " + id + "at floor " + getCurrentFloor());
        synchronized (door){
            door.changeStoppedState();
            door.wait();
        }
        request.remove((Integer) currentFloor);

    }

    public void continueMoving () throws InterruptedException{
        System.out.println("The descision is not to stop. Hence, moving continues ::");
        if (cabinState == States.CabinStates.UP) {
            System.out.println("changing current floor");

            motion.moveContinue();
            this.sleep(4500);
            this.currentFloor ++;

            System.out.println("THe current height is ::: " + motion.currentHeight);
            motion.setUpComingFloor(this.currentFloor + 1);

            //this.currentFloor ++;
        }

        else {
            // this.sleep(500);
            this.currentFloor --;
        }

    }

    public  Cabin (int id){
        this.id = id;
        this.cabinState = States.CabinStates.Ideal;
        this.currentFloor = 1;
        motion = new MotionControl();
        this.door = new DoorControl();
        door.start();
    }

   void addStop (int floorNo){
        if (request.contains(floorNo)) return;
        request.add(floorNo);


    }
}
