import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;

public class Model {

    GameBoard board;
    AiBot aiBot;

    /**
     * List of the current possible moves
     */
    LinkedList<Coordinate> Possible_Moves;

    /**
     * Constructor For Model class
     */
    public Model() {
        initBoard();
        Possible_Moves = new LinkedList<Coordinate>();
        aiBot = new AiBot(this);
    }

    /**
     * selectTool function gets a coordinates which represents a tool in the game and
     * update the current player selected tool to be it.
     *
     * @param Selected_Coordinate - coordinates which represents a tool in the game
     */
    public void selectTool(Coordinate Selected_Coordinate) {

        Player current_player = board.getCurrentPlayerPlayer();

        /* Get the selected tool coordinate, if no tool was selected it will be null */
        if (Selected_Coordinate != null) {
            current_player.setSelectedTool(Selected_Coordinate);
        } else {
            current_player.setSelectedTool(null);
            System.out.format("None selected\n");
        }
        updatePossibleMoves();
    }

    /**
     * makeTurn function gets a coordinates which represents a tool in the game and
     * replaces the selected tool of the current player to the given possible move tool
     *
     * @param Selected_Coordinate - coordinates which represents a possible move in the game
     */
    public void makeTurn(Coordinate Selected_Coordinate) {

        if (Selected_Coordinate != null) {
            Player current_player = board.getCurrentPlayerPlayer();
            Coordinate Selected_tool = current_player.getSelectedTool();

            current_player.playerTools.remove(Selected_tool);
            current_player.playerTools.put(Selected_Coordinate, new HexNode(current_player.getPlayerColor()));
            current_player.setSelectedTool(null);

            updatePossibleMoves();
        }
    }


    /**
     * GetSelectedCor function gets coordinates of the screen clicked position and returns a Coordinate of the
     * clicked tool , if None of the players tools was selected it returns null
     *
     * @return Coordinate - the tools that was chosen , if none was chosen null will be returned
     */
    public Coordinate getSelectedCor(double clicked_x, double clicked_y) {

        class distance {
            /* calculates the distance of the given points coordinates */
            public double calculate_distance(double point_x, double point_y, double clicked_x, double clicked_y) {
                return Math.sqrt(Math.pow(clicked_y - point_y, 2) + Math.pow(clicked_x - point_x, 2));
            }
        }

        HashMap<Coordinate, HexNode> nodes;
        Iterator it;
        distance distance = new distance();

        if (this.board.getCurrentPlayer() == Players.RED_PLAYER) {
            nodes = this.board.getRedPlayer().getPlayerTools();
        } else {
            nodes = this.board.getGreenPlayer().getPlayerTools();
        }

        it = nodes.entrySet().iterator();
        /* Iterate over the current player's tools and search if any was selected */
        while (it.hasNext()) {
            /* Current tool  */
            Map.Entry mapElement = (Map.Entry) it.next();
            Ellipse2D e = ((Coordinate) mapElement.getKey()).getEllipse();
            if (distance.calculate_distance(e.getX() + App.PIECE_RADIUS, e.getY() + App.PIECE_RADIUS, clicked_x, clicked_y) <= App.PIECE_RADIUS) {
                return (Coordinate) mapElement.getKey();
            }
        }
        /* In case no player tool was selected  */
        return null;
    }

    /**
     * getSelectedPossibleCor function gets coordinates of the screen clicked position and returns a Coordinate of the
     * clicked possible move , if None of the possible moves was selected it returns null
     *
     * @return Coordinate - the possible move that was chosen , if none was chosen null will be returned
     */
    public Coordinate getSelectedPossibleCor(double Mouse_X, double Mouse_Y) {

        class distance {
            /* calculates the distance of the given points coordinates */
            public double calculate_distance(double point_x, double point_y, double clicked_x, double clicked_y) {
                return Math.sqrt(Math.pow(clicked_y - point_y, 2) + Math.pow(clicked_x - point_x, 2));
            }
        }

        distance distance = new distance();
        List<Coordinate> nodes = getPossibleMoves();

        Iterator it = nodes.iterator();
        /* Iterate over the current player's tools and search if any was selected */
        while (it.hasNext()) {
            /* Current tool  */
            Coordinate coordinate = (Coordinate) it.next();
            Ellipse2D e = coordinate.getEllipse();
            if (distance.calculate_distance(e.getX() + App.PIECE_RADIUS, e.getY() + App.PIECE_RADIUS, Mouse_X, Mouse_Y) <= App.PIECE_RADIUS) {
                return coordinate;
            }
        }
        /* In case no player tool was selected  */
        return null;
    }

