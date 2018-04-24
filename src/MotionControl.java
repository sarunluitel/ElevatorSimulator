import java.util.Timer;
import java.util.TimerTask;

public class MotionControl{
    Timer timer;
    int currentFloor;
    int upComingFloor;
    double currentHeight;
    public final double FLOOR_HEIGHT = 100.0000; // height of floor in meters


    public final double MAX_SPEED = 20.0000; // the maximum speed (in m/s) upto which cabin accelerates
    public final double TIME_FOR_MAXSPEED = 1.0000; // time taken by cabin to reach max speed from rest
    public double acceleration = MAX_SPEED/TIME_FOR_MAXSPEED;

    public double firstHalfDistance = (MAX_SPEED * MAX_SPEED) / (2 * acceleration);
    public double thirdHalfDistance = FLOOR_HEIGHT-firstHalfDistance;



    public MotionControl()
    {
       this.currentFloor = 1;
       this.upComingFloor = 1;
       this.currentHeight = 0.00;
    }


    public void stop()
    {
        System.out.println("STOP METHOD REACHED");
        this.currentFloor = this.upComingFloor;



    }

    public void moveUp ()
    {
        long timeDiff = 10;
        timer = new Timer();
        double timeDiffInS = (double) timeDiff/1000;



        TimerTask motionTimer = new TimerTask() {
            int tempFloor;
            double timeInS = 0.0000;
            double tempHeight = 0.0000;
            double diffHeight = 0.0000;
            double diff = MAX_SPEED * timeDiffInS;
            @Override
            public void run() {


                timeInS = timeInS + timeDiffInS;
                if (tempHeight < firstHalfDistance)
                {

                    tempHeight = 0.5 * acceleration * timeInS * timeInS;
                    diffHeight = tempHeight;
                    System.out.println("Continue Move at max speed  TIME:::   " + timeInS);
                    System.out.println("The height reached ==  " + tempHeight);
                    setCurrentHeight((currentFloor - 1) * FLOOR_HEIGHT + diffHeight);



                    System.out.println("------------------------------    " + currentHeight);
                }


                else if (tempHeight < thirdHalfDistance) {

                    tempHeight += diff;


                    setCurrentHeight(currentHeight + diff);
                    System.out.println("------------------------------    " + currentHeight);

                }


                else
                {

                    tempHeight = 0;
                    this.cancel();
                    timer.cancel();
                    timer.purge();
                }


            }
        };
        timer.schedule(motionTimer,0,timeDiff);
    }

    public void moveContinue ()
    {
        long timeDiff = 10;
        timer = new Timer();
        double timeDiffInS = (double) timeDiff/1000;



        TimerTask motionTimer = new TimerTask() {
            double timeInS = 0.0000;
            double tempHeight = 0.0000;
            double diffHeight = MAX_SPEED *timeDiffInS;
            @Override
            public void run() {
                timeInS = timeInS + timeDiffInS;
                setCurrentHeight(currentHeight+diffHeight);


                if (tempHeight < FLOOR_HEIGHT)
                {

                    //System.out.println("Travelling with max speed:: Time  " + timeInS);
                    tempHeight += diffHeight;
                    System.out.println("The height reached ==  " + currentHeight);

              }

                else
                {
                    tempHeight = 0;

                    this.cancel();
                    timer.cancel();
                    timer.purge();
                    //System.out.println("THe current height is ::: " + currentHeight);

                    System.out.println("The deciding point in reached:: Time  " + timeInS);
                    System.out.println("The height reached ==  " + currentHeight);

                }


            }
        };
        timer.schedule(motionTimer,0,timeDiff);

    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setUpComingFloor(int upComingFloor) {
        this.upComingFloor = upComingFloor;
    }

    public void setCurrentHeight(double currentHeight) {

        this.currentHeight = currentHeight;
        //System.out.println("THe current height is ::: " + this.currentHeight);
    }
}