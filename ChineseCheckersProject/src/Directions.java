import java.util.ArrayList;
import java.util.List;


public class Directions {

    /** directionsValues is a list consisting coordinates that there values are the values that needs to be
     * summed up in order to get from one position to other , the index of each coordinate is appropriate to
     * the Direction enum indexes.
     */
    private List<Coordinate> directionsValues;

    /**
     * Directions class constructor
     */
    public Directions()
    {
        directionsValues = new ArrayList<Coordinate>();
        directionsValues.add(new Coordinate(-1,1,0));
        directionsValues.add(new Coordinate(-1,0,1));
        directionsValues.add(new Coordinate(0,-1,1));
        directionsValues.add(new Coordinate(1,-1,0));
        directionsValues.add(new Coordinate(1,0,-1));
        directionsValues.add(new Coordinate(0,1,-1));

    }

    /**
     * getDirection return the appropriate coordinate according to the given Direction
     */
    public Coordinate getDirection(Direction direction)
    {
        return directionsValues.get(direction.ordinal());
    }


}
