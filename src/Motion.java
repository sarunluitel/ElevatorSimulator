import java.util.ArrayList;

public class Motion{

    /**
     * The cabin moves with initial acceleration until time t1
     * Then it moves with an constant speed after t1 upto time t2
     * then it deaccelerates with the same negative aceeleration as first part
     */

    public final double FLOOR_HEIGHT = 3.0; // height of floor in meters
    public final double MAX_SPEED = 0.05; // the maximum speed (in m/s) upto which cabin accelerates
    public final double TIME_FOR_MAXSPEED = 3; // time taken by cabin to reach max speed from rest


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

    public Motion ()
    {

    }

    public ArrayList<Double> motionAnalyzer (int floorX1 , int floorX2)
    {
        /**
         *
         *  returns an arraylist containg 3 different point
         *  form the current floor upto first point is the point upto which elevator accelerates
         *  1st point to 2rd point it moves with constant speed
         *  2nd point to third point it slows down to zero
         *
         */

        ArrayList <Double> motionPoints = new ArrayList<>();
        if (floorX1 < floorX2)
        {
            motionPoints.add(floorX1 * FLOOR_HEIGHT + firstHalfDistance);
            motionPoints.add (floorX1 * FLOOR_HEIGHT + (basicSecondHalfDistance * (floorX2-floorX1)));
            motionPoints.add((double) floorX2 * FLOOR_HEIGHT);
        }

        return motionPoints;

    }









}