import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;


public class Surface extends JPanel implements MouseListener,ISurface, ActionListener {

    private final String BOARD_BACKGROUND = "BOARDBackGround.png";

    private BufferedImage image;

    private JButton rePlayBT;

    private IPresenter presenter;

    /**
     * Game Board
     */
     GameBoard board;

     LinkedList<Coordinate> possibleMoves;


    /********************FUNCTIONS************************/


    public Surface() {
        presenter = new Presenter(this);
        board = presenter.getBoard();
        possibleMoves = presenter.getPossibleMoves();

        rePlayBT = new JButton("Play Again");
        rePlayBT.setBounds(App.SCREEN_SIZE/2,2,50,500);
        rePlayBT.setVisible(false);
        rePlayBT.addActionListener(this);

        try {
            image = ImageIO.read(new File(BOARD_BACKGROUND));
        }
        catch(IOException ex){
            System.out.println("not found");
        }

        super.addMouseListener(this);
        super.add(rePlayBT);
    }

    /**
     * Helper method. Draws game to screen
     * @param g graphics object
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage(image,0,0,this);

        g2d.drawString("Current player:"+board.getCurrentPlayer(),0,20);

        HashMap<Coordinate,HexNode> nodes;
        Iterator it;

        /* Print player1(RED) tools  */
        nodes = this.board.getRedPlayer().getPlayerTools();
        it = nodes.entrySet().iterator();
        while(it.hasNext()){

           /* Current node  */
           Map.Entry mapElement = (Map.Entry)it.next();
           g2d.setColor(((HexNode)mapElement.getValue()).getPlayerColor());
           Ellipse2D e = ((Coordinate)mapElement.getKey()).getEllipse();
           g2d.fill(e);
           g2d.draw(e);

       }
        /* Print player2(GREEN) tools  */
        nodes = this.board.getGreenPlayer().getPlayerTools();
        it = nodes.entrySet().iterator();
        while(it.hasNext()) {

            Map.Entry mapElement = (Map.Entry) it.next();
            g2d.setColor(((HexNode) mapElement.getValue()).getPlayerColor());
            Ellipse2D e = ((Coordinate)mapElement.getKey()).getEllipse();
            g2d.fill(e);
            g2d.draw(e);

        }

        /* Print PossibleMoves  */
        LinkedList<Coordinate> Possible_moves = this.possibleMoves;
        for(int i=0;i<Possible_moves.size();i++)
        {
            g2d.setColor(Color.MAGENTA);
            Ellipse2D e = Possible_moves.get(i).getEllipse();
            g2d.fill(e);
            g2d.draw(e);
        }


    }

    @Override
    /**
     * Paints to screen. Calls draw method to draw game
     * @param g graphics object
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font font = new Font("David", Font.BOLD, 20);
        g.setFont(font);
        draw(g);
    }

    @Override
    /**
     * mouseClicked is activated
     * @param g graphics object
     */
    public void mouseClicked(MouseEvent e) {

        double mouseX = e.getX();
        double mouseY = e.getY();
        Players winner = presenter.makeTurn(mouseX,mouseY);
        if(winner != null) {
            JOptionPane.showMessageDialog(null,winner.toString()+" Won");
            rePlayBT.setVisible(true);
        }
        else {
            board = presenter.getBoard();
            possibleMoves = presenter.getPossibleMoves();
            repaint();
        }




    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        rePlayBT.setVisible(false);
        presenter = new Presenter(this);
        board = presenter.getBoard();
        possibleMoves = presenter.getPossibleMoves();

        repaint();
    }
}