    /**
     * updatePossibleMoves function checks whether there is a selected tool in the current player,
     * if there is - update the list of the possible moves to the possible move of the tool
     * else - clear the list of the possible moves
     */
    public void updatePossibleMoves() {

        Player Current_player = board.getCurrentPlayerPlayer();
        Current_player.clearVisitedTools();
        clearPossibleMoves();
        Coordinate Selected_tool = Current_player.getSelectedTool();

        /* if none of the tools are selected, clear the possible moves  */
        if (Selected_tool == null) {
            Possible_Moves.clear();
        } else {
            Selected_tool.setAsVisited();

            int coordinateX = Selected_tool.getX();
            int coordinateY = Selected_tool.getY();
            int coordinateZ = Selected_tool.getZ();
            int max_number_of_neighbors = 6;

            /* build 6 coordinates that representing the tool's near by neighbors */
            List<Coordinate> neighbors = new ArrayList<Coordinate>();
            Coordinate down_left = new Coordinate(coordinateX - 1, coordinateY + 1, coordinateZ);
            Coordinate left = new Coordinate(coordinateX - 1, coordinateY, coordinateZ + 1);
            Coordinate upper_left = new Coordinate(coordinateX, coordinateY - 1, coordinateZ + 1);
            Coordinate upper_right = new Coordinate(coordinateX + 1, coordinateY - 1, coordinateZ);
            Coordinate right = new Coordinate(coordinateX + 1, coordinateY, coordinateZ - 1);
            Coordinate down_right = new Coordinate(coordinateX, coordinateY + 1, coordinateZ - 1);

            neighbors.add(down_left);
            neighbors.add(left);
            neighbors.add(upper_left);
            neighbors.add(upper_right);
            neighbors.add(right);
            neighbors.add(down_right);

            /* insert to the Possible_Moves list the legal neighbors coordinates */
            for (int neighbor_index = 0; neighbor_index < max_number_of_neighbors; neighbor_index++) {
                if (isLegalMove(neighbors.get(neighbor_index))) {
                    Possible_Moves.add(neighbors.get(neighbor_index));
                }
            }
            /* activate the functions that searches for the selected tool's possible moves across the map  */
            setPossiblePass(Possible_Moves, Selected_tool, true, 1);

        }
    }

