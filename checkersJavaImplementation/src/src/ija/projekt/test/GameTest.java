/**
 * 
 */
package ija.projekt.test;

import ija.projekt.basis.Figure;
import ija.projekt.basis.Position;
import ija.projekt.figures.Rook;
import ija.projekt.game.Game;
import ija.projekt.game.Game.StepStatus;
import ija.projekt.game.Player;
import ija.projekt.players.HumanPlayer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author David Molnar
 */
public class GameTest {

    /**
     * 
     */
    protected Player player1, player2;

    /**
     * 
     */
    @Before
    public void setUp() {
        this.player1 = new HumanPlayer(true);
        this.player2 = new HumanPlayer(false);
    }

    /**
     * 
     */
    @After
    public void tearDown() {

    }

    /**
     * 
     */
    @Test
    public void test01() {
        Game game = new Game(player1, player2);

        Position p1 = game.getPositionAt('a', 3);
        Figure f1 = p1.getFigure();
        Position p2 = game.getPositionAt('b', 4);

        game.doMove(p1, p2);
        Figure f2 = p2.getFigure();

        OutputHelper.assertEquals("Test01: white pawn from a3 to b4", f1, f2);

        Position p3 = game.getPositionAt('b', 6);
        Figure f3 = p3.getFigure();
        Position p4 = game.getPositionAt('c', 5);
        game.doMove(p3, p4);
        Figure f4 = p4.getFigure();

        OutputHelper.assertEquals("Test01: black pawn from b6 to c5", f3, f4);
    }

    /**
     * 
     */
    @Test
    public void test02() {
        // players can not move other player's figures

        Game game = new Game(player1, player2);
        game.start();

        Position p1 = game.getPositionAt('b', 6);
        Figure f1 = p1.getFigure();
        Position p2 = game.getPositionAt('c', 5);
        game.doMove(p1, p2);
        Figure f2 = p2.getFigure();

        OutputHelper
                .assertNotSame(
                        "Test02: White player can not move black pawn b6 to c5",
                        f1, f2);

        Position p5 = game.getPositionAt('e', 3);
        Figure f5 = p5.getFigure();
        Position p6 = game.getPositionAt('f', 4);
        Figure f6 = p6.getFigure();
        game.doMove(p5, p6);

        OutputHelper.assertNotSame(
                "Test02: Black player can not move white e3 to f4", f5, f6);
    }

    /**
     * 
     */
    @Test
    public void test03() {
        // move pawn to same position

        Game game = new Game(player1, player2);

        Position p1 = game.getPositionAt('a', 3);
        Position p2 = game.getPositionAt('a', 3);
        StepStatus st = game.doMove(p1, p2);

        // move to same position
        OutputHelper.assertNotSame("Test03: white pawn a3 can not move to a3",
                st, StepStatus.OK);

        Position p4 = game.getPositionAt('b', 6);
        st = game.doMove(p4, p4);

        // move to same position
        OutputHelper.assertNotSame("Test03: black pawn b6 can not move to b6",
                st, StepStatus.OK);
    }

    /**
     * 
     */
    @Test
    public void test04() {
        // move pawn backward

        Game game = new Game(player1, player2);

        Position p1 = game.getPositionAt('a', 3);
        Position p2 = game.getPositionAt('b', 4);
        p1.getFigure().move(p2);

        StepStatus st = game.doMove(p2, p1);

        // move white pawn to backward
        OutputHelper.assertNotSame(
                "Test04: white pawn b4 can not move back to a3", st,
                StepStatus.OK);

        Position p3 = game.getPositionAt('b', 6);
        Position p4 = game.getPositionAt('c', 5);
        p3.getFigure().move(p4);

        st = game.doMove(p4, p3);

        // move black pawn to backward
        OutputHelper.assertNotSame(
                "Test04: black pawn c5 can not move back to b6", st,
                StepStatus.OK);
    }

    /**
     * 
     */
    @Test
    public void test05() {
        Game game = new Game(player1, player2);

        Position c3 = game.getPositionAt('c', 3);
        Figure c3f = c3.getFigure();
        Position d4 = game.getPositionAt('d', 4);

        game.doMove(c3, d4);

        Position f6 = game.getPositionAt('f', 6);
        Figure f6f = f6.getFigure();
        Position e5 = game.getPositionAt('e', 5);

        game.doMove(f6, e5);

        Figure d4f = d4.getFigure();
        Figure e5f = e5.getFigure();

        // OutputHelper.assertEquals("Test05: ", c3f, d4f);
        // OutputHelper.assertEquals("Test05: ", f6f, e5f);

        game.doMove(d4, f6);

        Figure f6f2 = f6.getFigure();

        OutputHelper.assertEquals(
                "Test05: white pawn d4 takes black pawn e5 and jumps to f6",
                f6f2, d4f);

        Figure e5f2 = e5.getFigure();

        OutputHelper.assertNull(
                "Test05: white pawn d4 takes black pawn e5, e5 is empty", e5f2);
    }

