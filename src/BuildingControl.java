import java.util.ArrayList;

public class BuildingControl {

    Cabin[] cabins;
    DoorControl[][] doors;
    ArrayList<Integer> notifyList;


    public static void main(String[] args) {
        BuildingControl b = new BuildingControl();
	    b.run();
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




    private  void run(){
        initialize();
       cabins[0] = new Cabin(1);
       cabins[0].addStop(5);
       cabins[0].addStop(6);
       cabins[0].addStop(7);

       doors[2][0] = new DoorControl();

       while (true){
           if (stoppedCabin(0)){
               System.out.println("building Control detected stopped cabin");
               openDoor(cabins[0].getCurrentFloor()-1,0);
               cabins[0].setCabinState(States.CabinStates.LoadUnload);
               notifyList.add (0);
           }

           while (!notifyList.isEmpty()){
               for (Integer id : notifyList){
                   if (!doors[cabins[id].getCurrentFloor()-1][id].getOpenState()) {
                       cabins[id].resume();
                       notifyList.remove((Integer) id);
                   }

               }
           }
        }



    }




}
