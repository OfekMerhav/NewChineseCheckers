/**
 *                   CoordinateMove
 * The CoordinateMove class represents a move in the game- what tool to move and where to
 *
 */
public class CoordinateMove {

    /** the tool to move */
    Coordinate startMoveCoordinate;
    /** the coordinate to move to */
    Coordinate toMoveCoordinate;
    /** the score of the move - used in the Ai algorithm */
    double moveScore;

    /**
     *  CoordinateMove Constructor
     */
    public CoordinateMove(double Score) {
        startMoveCoordinate = null;
        toMoveCoordinate = null;
        moveScore = Score;
    }
    /**
     *  CoordinateMove Constructor
     */
    public CoordinateMove(Coordinate startMoveCoordinate, Coordinate To_move_Coordinate, double Score) {
        this.startMoveCoordinate = startMoveCoordinate;
        this.toMoveCoordinate = To_move_Coordinate;
        moveScore = Score;
    }
}
