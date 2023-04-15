import java.awt.*;
import java.util.*;

public class AiBot {

    Model gameModel;
    GameBoard board;
    /**
     * Constructor For AiBot class
     * @param gameModel - The model of the game where the Bot is Built
     */
    public AiBot(Model gameModel) {
        this.gameModel = gameModel;
        this.board = gameModel.getBoard();
    }
    /**
     * This method is used to Activate the Nega Max Algorithm
     * Using Alpha Beta purning in order to find the best move for the BOT
     * in the current state of the game board.
     * @param Depth - the maximum depth of the game tree
     * @param Alpha - The best score for move yet in the search tree
     * @param Beta - The worst score for move yet in the search tree
     * @return CoordinateMove - class represents the tool selected and the possible move to activate from it
     */
    public CoordinateMove negaMax(int Depth, double Alpha, double Beta,boolean flag)
    {
        /* Switch back to the previous player */
        board.switchPlayer();
        if(gameModel.checkForWin() || Depth == 0)
        {
            return new CoordinateMove(-evaluateBoard(flag));
        }
        /* Switch back to the current player */
        board.switchPlayer();

        /* Build a copy of the current players tool */
        HashMap<Coordinate,HexNode> nodes = buildMapCopy(this.board.getCurrentPlayerPlayer().getPlayerTools());
        Iterator it = nodes.entrySet().iterator();

        LinkedList<Coordinate> Possible_Moves ;
        /* Get current player */
        Players CurrentPlayer = board.getCurrentPlayer();

        Coordinate Outer_Best_Move = null;
        double Outer_best_move_score ;
        Coordinate Outer_Inner_Best_Move = null;

        /* Set the best score to minimum */
        Outer_best_move_score = -Double.MAX_VALUE;

        /* Iterate over the player's tools */
        while(it.hasNext()){

            /* Current node  */
            Map.Entry mapElement = (Map.Entry)it.next();

            Coordinate Selected_Coordinate = new Coordinate((Coordinate)mapElement.getKey());
            gameModel.selectTool((Coordinate)mapElement.getKey());


            Possible_Moves = new LinkedList(gameModel.getPossibleMoves());

            Coordinate Current_Player_Destination;

            /* Set the destination coordinate according to the current player */
            if (CurrentPlayer == Players.RED_PLAYER) {
                Current_Player_Destination = new Coordinate(4, -8, 4);
            } else {
                Current_Player_Destination = new Coordinate(-4, 8, -4);
            }

            /* sort the possible moves in order to make the alpha beta pruning more efficient */
            Collections.sort(Possible_Moves, Comparator.comparingInt(s ->(int)s.distance(Current_Player_Destination)));

            CurrentPlayer = board.getCurrentPlayer();

            Coordinate best_move = new Coordinate();
            double best_move_score ;

            /* Set the score of the tool's possible moves to minimum */
            best_move_score = -Double.MAX_VALUE;

            Players current = board.getCurrentPlayer();

            /* run over the current tool's possible moves */
            for (Coordinate move : Possible_Moves)
            {
                /* Make turn */
                gameModel.makeTurn(move);
                board.switchPlayer();
                CoordinateMove coordinateMove = negaMax(Depth-1,Alpha,Beta,flag);


                board.setCurrentPlayer(current);
                CurrentPlayer = board.getCurrentPlayer();


                /* UnDo move */
                gameModel.selectTool(move);
                gameModel.makeTurn(Selected_Coordinate);
                gameModel.selectTool(move);

                /* Is the last possible move is better then the best yet */
                if(coordinateMove.moveScore > best_move_score)
                {
                    best_move_score = coordinateMove.moveScore;
                    best_move = move;
                }
                /* Alpha-Beta Pruning */
                if(CurrentPlayer == Players.GREEN_PLAYER)
                {
                    Alpha = Math.max(Alpha, coordinateMove.moveScore);
                    if (Beta <= Alpha)
                    {
                        break;
                    }
                }
                else {
                    Beta = Math.min(Beta, coordinateMove.moveScore);
                    if (Beta <= Alpha)
                    {
                        break;
                    }
                }

            }
            /* Is the last tools best possible move is better then the best yet */
            if(best_move_score > Outer_best_move_score)
            {
                Outer_best_move_score = best_move_score;
                Outer_Best_Move = Selected_Coordinate;
                Outer_Inner_Best_Move = best_move;
            }

        }

        gameModel.board.getCurrentPlayerPlayer().setSelectedTool(null);
        /* return the best move */
        return new CoordinateMove(Outer_Best_Move,Outer_Inner_Best_Move,-Outer_best_move_score);

    }

    /**
     * This method is used to evaluate the current state of the game board
     * and return the score considering the current player (the player that the method was activated in his turn)
     * @return double - The score of the board
     */
    private double evaluateBoard(boolean flag) {
        /* Get current player */
        Players CurrentPlayer = board.getCurrentPlayer();
        if(CurrentPlayer == Players.RED_PLAYER)
            return redPlayerDistance(flag) - greenPlayerDistance(flag);
        else
            return greenPlayerDistance(flag) - redPlayerDistance(flag);
    }


    /**
     * this method is used to sum up all of the distances of the green player
     * from the top of the  opposite triangle.
     * @return double - sum of distances
     */
    public double greenPlayerDistance(boolean flag)
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
            if(flag)
                Green_Distance_Sum += Green_Player_Destination.distance(iterator_Coor2);
            else
                Green_Distance_Sum += distance(Green_Player_Destination,iterator_Coor2);

        }

        return Green_Distance_Sum;


    }
    /**
     * this method is used to sum up all of the distances of the red player
     * from the top of the  opposite triangle.
     *  @return double - sum of distances
     */
    private double redPlayerDistance(boolean flag)
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
            if(flag)
                Red_Distance_Sum += Red_Player_Destination.distance(iterator_Coor2);
            else
                Red_Distance_Sum += distance(Red_Player_Destination,iterator_Coor2);
        }
        return Red_Distance_Sum;

    }


    /**
     * this method is used to build a hard copy of the given HashMap
     * @param tools - HashMap that has to be copied
     * @return HashMap - Hard copy of the given HashMap
     */
    private HashMap buildMapCopy(HashMap tools)
    {
        /* Create new HashMap */
        HashMap<Coordinate,HexNode> Player_tool = new HashMap<>();

        Iterator it = tools.entrySet().iterator();
        /* Iterate over the given tools and copy them to the new HashMap */
        while(it.hasNext())
        {
            Map.Entry mapElement = (Map.Entry)it.next();
            Color c = ((HexNode)mapElement.getValue()).getPlayerColor();
            Coordinate Coordinate = new Coordinate((Coordinate)mapElement.getKey());

            Player_tool.put(new Coordinate(Coordinate), new HexNode(c));
        }

        return Player_tool;

    }

    /**
     * this function gets a number and reurn e^number
     */
    public double expon(double number){
        return Math.exp(number);
    }
    /**
     * this function gets two coordinates and return the distance of them
     */
    public double distance(Coordinate coordinate1,Coordinate coordinate2) {
        return expon(Math.sqrt(Math.pow(coordinate1.getX() - coordinate2.getX(), 2) + Math.pow(coordinate1.getY() - coordinate2.getY(), 2) + Math.pow(coordinate1.getZ() - coordinate2.getZ(), 2)));
    }

}
