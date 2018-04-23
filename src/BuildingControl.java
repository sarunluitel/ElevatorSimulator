import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BuildingControl extends Thread
{

  private Cabin[] cabins;
  DoorControl[][] doors;
  ArrayList<Integer> notifyList;
  ArrayList<Integer> requestQueue;


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
      temp = validateCabin(floorNo,dir,i);
      System.out.println("Best Distance for cabin " + i + "  floor : " + floorNo + " is " + temp + " cabin is at "+ cabins[i].getCurrentFloor());
      if (temp < bestDistance){
        bestDistance = temp;
        id = i;
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
      return (floorNo - currentFloor) * dir;
    }
    if (((floorNo - currentFloor) * dir) < 0) return 100;
    return (floorNo - currentFloor) * dir;
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
      public void run()
      {
        int[] a = MapView.getInstance().getCabinFloorRequest();
        if (a!=null && a[0] != -10 ){
          System.out.println("floor  "+a[0]);
          int id = handdleRequest(a[0],a[1]);
          if (id>=0){
            cabins[id].addStop(a[0]);
          }
          else{
            System.out.println("--------------------------------------------------------------------------------------");
              requestQueue.add(a[0]);
              requestQueue.add(a[1]);
          }
        }

        for (int i = 0; i < requestQueue.size();i++){
            int id = handdleRequest(requestQueue.get(i),requestQueue.get(i+1));
            if (id>=0){
                cabins[id].addStop(requestQueue.get(i));
              System.out.println("Length of requestQueue is " + requestQueue.size() + "and value of i is " + i);
                requestQueue.remove(i);
                requestQueue.remove(i);
            }
            i++;
        }

      }
    }

// And From your main() method or any other method
    Timer timer = new Timer();
    timer.schedule(new SayHello(), 0, 500);


  }

}
