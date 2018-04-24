import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BuildingControl extends Thread
{

  private Cabin[] cabins;
  DoorControl[][] doors;
  ArrayList<Integer> notifyList;
  ArrayList<Integer> requestQueue;
  boolean[] key = new boolean[4];
  int a[] = new int[2];

  boolean emergency1;


  public static void main(String[] args) throws InterruptedException
  {
    BuildingControl b = new BuildingControl();
    b.start();

    GUI g = new GUI();
    g.instantiate(args);
  }


  public void initialize()
  {

      requestQueue = new ArrayList<>();
    cabins = new Cabin[4]; // run
    emergency1 = false;

    //Cabin ID has to be 0,1,2,3. GUI uses this to recognize the cabins.
    cabins[0] = new Cabin(0);
    cabins[1] = new Cabin(1);
    cabins[2] = new Cabin(2);
    cabins[3] = new Cabin(3);





    MapView.getInstance().setElevators(cabins);

    doors = new DoorControl[10][2];
    for (int i = 0; i < doors.length; i++)
    {
      for (int j = 0; j < doors[0].length; j++)
      {
        doors[i][j] = new DoorControl();
      }
    }
    notifyList = new ArrayList<Integer>();

  }

  boolean stoppedCabin(int id)
  {
    return cabins[id].getCabinState() == States.CabinStates.Stopped;
  }

  void openDoor(int floor, int id)
  {
    doors[floor][id].start();
  }

  int handdleRequest (int floorNo, int dir){

    int bestDistance =11;
    int id = -1;
    int temp;
    for (int i = 0; i < cabins.length ; i++){
      if (key[i]) continue;
      temp = validateCabin(floorNo,dir,i);
      System.out.println("Best Distance for cabin " + i + "  floor : " + floorNo + " dir : "+ dir + " is " + temp + " cabin is at "+ cabins[i].getCurrentFloor() + " cabin state : " + cabins[i].getCabinState());
      if (temp < bestDistance){
        bestDistance = temp;
        id = i;
      }
    }

    if (bestDistance > 3 && id!=-1 && (cabins[id].getCabinState()!= States.CabinStates.Ideal))  {
      for (int i = 0; i < cabins.length; i++){

        if (cabins[i].getCabinState() == States.CabinStates.Ideal) {
          id = i;
          break;
        }

      }
    }
    System.out.println(id);
    return id;
  }

  int validateCabin (int floorNo, int dir , int cabinId){
    States.CabinStates currentState = cabins[cabinId].getCabinState();
    int currentFloor = cabins[cabinId].getCurrentFloor();
    if (!(currentState == States.CabinStates.Ideal)) {
      if (dir > 0) {
        if (currentState == States.CabinStates.Down) return 100;
      }
      if (dir < 0) {
        if (currentState == States.CabinStates.UP) return 100;
      }
    }
    else{
      return Math.abs(floorNo - currentFloor) ;
    }
    if (((floorNo - currentFloor) * dir) <= 0) return 100;
    return Math.abs(floorNo - currentFloor) ;
  }

  void reInitializeCabins(){
    for (Cabin c: cabins){
      c.clearRequest();
      c.door.removeFromEmergency();
    }
  }


  @Override
  public void run()
  {
    //Cabin ID has to be 0,1,2,3. GUI uses this to recognize the cabins.
    initialize();
    cabins[0].start();
    cabins[1].start();
    cabins[2].start();
    cabins[3].start();




    System.out.println("added everything");

    //TODO : Optimize for the nearest elevator. Give weight i.e time value for each elevator to get to that point and take minimum of thoes times.


    class SayHello extends TimerTask
    {
      public void run() {
        boolean emergency = MapView.getInstance().getisEmergency();


        System.out.println(key[0]);
        if (emergency) {
          for (int i = 0; i < cabins.length; i++){
            cabins[i].goToEmergency();
            if (cabins[i].getCurrentFloor() == 1 && cabins[i].getCabinState()== States.CabinStates.Ideal) cabins[i].door.goToEmergency();
            emergency1 = true;
          }


        } else {

           a = MapView.getInstance().getFloorRequests();
          key = MapView.getInstance().getKeypressed();
          if (emergency1){
            System.out.println("Here -------------------------------------");
            try {
              Thread.sleep(5000);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            reInitializeCabins();
            a = null;
            emergency1 = false;
          }
          if (a != null) {
            System.out.println("floor  " + a[0]);
            int id = handdleRequest(a[0], a[1]);
            if (id >= 0) {
              cabins[id].addStop(a[0]);
            } else {
              requestQueue.add(a[0]);
              requestQueue.add(a[1]);
            }
          }

          for (int i = 0; i < requestQueue.size(); i++) {
            int id = handdleRequest(requestQueue.get(i), requestQueue.get(i + 1));
            if (id >= 0) {
              cabins[id].addStop(requestQueue.get(i));
              //System.out.println("Length of requestQueue is " + requestQueue.size() + "and value of i is " + i);
              requestQueue.remove(i);
              requestQueue.remove(i);
            }
            i++;
          }

          int[] b = MapView.getInstance().getCabinFloorRequest();
          if (b != null) {
            cabins[b[1]].addCabinStop(b[0]);
            System.out.println("request from elevator " + b[0] + "at floor " + b[1]);
          }

        }
      }
    }

// And From your main() method or any other method
    Timer timer = new Timer();
    timer.schedule(new SayHello(), 0, 71);


  }

}
