import java.util.ArrayList;
import java.util.LinkedList;

public interface IPresenter {

     GameBoard getBoard();

     LinkedList<Coordinate> getPossibleMoves();

     Players makeTurn(double mouseX, double mouseY);

     void activateBot();

}
