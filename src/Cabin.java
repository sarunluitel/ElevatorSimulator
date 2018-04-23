import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Cabin extends Thread {

    private int id ;
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
                    if (request.contains(currentFloor)) {
                        try {
                            executeStopped();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (request.isEmpty()){
                        cabinState = States.CabinStates.Ideal;
                        System.out.println("Cabin " + id + " is ideal now");
                    }
                }


                }

            }


// And From your main() method or any other method
        Timer timer = new Timer();
        timer.schedule(new SayHello(), 0, 500);



    }

    private void changeState(){
        if ((int)request.get(0) > currentFloor) this.cabinState = States.CabinStates.UP;
        else if ((int)request.get(0) < currentFloor) this.cabinState = States.CabinStates.Down;
        else this.cabinState = States.CabinStates.Ideal;
    }


    public States.CabinStates getCabinState() {return this.cabinState;}
    public  void setCabinState(States.CabinStates newState) { this.cabinState = newState;}

    public  int getCurrentFloor() {return this.currentFloor;}

    public  void move() throws InterruptedException {

        if (cabinState == States.CabinStates.UP) {
            this.sleep(500);
            System.out.println("changing current floor");
            this.currentFloor ++;
        }

        else if (cabinState == States.CabinStates.Down) {
            this.sleep(500);
            this.currentFloor --;
        }

        if (currentFloor > 10 || currentFloor < 0){
            System.out.println("--------------------------------------------");
            for (int i = 0; i < request.size(); i++){
               int a = (int) request.get(i);
                System.out.println("request is "+ a);
                System.out.println("CabinState is " + cabinState);
            }
        }



    }

    public  void executeStopped() throws InterruptedException {

        System.out.println("Stopped Cabin " + id + "at floor " + getCurrentFloor());
        synchronized (door){
            door.changeStoppedState();
            door.wait();
        }
        request.remove((Integer) currentFloor);


    }

    public  Cabin (int id){
        this.id = id;
        this.cabinState = States.CabinStates.Ideal;
        this.currentFloor = 1;
        this.door = new DoorControl();
        door.start();
    }

   void addStop (int floorNo){
        if (request.contains(floorNo)) return;
        request.add(floorNo);


    }
}
