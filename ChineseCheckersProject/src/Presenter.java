import java.time.Duration;import java.time.Instant;import java.util.LinkedList;public class Presenter implements IPresenter{    Model gameModel;    ISurface surface;    /**     * Presenter Constructor     * @param surface -     */    public Presenter(Surface surface) {        gameModel = new Model();        this.surface = surface;    }    /**     * makeTurn function get the mouse coordinates and:     * - if given coordinates are of a tool - it will be selected     * - if there is a selected tool already and the given coordinates are of possible move, the move will be activated     * @param mouseX - X axis coordinate of the clicked mouse position     * @param mouseY - Y axis coordinate of the clicked mouse position     * @return Players - the player who won the game, if none won null will be returned     */    public Players makeTurn(double mouseX, double mouseY)    {        Coordinate Selected_Coordinate ;        /* In case there is no selected move */        if(gameModel.board.getCurrentPlayerPlayer().getSelectedTool() == null) {            Selected_Coordinate = gameModel.getSelectedCor(mouseX, mouseY);            System.out.println("x:"+Selected_Coordinate.getX()+"y:"+Selected_Coordinate.getY()+"z:"+Selected_Coordinate.getZ());            gameModel.selectTool(Selected_Coordinate);        }        else {            /* get the selected tool by x and y coordinates */            Selected_Coordinate = gameModel.getSelectedPossibleCor(mouseX,mouseY);            /* if a tool was selected */            if (Selected_Coordinate != null)            {                System.out.println("x:"+Selected_Coordinate.getX()+"y:"+Selected_Coordinate.getY()+"z:"+Selected_Coordinate.getZ());                gameModel.makeTurn(Selected_Coordinate);                if(gameModel.checkForWin())                {                    System.out.format("Player: "+ gameModel.board.getCurrentPlayer() + "WON !!");                }                else {                    gameModel.board.switchPlayer();                    activateBot();                }            }            /* if a tool was not selected */            else            {                gameModel.selectTool(Selected_Coordinate);            }        }        gameModel.board.switchPlayer();        /* checks for win  */        if(gameModel.checkForWin())        {            System.out.format("Player: "+ gameModel.board.getCurrentPlayer() + "WON !!");            return gameModel.board.getCurrentPlayer();        }        else        {            gameModel.board.switchPlayer();            return null;        }    }    /**     * activateBot function gets the turn of the bot and activate it.     * @return Players - the player who won the game, if none won null will be returned     */    public void activateBot()    {        if(gameModel.board.getCurrentPlayer() == Players.GREEN_PLAYER)        {            double Alpha = Double.MIN_VALUE; // Worst for AI            double Beta = Double.MAX_VALUE;// Worst for Player            Instant start = Instant.now();            double greenPlayerDis = gameModel.aiBot.greenPlayerDistance(true);            System.out.println("greenPlayerDistance" + greenPlayerDis);            /* activate Nega-max function and get AI's bot */            CoordinateMove coordinateMove = greenPlayerDis > 50 ?                    greenPlayerDis > 150 ?                    gameModel.aiBot.negaMax(3,  Alpha, Beta,true) : gameModel.aiBot.negaMax(3,  Alpha, Beta,false) :                    gameModel.aiBot.negaMax(4,  Alpha, Beta,true);            Instant end = Instant.now();            Duration timeElapsed = Duration.between(start, end);            System.out.println("AI TIME IN Millis:" + timeElapsed.toMillis());            /* Do-AI's Move */            gameModel.selectTool(coordinateMove.startMoveCoordinate);            gameModel.makeTurn(coordinateMove.toMoveCoordinate);            gameModel.board.switchPlayer();            gameModel.selectTool(null);            System.out.println("Bot Turn: "+coordinateMove.startMoveCoordinate.toString() + "\nTO: "                    +coordinateMove.toMoveCoordinate.toString());        }    }    public GameBoard getBoard()    {        return this.gameModel.getBoard();    }    public LinkedList<Coordinate> getPossibleMoves()    {        return this.gameModel.getPossibleMoves();    }}