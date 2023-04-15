import java.awt.*;


/**
 *                                        GameBoard Class
 * The GameBoard class represents the board which consist two players(each represents there tools and locations)
 * and the current player who has the turn
 *
 */
public class GameBoard {

    /** The two players of the game */
    private Player redPlayer, greenPlayer;
    /** The player who has the current turn */
    private Players currentPlayer;
    /** class with coordinates representing directions */
    private final Directions directions;


    /**
     *  GameBoard Constructor
     */
    public GameBoard() {
        setRedPlayer(new Player(Color.RED));
        setGreenPlayer(new Player(Color.GREEN));

        currentPlayer = Players.RED_PLAYER;
        directions = new Directions();

//        middle nodes

//        for(int x = -1*(radius-1); x <= radius-1; x++)
//            for(int y = -1*(radius-1); y <= radius-1; y++)
//                if(-1*(x+y) >= -1*(radius-1) && -1*(x+y) <= radius-1)
//                    nodes.put(new Coordinate(x, y, -1*(x+y)),new HexNode(players[0]));
//
//        for (int y = -4; y < 0; y++)
//            for (int z = (4-y); z > 4; z--)
//                nodes.put(new Coordinate((-y-z), y, z), new HexNode(players[0]));
//
//        for (int y = -4; y < 0; y++)
//            for (int x = (4-y); x > 4; x--)
//                nodes.put(new Coordinate(x, y, (-x-y)), new HexNode(players[0]));
//
//        for (int y = 4; y > 0; y--)
//            for (int x = (-4-y); x < -4; x++)
//                nodes.put(new Coordinate(x, y, (-x-y)), new HexNode(players[0]));
//
//        for (int y = 4; y > 0; y--)
//            for (int z = (-4-y); z < -4; z++)
//                nodes.put(new Coordinate(-y-z, y, z), new HexNode(players[0]));

    }
    /**
     *  switchPlayer function sets the currentPlayer to the opposite one
     */
    public void switchPlayer()
    {
        if(currentPlayer == Players.RED_PLAYER)
           currentPlayer = Players.GREEN_PLAYER;
        else
        {
            currentPlayer = Players.RED_PLAYER;
        }
    }
    /**
     *  isInBoardBounds function gets a coordinate and checks whether it's in the board bounds
     * @return boolean - whether it's in the board bounds
     */
    public boolean isInBoardBounds(Coordinate coordinate)
    {
        int coordinateX = coordinate.getX();
        int coordinateY = coordinate.getY();
        int coordinateZ = coordinate.getZ();

        if (coordinateZ <= -5 && (coordinateY > 4 || coordinateX > 4))
            return false;
        if (coordinateY <= -5 && (coordinateZ > 4 || coordinateX > 4))
            return false;
        if (coordinateX <= - 5 && (coordinateY > 4 || coordinateZ > 4))
            return false;
        if (coordinateY >= 5 && (coordinateZ < -4 || coordinateX < -4))
            return false;
        if (coordinateZ >= 5 && (coordinateX < -4 || coordinateY < -4))
            return false;
        if (coordinateX >= 5 && (coordinateY < -4 || coordinateZ < -4))
            return false;

        return true;
    }

    /**
     * Getters and Setters
     */
    public Player getRedPlayer() {
        return redPlayer;
    }

    public void setRedPlayer(Player red_player) {
        this.redPlayer = red_player;

    }


    public Player getGreenPlayer() {
        return greenPlayer;
    }

    public void setGreenPlayer(Player green_Player) {
        this.greenPlayer = green_Player;
    }

    public Players getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getCurrentPlayerPlayer()
    {
        return currentPlayer.ordinal() == 0 ? redPlayer : greenPlayer;
    }

    public void setCurrentPlayer(Players currentPlayer) {
        this.currentPlayer = currentPlayer;
    }


    public Directions getDirections()
    {
        return this.directions;
    }

}
