import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Cabin extends Thread {

    private int id ;
    private States.CabinStates cabinState;
    private ArrayList request = new ArrayList<Integer>();
    private ArrayList requestQueue = new ArrayList<Integer>();
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
                    if (cabinState == States.CabinStates.Ideal) changeState();
                    for (int i = 0; i < request.size(); i++){
                        int a = (int) request.get(i);
                        System.out.print(" " + a + " ");
                    }
                    System.out.println();
                    try {
                        System.out.println("moving");
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
                if (!requestQueue.isEmpty()){
                    System.out.printf("adding queue");
                    request.addAll(requestQueue);
                    requestQueue.clear();
                }


                }

            }


// And From your main() method or any other method
        Timer timer = new Timer();
        timer.schedule(new SayHello(), 0, 500);



    }

    private synchronized void changeState(){
        if ((int)request.get(0) > currentFloor) this.cabinState = States.CabinStates.UP;
        else if ((int)request.get(0) < currentFloor) this.cabinState = States.CabinStates.Down;
        else this.cabinState = States.CabinStates.Ideal;
    }


    public synchronized States.CabinStates getCabinState() {return this.cabinState;}
    public  void setCabinState(States.CabinStates newState) { this.cabinState = newState;}

    public  int getCurrentFloor() {return this.currentFloor;}

    public  void move() throws InterruptedException {
        System.out.println("inside move method");

        if (cabinState == States.CabinStates.UP) {
            this.sleep(500);

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
            }
            System.out.println("CabinState is " + cabinState);
            System.out.println("Floor is " + currentFloor);
        }



    }

    public  void executeStopped() throws InterruptedException {

       // System.out.println("Stopped Cabin " + id + "at floor " + getCurrentFloor());
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
        boolean empty = request.isEmpty();
        if (request.contains(floorNo)) return;
        if (floorNo == currentFloor) return;;
       System.out.println("request " + floorNo + " is added when currentfloor " + currentFloor);
       System.out.print("request is ");
       System.out.println();
        request.add(floorNo);
        if (empty) changeState();


    }


    void addCabinStop (int floorNo){

        int difference = floorNo - currentFloor;
        System.out.println("adding request " + floorNo + " difference is " + difference );
        if (difference < 0 && (cabinState == States.CabinStates.Down || cabinState == States.CabinStates.Ideal)){
            System.out.println("added when cabinState DOWN");
            request.add(floorNo);
            return;
        }

        if (difference > 0 && (cabinState == States.CabinStates.UP || cabinState == States.CabinStates.Ideal)){
            System.out.println("added when cabinState UP ");
            request.add(floorNo);
            return;
        }
        System.out.println("adding at queue " + floorNo);
        requestQueue.add(floorNo)  ;

    }

    void goToEmergency(){
      this.request.clear();
      this.requestQueue.clear();
      this.addStop(1);
  }
}
