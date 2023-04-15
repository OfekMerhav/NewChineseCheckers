import javafx.scene.layout.BackgroundImage;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class App extends JFrame {
    /** SCREEN AND BOARD INFORMATION */
    public static final int SCREEN_SIZE = 900;

    public static final int BOARD_RADIUS = 5;
    public static final int HEX_DIAMETER = (SCREEN_SIZE*2/3)/(BOARD_RADIUS + 2*(BOARD_RADIUS-1));
    //public static final int HEX_DIAMETER = 35;
    public static final int PIECE_DIAMETER = HEX_DIAMETER*4/5;
    public static final int PIECE_RADIUS = PIECE_DIAMETER/2;

    /** GAME INFORMATION */
    public static final double TOOLS_DISTANCE= Math.sqrt(2);

    /**
     * Constructs application by initializing the board and the screen
     */
    public App() {
        initScreen();
    }


    /**
     * Creates the screen and adds a surface to it to draw on
     */
    private void initScreen() {

        add(new Surface());
        pack();
        setTitle("Chinese Checkers");
        setSize(SCREEN_SIZE, SCREEN_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * main function
     */
    public static void main(String[] args) {
        App app = new App();
        app.setVisible(true);
    }

}
