/**
 * 
 */
package ija.projekt.gui;

import ija.projekt.basis.Desk;
import ija.projekt.basis.Figure;
import ija.projekt.basis.Position;
import ija.projekt.game.Game;
import ija.projekt.game.Player;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * @author  David Molnar
 */
public class GameBoard extends JPanel {

    /**
     * 
     */
    protected int dim;
    
    /**
     * 
	 */
    protected Game game;

    /**
     * 
     */
    protected int h, hA;
    
    /**
     * 
     */
    protected final int hD = 30;
    
    /**
     * 
     */
    protected boolean isInteractive;

    /**
     * 
	 */
    protected Position pos;

    /**
     * 
     */
    protected int w, wA;

    /**
     * 
     */
    protected final int wD = 30;

    /**
     * 
     * @param game
     */
    public GameBoard(Game game) {
        setGame(game);

        this.isInteractive = true;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                click(e.getX(), e.getY());
            }
        });
    }

    /**
     * 
     * @param x
     * @param y
     */
    public void click(int x, int y) {
        if (x < wD || x > (wA + wD) || y < hD || y > (hA + hD)) {
            return;
        }

        int xN = (int) (Math.ceil((double) (x - wD) / wA * dim)) - 1;
        int yN = (int) (Math.ceil((double) (y - hD) / hA * dim)) - 1;

        char c = Position.getImagCol(xN);
        int r = Position.getImagRow(yN);
        
        Position actualPos = this.game.getPositionAt(c, r);
        Player cp = this.game.getCurrentPlayer();
        
        if (cp == null || actualPos == null) {
            return;
        }

        if (pos == null) {
            Figure f = actualPos.getFigure();
            
            if (f != null && f.isWhite() == cp.isWhite()
                    && cp.isInteractive() && !this.game.getFinished()
                    && this.isInteractive) {
                pos = actualPos;
                pos.select();
            }
        } else {
            cp.doMove(pos, actualPos);
            pos.unSelect();
            pos = null;
        }

        this.repaint();
    }

    /**
     * 
     * @param g
     * @param w
     * @param h
     * @param wD
     * @param hD
     */
    private void drawTable(Graphics g, int w, int h, int wD, int hD) {
        // table & figures
        for (char c = Desk.START_LETTER; c <= this.game.getEndLetter(); c++) {
            for (int r = 1; r <= this.game.getDeskDimension(); r++) {
                Position p = this.game.getPositionAt(c, r);
                p.draw(g, wD, hD, w, h);

                Figure f = p.getFigure();

                if (f != null) {
                    f.draw(g, wD, hD, w, h);
                }
            }
        }

        // labels
        g.setColor(Color.black);

        for (int x = 0; x < this.game.getDeskDimension(); x++) {
            g.drawString(String.valueOf(x + 1), wD / 2, x * h + hD + h / 2);
            g.drawString(Character.toString((char) (Desk.START_LETTER + x)), x
                    * w + wD + w / 2, hD / 2);
        }
    }

    /**
     * 
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (game == null) {
            return;
        }

        w = (getWidth() - wD * 2) / dim;
        h = (getHeight() - hD * 2) / dim;
        wA = dim * w;
        hA = dim * h;

        drawTable(g, w, h, wD, hD);
    }

    /**
     * 
	 * @param game
	 */
    public void setGame(Game game) {
        this.game = game;

        if (this.game != null) {
            this.dim = game.getDeskDimension();
        }
    }

    /**
     * 
     * @param mode
     */
    public void setInteractivity(boolean mode) {
        this.isInteractive = mode;
        this.repaint();
    }
}
