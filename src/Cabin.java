import java.util.ArrayList;

public class Cabin extends Thread {

    private int id ;
    private States.CabinStates cabinState;
    private ArrayList request = new ArrayList<Integer>();
    private int currentFloor;
    @Override
    public synchronized void run() {

            while (!request.isEmpty()){
                System.out.println("Cabin " + id + " is at Floor no " + currentFloor);
                try {
                    move();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (request.contains(currentFloor)){
                    System.out.println("going to stop now");
                    try {
                        executeStopped();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    request.remove((Integer) currentFloor);
                }
            }

            cabinState = States.CabinStates.Ideal;

    }

    public States.CabinStates getCabinState() {return this.cabinState;}
    public synchronized void setCabinState(States.CabinStates newState) { this.cabinState = newState;}

    public synchronized int getCurrentFloor() {return this.currentFloor;}

    public synchronized void move() throws InterruptedException {
        System.out.println("moving");
        if (cabinState == States.CabinStates.UP) {
            this.sleep(500);
            this.currentFloor++;
        }

        else {
            Thread.sleep(500);
            this.currentFloor--;
        }

        System.out.println("moved");
    }

    public synchronized void executeStopped() throws InterruptedException {
        System.out.println("Stopped");
        cabinState = States.CabinStates.Stopped;
        this.suspend();

    }

    public  Cabin (int id){
        this.id = id;
        this.cabinState = States.CabinStates.Ideal;
        this.currentFloor = 1;
    }

  synchronized void addStop (int floorNo){
        request.add(floorNo);
        if (cabinState == States.CabinStates.Ideal) {
            if (currentFloor < floorNo) cabinState = States.CabinStates.UP;
            else cabinState = States.CabinStates.Down;
            this.start();
        }

    }
}
