import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class BuildingControl extends Thread
{

  private Cabin[] cabins;
  DoorControl[][] doors;
  ArrayList<Integer> notifyList;


  public static void main(String[] args) throws InterruptedException
  {
    BuildingControl b = new BuildingControl();
    b.start();

    GUI g = new GUI();
    g.instantiate(args);
  }


  public void initialize()
  {
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


  @Override
  public void run()
  {
    //Cabin ID has to be 0,1,2,3. GUI uses this to recognize the cabins.
    initialize();

    cabins[1].start();
    cabins[2].start();
    cabins[3].start();
    System.out.println("added everything");

    //TODO : Optimize for the nearest elevator. Give weight i.e time value for each elevator to get to that point and take minimum of thoes times.


    class SayHello extends TimerTask
    {
      public void run()
      {
        int[] a = MapView.getInstance().getFloorRequests();
        if (a!=null )System.out.println("flooe  "+a[0]);
      }
    }

// And From your main() method or any other method
    Timer timer = new Timer();
    timer.schedule(new SayHello(), 0, 500);


  }

}