    /**
     * 
     */
    @Test
    public void test06() {
        Game game = new Game(player1, player2);

        game.doMove(game.getPositionAt('a', 3), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('b', 6), game.getPositionAt('c', 5));
        game.doMove(game.getPositionAt('b', 4), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('b', 2), game.getPositionAt('a', 3));
        game.doMove(game.getPositionAt('c', 5), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('c', 3), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('h', 6), game.getPositionAt('g', 5));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('g', 5), game.getPositionAt('h', 4));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('d', 8));

        Position d8 = game.getPositionAt('d', 8);
        Figure rook = d8.getFigure();

        OutputHelper.assertTrue(
                "Test06: white pawn c7 becomes a rook stepping on d8",
                rook instanceof Rook);
    }

    /**
     * 
     */
    @Test
    public void test07() {
        Game game = new Game(player1, player2);
        game.start();

        game.doMove(game.getPositionAt('c', 3), game.getPositionAt('d', 4));
        game.doMove(game.getPositionAt('f', 6), game.getPositionAt('e', 5));
        StepStatus st = game.doMove(game.getPositionAt('d', 4),
                game.getPositionAt('c', 5));

        OutputHelper.assertTrue(
                "Test07: white pawn from d4 must take black pawn on e5",
                st == StepStatus.MUST_MOVE);
    }

    /**
     * 
     */
    @Test
    public void test08() {
        // move pawn not empty position

        Game game = new Game(player1, player2);

        Position p1 = game.getPositionAt('a', 3);
        Position p3 = game.getPositionAt('a', 2);
        StepStatus st = game.doMove(p1, p3);

        // move to position which is not empty
        OutputHelper.assertNotSame(
                "Test08: white pawn a3 can not move to non empty a2", st,
                StepStatus.OK);

        Position p4 = game.getPositionAt('b', 6);
        Position p5 = game.getPositionAt('c', 7);
        st = game.doMove(p4, p5);

        // move to position which is not empty
        OutputHelper.assertNotSame(
                "Test08: black pawn b6 can not move to non empty c7", st,
                StepStatus.OK);
    }

    /**
     * 
     */
    @Test
    public void test09() {
        Game game = new Game(player1, player2);

        game.doMove(game.getPositionAt('a', 3), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('b', 6), game.getPositionAt('c', 5));
        game.doMove(game.getPositionAt('b', 4), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('b', 2), game.getPositionAt('a', 3));
        game.doMove(game.getPositionAt('c', 5), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('c', 3), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('h', 6), game.getPositionAt('g', 5));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('g', 5), game.getPositionAt('h', 4));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('d', 8));
        game.doMove(game.getPositionAt('g', 7), game.getPositionAt('h', 6));

        Position p1 = game.getPositionAt('d', 8);
        Figure f1 = p1.getFigure();
        Position p2 = game.getPositionAt('c', 7);
        game.doMove(p1, p2);
        Figure f2 = p2.getFigure();
        OutputHelper.assertEquals("Test09: White rook d8 moves to c7", f1, f2);
    }

    
    /**
     * 
     */
    @Test
    public void test10() {
        Game game = new Game(player1, player2);

        game.doMove(game.getPositionAt('a', 3), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('b', 6), game.getPositionAt('c', 5));
        game.doMove(game.getPositionAt('b', 4), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('b', 2), game.getPositionAt('a', 3));
        game.doMove(game.getPositionAt('c', 5), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('c', 3), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('h', 6), game.getPositionAt('g', 5));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('g', 5), game.getPositionAt('h', 4));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('d', 8));
        game.doMove(game.getPositionAt('g', 7), game.getPositionAt('h', 6));

        Position p1 = game.getPositionAt('d', 8);
        Figure f1 = p1.getFigure();
        Position p2 = game.getPositionAt('a', 5);
        game.doMove(p1, p2);
        Figure f2 = p2.getFigure();
        OutputHelper.assertEquals("Test10: White rook d8 moves to a5", f1, f2);
    }
    
    /**
     * 
     */
    @Test
    public void test11() {
        Game game = new Game(player1, player2);

        game.doMove(game.getPositionAt('a', 3), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('b', 6), game.getPositionAt('c', 5));
        game.doMove(game.getPositionAt('b', 4), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('b', 2), game.getPositionAt('a', 3));
        game.doMove(game.getPositionAt('c', 5), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('c', 3), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('h', 6), game.getPositionAt('g', 5));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('g', 5), game.getPositionAt('h', 4));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('d', 8));
        game.doMove(game.getPositionAt('g', 7), game.getPositionAt('h', 6));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('h', 6), game.getPositionAt('g', 5));

        Position p1 = game.getPositionAt('c', 7);
        Figure f1 = p1.getFigure();
        Position p2 = game.getPositionAt('e', 5);
        game.doMove(p1, p2);
        Figure f2 = p2.getFigure();
        Figure f3 = game.getPositionAt('d', 6).getFigure();
        OutputHelper.assertEquals("Test11: White rook d8 moves to e5", f1, f2);
        OutputHelper.assertNull(
                "Test11: White rook d8 moves to e5 and takes d6", f3);
    }

    /**
     * 
     */
    @Test
    public void test12() {
        Game game = new Game(player1, player2);

        game.doMove(game.getPositionAt('a', 3), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('b', 6), game.getPositionAt('c', 5));
        game.doMove(game.getPositionAt('b', 4), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('b', 2), game.getPositionAt('a', 3));
        game.doMove(game.getPositionAt('c', 5), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('c', 3), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('h', 6), game.getPositionAt('g', 5));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('g', 5), game.getPositionAt('h', 4));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('d', 8));
        game.doMove(game.getPositionAt('g', 7), game.getPositionAt('h', 6));

        Position p1 = game.getPositionAt('d', 8);
        Figure f1 = p1.getFigure();
        Position p2 = game.getPositionAt('a', 5);
        game.doMove(p1, p2);
        Figure f2 = p2.getFigure();
        OutputHelper.assertEquals("Test12: White rook d8 moves to a5", f1, f2);
    }

    /**
     * 
     */
    @Test
    public void test13() {
        Game game = new Game(player1, player2);

        game.doMove(game.getPositionAt('a', 3), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('b', 6), game.getPositionAt('c', 5));
        game.doMove(game.getPositionAt('b', 4), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('b', 2), game.getPositionAt('a', 3));
        game.doMove(game.getPositionAt('c', 5), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('c', 3), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('h', 6), game.getPositionAt('g', 5));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('g', 5), game.getPositionAt('h', 4));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('d', 8));
        game.doMove(game.getPositionAt('g', 7), game.getPositionAt('h', 6));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('f', 6), game.getPositionAt('g', 5));

        Position p1 = game.getPositionAt('a', 5);
        Figure f1 = p1.getFigure();
        Position p2 = game.getPositionAt('d', 8);
        game.doMove(p1, p2);
        Figure f2 = p2.getFigure();
        OutputHelper.assertEquals("Test13: White rook a5 moves back to d8", f1,
                f2);
    }

    /**
     * 
     */
    @Test
    public void test14() {
        Game game = new Game(player1, player2);

        game.doMove(game.getPositionAt('a', 3), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('b', 6), game.getPositionAt('c', 5));
        game.doMove(game.getPositionAt('b', 4), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('b', 2), game.getPositionAt('a', 3));
        game.doMove(game.getPositionAt('c', 5), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('c', 3), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('h', 6), game.getPositionAt('g', 5));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('g', 5), game.getPositionAt('h', 4));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('d', 8));
        game.doMove(game.getPositionAt('g', 7), game.getPositionAt('h', 6));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('f', 6), game.getPositionAt('g', 5));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('d', 8));
        game.doMove(game.getPositionAt('d', 6), game.getPositionAt('c', 5));

        StepStatus st = game.doMove(game.getPositionAt('d', 8),
                game.getPositionAt('c', 7));

        OutputHelper.assertTrue(
                "Test14: White rook d8 moves back to c7, must take e7",
                st == StepStatus.MUST_MOVE);
    }

    /**
     * 
     */
    @Test
    public void test15() {
        Game game = new Game(player1, player2);

        game.doMove(game.getPositionAt('a', 3), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('b', 6), game.getPositionAt('c', 5));
        game.doMove(game.getPositionAt('b', 4), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('b', 2), game.getPositionAt('a', 3));
        game.doMove(game.getPositionAt('c', 5), game.getPositionAt('b', 4));
        game.doMove(game.getPositionAt('c', 3), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('h', 6), game.getPositionAt('g', 5));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('c', 7));
        game.doMove(game.getPositionAt('g', 5), game.getPositionAt('h', 4));
        game.doMove(game.getPositionAt('c', 7), game.getPositionAt('d', 8));
        game.doMove(game.getPositionAt('g', 7), game.getPositionAt('h', 6));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('a', 5));
        game.doMove(game.getPositionAt('f', 6), game.getPositionAt('g', 5));
        game.doMove(game.getPositionAt('a', 5), game.getPositionAt('d', 8));
        game.doMove(game.getPositionAt('d', 6), game.getPositionAt('c', 5));
        game.doMove(game.getPositionAt('d', 8), game.getPositionAt('f', 6));
        game.doMove(game.getPositionAt('a', 7), game.getPositionAt('b', 6));
        game.doMove(game.getPositionAt('d', 2), game.getPositionAt('c', 3));
        game.doMove(game.getPositionAt('b', 6), game.getPositionAt('a', 5));

        StepStatus st = game.doMove(game.getPositionAt('f', 6),
                game.getPositionAt('b', 2));

        OutputHelper
                .assertTrue(
                        "Test15: White rook f6 can not move back to b2 jumping over c3 white pawn",
                        st != StepStatus.OK);
    }
}
