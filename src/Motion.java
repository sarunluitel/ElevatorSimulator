import java.util.ArrayList;

public class Motion{

    /**
     * The cabin moves with initial acceleration until time t1
     * Then it moves with an constant speed after t1 upto time t2
     * then it deaccelerates with the same negative aceeleration as first part
     */


    public final double FLOOR_HEIGHT = 250; // height of floor in meters
    public final double MAX_SPEED = 100; // the maximum speed (in m/s) upto which cabin accelerates
    public final double TIME_FOR_MAXSPEED = 0.5; // time taken by cabin to reach max speed from rest
    public long startTime;
    public ArrayList <Double> motionList;
    int targetFloor;
    long currentHeight;
    public long elapsedTime;
    public long position;



    public States.MotionStates motorState;
    public double speedUpPoint;
    public double slowDownPoint;
    public double targerHeight;
    public double startHeight;
    public double timeForSlowingPoint;



    /**
     * v = u + a t
     * v = at , since initial speed is 0
     * a = v /t
     */
    public double acceleration = MAX_SPEED/TIME_FOR_MAXSPEED;

    /**
     * Dividing the whole motion into three half:
     * 1st half = going from 0m/s to max speed
     * 2nd half = travelling at max speed
     * 3rd half = going from max to stop
     *
     * the distance form 1st have and 3rd half is same
     * whereas the distance for 2 nd half depends on how many floor you are going
     */

    /**
     * v^2 = u ^2 + 2 * a * s
     * v ^2 = = 2 * a * s
     * s = v ^2 / 2a
     */

    public double firstHalfDistance = (MAX_SPEED * MAX_SPEED) / (2 * acceleration);
    public double thirdHalfDistance = firstHalfDistance;

    // This is for travelling 1 floor. for travelling n floor multiply this by n
    public double basicSecondHalfDistance = FLOOR_HEIGHT - firstHalfDistance - thirdHalfDistance;

    public int currentFloor;

    public Motion ()
    {
      this.currentHeight = 0;
      this.targetFloor = 0;
      this.position = 0;
      this.elapsedTime = 0;
      this.startTime = 0;
    }

    /**
     * Only start motor at the point when elevator is moving.
     *
     */
    public void start (States.MotionStates motionState, int tarFloor)
    {


        targetFloor = tarFloor;

        if (motionState == States.MotionStates.UP) moveUp();
        if (motionState == States.MotionStates.DOWN) moveDown();
        if (motionState == States.MotionStates.STOP) stop();

    }


    private void moveUp ()
    {
        startHeight = currentHeight;
        targerHeight = (targetFloor -1) * FLOOR_HEIGHT ;
        speedUpPoint = currentHeight + firstHalfDistance;
        slowDownPoint = targerHeight - firstHalfDistance;
        timeForSlowingPoint =  TIME_FOR_MAXSPEED + (slowDownPoint/MAX_SPEED);
        startTime = System.currentTimeMillis();
        motorState = States.MotionStates.UP;
        System.out.println("The motor has started moving up");
        System.out.println("The slowDown point is " + slowDownPoint);


        while (true)
        {
           System.out.println("The cabin position is ==   " + currentHeight);
           double position = getPosition();
           if (Math.abs(position - targerHeight) < 10)
           {
               System.out.println("The target floor is reached");
               System.out.println("The target position is ==  " + targerHeight);
               System.out.println ("The current position is  ==  " + currentHeight);
               break;
           }
        }


        stop();
    }

    private void moveDown()
    {

        targerHeight = (targetFloor -1) * FLOOR_HEIGHT ;
        speedUpPoint = currentHeight - firstHalfDistance;
        slowDownPoint = targerHeight + firstHalfDistance;
        startTime = System.currentTimeMillis();
        motorState = States.MotionStates.DOWN;

        while (true)
        {
            if (getPosition() - targerHeight < 0.00001) break;
        }
        stop();
    }

    private void stop()
    {
        System.out.println("The motor is stopping now");
        motorState = States.MotionStates.STOP;


    }

    private double getPosition ()
    {


        long timeElapsed = (System.currentTimeMillis() - startTime)/ 1000;

        System.out.println("Time elapsed since start  " + timeElapsed);


        if (currentHeight > slowDownPoint)
        {

            double diffTime = timeElapsed - timeForSlowingPoint;
            double diffPos = MAX_SPEED * diffTime - 0.5 * acceleration * diffTime * diffTime;
            currentHeight = (long) (slowDownPoint + diffPos);

        }

        else {
            if (timeElapsed < TIME_FOR_MAXSPEED) {


                double diffPos = (0.5 * acceleration * timeElapsed * timeElapsed);
                currentHeight = (long) (startHeight + diffPos);
            }

            else
            {
                double diffTime = timeElapsed - TIME_FOR_MAXSPEED;
                double diffPos = MAX_SPEED * diffTime;
                currentHeight = (long) (startHeight + diffPos);
            }
        }

        return currentHeight;


    }



    public boolean isSafeLanding (int floor)
    {


        double diff = Math.abs(getPosition() - (floor-1)*FLOOR_HEIGHT);
        if (diff > getSafeLandingDistance())
        {
            return true;
        }
        return false;
    }

    public double getSafeLandingDistance()
    {
        // v= 0;
        // a = - acceleration
        // u = max speed
        // v^2 = u^2 + 2 a s
        // s = v^2/(2a)

        double safeDistance = (MAX_SPEED*MAX_SPEED)/(2 * acceleration);
        return  safeDistance;
    }



    public double getCurrentHeight ()
    {
        return currentHeight;
    }



   public void setTarGetFloor(int newFloor)
   {
       targetFloor = newFloor;

   }





}