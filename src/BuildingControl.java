import java.util.ArrayList;

public class BuildingControl {

    Cabin[] cabins = new Cabin[2];


    public static void main(String[] args) {
        BuildingControl b = new BuildingControl();
	    b.run();
    }


    private  void run(){
       cabins[0] = new Cabin(1);
       cabins[0].addStop(5);
       cabins[0].addStop(6);
       cabins[0].addStop(3);
       cabins[0].addStop(7);
       
    }




}
