import java.awt.geom.Ellipse2D;

/**
 *                            Coordinate Class
 * The Coordinate class represents the location/Coordinate of the hex in the hex board
 * the location is represented by x,y,z by the cube-coordinated board method
 */
public class Coordinate {

    private int x, y, z;

    private boolean isVisited;

    /**
     * Empty Coordinate Constructor
     */
    public Coordinate() {
        isVisited = false;
    }

    /**
     * Coordinate Constructor
     *
     * @param x represents x coordinate
     * @param y represents y coordinate
     * @param z represents z coordinate
     */
    public Coordinate(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
        isVisited = false;


    }
    /**
     * Coordinate Copy Constructor
     * @param coordinate - coordinate to be copied
     */
    public Coordinate(Coordinate coordinate) {
        this.x = coordinate.x;
        this.y = coordinate.y;
        this.z = coordinate.z;
        isVisited = false;
    }

    /**
     * getEllipse function builds an Ellipse2D object according to the
     * coordinate's values
     * @return Ellipse2D - describes an ellipse that can be shown on the screen
     */
    public Ellipse2D getEllipse() {

        double dx = this.x;
        double dy = this.y;
        double dz = this.z;
        double px = -1 * (dz - dx) / 2;
        double py = dy;

        return new Ellipse2D.Double(px * App.HEX_DIAMETER + App.SCREEN_SIZE / 2,
                py * (App.HEX_DIAMETER) + App.SCREEN_SIZE / 2,
                App.PIECE_DIAMETER, App.PIECE_DIAMETER);
    }

    /**
     * equals is an overriding function for checking equality between coordinates
     * @param obj
     * @return boolean - if the coordinates are equals or not
     */
    @Override
    public boolean equals(Object obj) {

        Coordinate coordinate = (Coordinate) obj;

        return x == coordinate.getX() && y == coordinate.getY() && z == coordinate.getZ();

    }
    /**
     * hashCode is an overriding function for calculating hash code to the coordinate object
     * @return int - calculated hash code
     */
    @Override
    public int hashCode() {

        int result;
        String s = "";
        s += String.valueOf(x);
        s += String.valueOf(y);
        s += String.valueOf(z);
        result = s.hashCode();
        return result;

    }

    /**
     * addCoordinate function gets a coordinates and sums the coordinates up
     * in given number of times
     * @param coordinate - coordinate to sum with
     * @return void
     */
    public void addCoordinate(Coordinate coordinate, int number_of_times) {
        this.x += coordinate.x*number_of_times;
        this.y += coordinate.y*number_of_times;
        this.z += coordinate.z*number_of_times;
    }
    /**
     * getRelativeDirectionOfCor function gets a coordinates and return it's relative Direction to the
     * current coordinate.
     * @param coordinate - coordinate check with
     * @return Direction - the relative Direction
     */
    public Direction getRelativeDirectionOfCor(Coordinate coordinate) {
        if (this.x == coordinate.x) {

            if (this.y > coordinate.y)
                return Direction.UPPER_LEFT;
            return Direction.DOWN_RIGHT;
        }
        if (this.y == coordinate.y) {

            if (this.x > coordinate.x)
                return Direction.LEFT;
            return Direction.RIGHT;
        }
        if (this.z == coordinate.z) {

            if (this.x > coordinate.x)
                return Direction.DOWN_LEFT;
            return Direction.UPPER_RIGHT;
        }
        return Direction.NONE;

    }

    /**
     * distance function calculates the distance of two coordinates
     * @param coordinate - coordinate to measure with
     * @return double - calculated distance
     */
    public double distance(Coordinate coordinate) {
        return Math.sqrt(Math.pow(x - coordinate.x, 2) + Math.pow(y - coordinate.y, 2) + Math.pow(z - coordinate.z, 2));
    }
    public double distance_expon(Coordinate coordinate) {
        return Math.exp(Math.sqrt(Math.pow(x - coordinate.x, 2) + Math.pow(y - coordinate.y, 2) + Math.pow(z - coordinate.z, 2)));
    }

    /**
     * Getters and Setters
     */
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setAsVisited() {
        this.isVisited = true;
    }

    public void clearIsVisited() {
        this.isVisited = false;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", is_visited=" + isVisited +
                '}';
    }
}
