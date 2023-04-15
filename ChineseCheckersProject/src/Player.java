import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 *                   Player Class
 * The Player class represents a player in the game
 *
 */
public class Player {

    /** The player's color signature  */
    private Color playerColor;
    /**
     * Each player has 10 tools
     * Each tool has a unique Coordinate on the board
     */
    public HashMap<Coordinate,HexNode> playerTools;
    /**
     * The coordinate of the player's selected tool
     * If none is selected, Selected_tool contains null
     */
    private Coordinate selectedTool;


    /**
     * Player Constructor
     * @param player_color represents the player's color signature
     */
    public Player(Color player_color) {
        setPlayerColor(player_color);
        playerTools = new HashMap<Coordinate,HexNode>();
        selectedTool = null;

        /* add initial nodes */
       if(player_color == Color.GREEN)
       {
           for (int z = 4; z > 0; z--) {
               for (int x = 4; x > (4 - z); x--) {
                   playerTools.put(new Coordinate(x, -(z + x), z), new HexNode(player_color));
               }
           }
       }
       else if(player_color == Color.RED)
       {
           for (int z = -4; z < 0; z++) {
               for (int x = -4; x < -(4 + z); x++) {
                   playerTools.put(new Coordinate(x, -(z + x), z), new HexNode(player_color));
               }
           }
       }

    }

    /**
     * clearVisitedTools function runs over the player's tools and sets them to be unvisited
     */
    public void clearVisitedTools()
    {
        Iterator iterator = playerTools.entrySet().iterator();
        /* Iterate over the tools */
        while (iterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) iterator.next();
            Coordinate current_cor = (Coordinate) mapElement.getKey();
            current_cor.clearIsVisited();
        }

    }
    /**
     * containsTool function runs over the player's tools and sets them to be unvisited
     */
    public boolean containsTool(Coordinate possible_move)
    {
        return playerTools.containsKey(possible_move);
    }

    /**
     *   Getter and Setters
     */
    public Color getPlayerColor() {
        return playerColor;
    }
    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }
    public HashMap<Coordinate, HexNode> getPlayerTools() {
        return playerTools;
    }
    public void setPlayerTools(HashMap<Coordinate, HexNode> playerTools) {
        this.playerTools = playerTools;
    }

    public Coordinate getSelectedTool() {
        return selectedTool;
    }

    public void setSelectedTool(Coordinate selectedTool) {

        if(selectedTool != null) {
            if(playerTools.containsKey(selectedTool))
                this.selectedTool = selectedTool;
        }
        else
            this.selectedTool = null;

    }
}
