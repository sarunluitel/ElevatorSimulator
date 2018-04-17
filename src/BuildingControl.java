import java.util.ArrayList;

public class BuildingControl extends Thread {

    Cabin[] cabins;
    DoorControl[][] doors;
    ArrayList<Integer> notifyList;


    public static void main(String[] args) throws InterruptedException {
        GUI g = new GUI();
        g.instantiate(args);
        BuildingControl b = new BuildingControl();
	   // b.start();
    }



    public void initialize(){
        cabins = new Cabin[2];
        doors = new DoorControl[10][2];
        for (int i=0; i < doors.length; i++) {
            for (int j=0; j < doors[0].length; j++){
                doors[i][j] = new DoorControl();
            }
        }
        notifyList = new ArrayList<Integer>();

    }

    boolean stoppedCabin(int id){
        if (cabins[id].getCabinState()== States.CabinStates.Stopped) return true;
        return false;
    }

    void openDoor(int floor, int id){
        doors[floor][id].start();
    }



    @Override
    public void run()  {
        initialize();
        cabins[0] = new Cabin(1);
        cabins[0].start();

        cabins[0].addStop(5);
        cabins[0].addStop(6);
        cabins[0].addStop(7);

        cabins[1] = new Cabin(25);
        cabins[1].start();

        cabins[1].addStop(5);
        cabins[1].addStop(6);
        cabins[1].addStop(7);

        System.out.println("added everything");





    }




}