    /**
     * setPossiblePass function gets a coordinate and a list and Updates
     * all the possible moves from the coordinate's position into the list
     *
     * @param poss_coordinates - list for the containing of the possible moves
     * @param possible_move    - selected tool
     * @param first_time       -
     * @param jump_number      -
     */
    public void setPossiblePass(List<Coordinate> poss_coordinates, Coordinate possible_move, boolean first_time, double jump_number) {

        Player Red_player = board.getRedPlayer();
        Player Green_Player = board.getGreenPlayer();

        Iterator iterator = Red_player.getPlayerTools().entrySet().iterator();
        boolean red = true;
        /* Iterate and calculate the distance of each red tool , if the distance shows that its in the same line,it will
         * take a possible move accordingly  */
        while (iterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) iterator.next();

            if (red)
                Red_player.clearVisitedTools();
            else {
                Green_Player.clearVisitedTools();

            }
            if (!iterator.hasNext() && red) {
                Red_player.clearVisitedTools();
                iterator = Green_Player.getPlayerTools().entrySet().iterator();
                red = false;
            }

            Coordinate current_cor = (Coordinate) mapElement.getKey();

            if (current_cor.equals(possible_move)) {
                /* set the current tool as visited in the tools set */
                current_cor.setAsVisited();
            }
            if (!current_cor.isVisited()) {
                double number_of_tools_distance = possible_move.distance(current_cor) / App.TOOLS_DISTANCE;

                /* if the two coordinates are on a same axis  */
                if (Math.abs(Math.round(number_of_tools_distance) - number_of_tools_distance) <= 0.0001 &&
                        (Math.round(number_of_tools_distance) == Math.round(jump_number) || first_time)) {
                    /* set the current tool as visited   */
                    current_cor.setAsVisited();
                    /* get where is the coordinate relative to the tool   */
                    Direction relative_direction = possible_move.getRelativeDirectionOfCor(current_cor);

                    if (relative_direction != Direction.NONE && isClosestInAxis(possible_move, current_cor, relative_direction)) {

                        /* build coordinate number_of_tools_distance from the tool at the same direction   */
                        Coordinate possible_coordinate = new Coordinate(current_cor);
                        /* add to the possible coordinate the wanted coordinate representing direction number of tools distance times */
                        possible_coordinate.addCoordinate(board.getDirections().getDirection(relative_direction), (int) Math.round(number_of_tools_distance));


                        /* check if the move is valid, if it does add to the possible moves pass list (coordinates)  */
                        if (isLegalMove(possible_coordinate) && isClosestInAxis(current_cor, possible_coordinate, relative_direction)
                                && !poss_coordinates.contains(possible_coordinate)) {
                            /* add coordinate to the list of coordinates  */
                            poss_coordinates.add(possible_coordinate);
                            setPossiblePass(poss_coordinates, possible_coordinate, false, number_of_tools_distance);
                            /* set all of the tools to unvisited  */
                            if (red)
                                Red_player.clearVisitedTools();
                            else {
                                Green_Player.clearVisitedTools();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Is_Legal_Move function is activated in order to check if a given coordinate
     * is legal move meaning its not taken by other tool and is inside the board boundaries
     */
    public boolean isLegalMove(Coordinate possible_move) {

        HashMap<Coordinate, HexNode> green_player_hashmap = (board.getGreenPlayer().getPlayerTools());
        HashMap<Coordinate, HexNode> red_player_hashmap = (board.getRedPlayer().getPlayerTools());
        /* checks whether the given move is already filled with other tool */
        if (red_player_hashmap.containsKey(possible_move) || green_player_hashmap.containsKey(possible_move)) {
            return false;
        }
        /* checks whether the given coordinate move is in the game boundaries */
        return board.isInBoardBounds(possible_move);

    }

    /**
     * activated for two tools that are on the same axis
     * gets selected tool , coordinate, and it's relative direction -> if the is a tool standing in between those
     * two tools the functions will return false
     *
     * @return boolean
     */
    public boolean isClosestInAxis(Coordinate selected_tool, Coordinate coordinate, Direction relative_direction) {

        Player Red_player = board.getRedPlayer();
        Player Green_Player = board.getGreenPlayer();

        Iterator iterator = Red_player.getPlayerTools().entrySet().iterator();
        Iterator iterator2 = Green_Player.getPlayerTools().entrySet().iterator();

        /* Iterate and calculate the distance of each red tool , if the distance shows that its in the same line,it will
         * take a possible move accordingly  */
        while (iterator.hasNext() && iterator2.hasNext()) {
            Map.Entry mapElement = (Map.Entry) iterator.next();
            Coordinate red_current_cor = (Coordinate) mapElement.getKey();

            Map.Entry mapElement2 = (Map.Entry) iterator2.next();
            Coordinate green_current_cor = (Coordinate) mapElement2.getKey();

            switch (relative_direction) {
                case DOWN_LEFT:
                    if (red_current_cor.getZ() == coordinate.getZ()) {
                        if (red_current_cor.getX() > coordinate.getX() && red_current_cor.getX() < selected_tool.getX())
                            return false;

                    }
                    if (green_current_cor.getZ() == coordinate.getZ()) {
                        if (green_current_cor.getX() > coordinate.getX() && green_current_cor.getX() < selected_tool.getX())
                            return false;
                    }

                    break;
                case LEFT:
                    if (red_current_cor.getY() == coordinate.getY()) {
                        if (red_current_cor.getX() > coordinate.getX() && red_current_cor.getX() < selected_tool.getX())
                            return false;

                    }
                    if (green_current_cor.getY() == coordinate.getY()) {
                        if (green_current_cor.getX() > coordinate.getX() && green_current_cor.getX() < selected_tool.getX())
                            return false;
                    }

                    break;
                case UPPER_LEFT:
                    if (red_current_cor.getX() == coordinate.getX()) {
                        if (red_current_cor.getY() > coordinate.getY() && red_current_cor.getY() < selected_tool.getY())
                            return false;

                    }
                    if (green_current_cor.getX() == coordinate.getX()) {
                        if (green_current_cor.getY() > coordinate.getY() && green_current_cor.getY() < selected_tool.getY())
                            return false;
                    }

                    break;
                case UPPER_RIGHT:
                    if (red_current_cor.getZ() == coordinate.getZ()) {
                        if (red_current_cor.getY() > coordinate.getY() && red_current_cor.getY() < selected_tool.getY())
                            return false;

                    }
                    if (green_current_cor.getZ() == coordinate.getZ()) {
                        if (green_current_cor.getY() > coordinate.getY() && green_current_cor.getY() < selected_tool.getY())
                            return false;
                    }

                    break;
                case RIGHT:
                    if (red_current_cor.getY() == coordinate.getY()) {
                        if (red_current_cor.getX() < coordinate.getX() && red_current_cor.getX() > selected_tool.getX())
                            return false;

                    }
                    if (green_current_cor.getY() == coordinate.getY()) {
                        if (green_current_cor.getX() < coordinate.getX() && green_current_cor.getX() > selected_tool.getX())
                            return false;
                    }

                    break;
                case DOWN_RIGHT:
                    if (red_current_cor.getX() == coordinate.getX()) {
                        if (red_current_cor.getY() < coordinate.getY() && red_current_cor.getY() > selected_tool.getY())
                            return false;

                    }
                    if (green_current_cor.getX() == coordinate.getX()) {
                        if (green_current_cor.getY() < coordinate.getY() && green_current_cor.getY() > selected_tool.getY())
                            return false;
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * checkForWin function checks whether the current player won the game
     *
     * @return boolean - if there is a win or not
     */
    public boolean checkForWin() {

        Players Current_Player = board.getCurrentPlayer();
        //add initial nodes
        if (Current_Player == Players.RED_PLAYER) {
            Player Red_Player = board.getRedPlayer();

            for (int z = 4; z > 0; z--) {
                for (int x = 4; x > (4 - z); x--) {
                    Coordinate coordinate = new Coordinate(x, -(z + x), z);
                    if (!Red_Player.containsTool(coordinate)) {
                        return false;
                    }
                }
            }
        } else if (Current_Player == Players.GREEN_PLAYER) {
            Player Green_Player = board.getGreenPlayer();
            for (int z = -4; z < 0; z++) {
                for (int x = -4; x < -(4 + z); x++) {
                    Coordinate coordinate = new Coordinate(x, -(z + x), z);
                    if (!Green_Player.containsTool(coordinate)) {
                        return false;
                    }
                }
            }
        }
        return true;


    }


    /**
     * This method is used to evaluate the current state of the game board
     * and return the score considering the current player (the player that the method was activated in his turn)
     * @return double - The score of the board
     */
    public double evaluateBoard() {
        /* Get current player */
        Players CurrentPlayer = board.getCurrentPlayer();
        if(CurrentPlayer == Players.RED_PLAYER)
            return redPlayerDistance() - greenPlayerDistance();
        else
            return greenPlayerDistance() - redPlayerDistance();
    }


    /**
     * this method is used to sum up all of the distances of the green player
     * from the top of the  opposite triangle.
     * @return double - sum of distances
     */
    public double greenPlayerDistance()
    {
        double Green_Distance_Sum = 0;
        Coordinate Green_Player_Destination = new Coordinate(-4, 8, -4);
        Iterator green_iterator;

        green_iterator = board.getGreenPlayer().getPlayerTools().entrySet().iterator();
        Map.Entry mapElement2;
        /* Iterate over the green player tools and sum up their distances */
        while (green_iterator.hasNext()) {

            mapElement2 = (Map.Entry) green_iterator.next();
            Coordinate iterator_Coor2 = (Coordinate) mapElement2.getKey();
            Green_Distance_Sum += Green_Player_Destination.distance(iterator_Coor2);
//            Green_Distance_Sum += distance(Green_Player_Destination,iterator_Coor2);

        }

        return Green_Distance_Sum;


    }
    /**
     * this method is used to sum up all of the distances of the red player
     * from the top of the  opposite triangle.
     *  @return double - sum of distances
     */
    private double redPlayerDistance()
    {
        double Red_Distance_Sum = 0;
        Coordinate Red_Player_Destination = new Coordinate(4, -8, 4);
        Iterator red_iterator;

        red_iterator = board.getRedPlayer().getPlayerTools().entrySet().iterator();
        Map.Entry mapElement2;

        /* Iterate over the red player tools and sum up their distances */
        while (red_iterator.hasNext()) {
            mapElement2 = (Map.Entry) red_iterator.next();
            Coordinate iterator_Coor2 = (Coordinate) mapElement2.getKey();
            Red_Distance_Sum += Red_Player_Destination.distance(iterator_Coor2);
//            Red_Distance_Sum += distance(Red_Player_Destination,iterator_Coor2);
        }
        return Red_Distance_Sum;

    }


    public void initBoard() {
        this.board = new GameBoard();
    }

    public GameBoard getBoard() {
        return this.board;
    }

    public void clearPossibleMoves() {
        this.Possible_Moves.clear();
    }

    public LinkedList<Coordinate> getPossibleMoves() {
        return this.Possible_Moves;
    }
}