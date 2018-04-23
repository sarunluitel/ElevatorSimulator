import java.util.ArrayList;

public class CabinControl extends Thread
{
    public static final int FLOOR_HEIGHT = 3;
    public States.CabinStates cabinState;
    public Motion motor;
    public int currentFloor;
    public int iD;
    public int heightFromButtom;


    public DoorControl doorControl;
    public ArrayList <Integer> requests;
    public CabinControl(int iD)
    {
      this.iD = iD;
      this.motor = new Motion();
      this.currentFloor = 1;
      this.cabinState = States.CabinStates.Ideal;
      this.heightFromButtom = 0;
      requests = new ArrayList<>();
    }

    @Override
    public void run () {

        System.out.println("Cabin is Idle at floor 0. it's started  now ");
        while (true)
        {
            // if request is not empty change the state selecting the best floor available:
            if (!requests.isEmpty()) completeRequests();


            break;



        }

    }

    public void completeRequests()
    {
        int reQuestNo = 0;
        System.out.println("Completing all the requests");
        while (!requests.isEmpty())
        {
            int targetFloor = requests.get(0);
            changeState(getNextCabinStates(targetFloor),targetFloor);
            requests.remove(0);

        }
    }

    public void changeState (States.CabinStates newState, int targetFloor)
    {

        cabinState = newState;
        motor.start(States.MotionStates.UP,targetFloor);

        // activate the motors to move up using all the physics.
    }


    public States.CabinStates getNextCabinStates (int targetFloor)
    {
        if ((targetFloor - currentFloor) > 0 ) return States.CabinStates.UP;
        else if ((targetFloor - currentFloor) == 0) return  States.CabinStates.Ideal;
        else return States.CabinStates.Down;
    }

    public void updateRequest (int floor)
    {
        if (motor.isSafeLanding(floor)) requests.add(floor);
        else System.out.println("floor  "  + floor + " is not added to this cabin because need more time for stoping");
    }

    public static void main (String[] args)
    {
       CabinControl cabin1 = new CabinControl(1);
       cabin1.requests.add(3);
       cabin1.start();


    }


}

