package androidsamples.java.tictactoe;

import org.junit.Test;

import static org.junit.Assert.*;

import androidsamples.java.tictactoe.models.GameModel;
import androidsamples.java.tictactoe.GameFragment;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testWin(){
        String[] gameArr= {"X","X","X",
                           "O","O","",
                            "","",""};
        int num = GameFragment.testCheckWin(gameArr);
        assertEquals(1,num);
    }
    @Test
    public void testLoss() {
        // O wins with a horizontal row (top row)
        String[] gameArr = {
                "O", "O", "O",  // O wins
                "X", "X", "",
                "", "X", ""
        };
        int num = GameFragment.testCheckWin(gameArr);
        assertEquals(-1, num);  // -1 represents a loss for X (because O won)
    }

    @Test
    public void testDraw() {
        // All spots are filled, but no one has won
        String[] gameArr = {
                "X", "O", "X",
                "X", "X", "O",
                "O", "X", "O"
        };
        int num = GameFragment.testCheckWin(gameArr);
        assertEquals(0, num);  // 0 represents a draw
    }

    @Test
    public void testWinDiagonalTopLeftToBottomRight() {
        // X wins with a diagonal from top-left to bottom-right
        String[] gameArr = {
                "X", "O", "O",
                "O", "X", "",
                "X", "", "X"
        };
        int num = GameFragment.testCheckWin(gameArr);
        assertEquals(1, num);  // 1 represents a win for X
    }
    @Test
    public void testLossBottomRow() {
        // O wins with the bottom row
        String[] gameArr = {
                "X", "X", "O",
                "X", "O", "",
                "O", "O", "O"  // O wins in the bottom row
        };
        int num = GameFragment.testCheckWin(gameArr);
        assertEquals(-1, num);  // -1 represents a loss for X, as O won
    }

}