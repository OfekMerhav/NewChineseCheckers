import java.awt.*;

/**
 * HexNode Class
 * The HexNode class represents a single hex node in the board
 * Each player has 10 hex nodes in any time of the game
 */
public class HexNode {

    private Color playerColor;

    /**
     * HexNode Constructor
     */
    public HexNode(Color playerColor) {
        this.playerColor = playerColor;
    }

    /**
     *   Getter and Setters
     */
    public Color getPlayerColor() {
        return playerColor;
    }
}
