/**
 * Basic elements of the game
 */
package ija.projekt.basis;

import java.awt.Graphics;
import java.util.List;

/**
 * This class represents the figure on the desk.
 * @author  David Molnar
 */
public abstract class Figure {

    /**
     * Stores the position from a desk, in which the figure is/
	 */
    protected Position p;
    
    /**
     * Stores the color of the figure.
     * If true, color is white, if false color is black.
	 */
    protected boolean white;

    /**
     * Creates new figure based on existing one.
     * Puts the new figure on position p.
     * @param newF existing figure
     * @param p put new figure here
     */
    public Figure(Figure newF, Position p) {
        this(p, newF.white);
    }

    /**
     * Create new figure.
     * @param p store figure on position p
     * @param white new figue's color
     */
    public Figure(Position p, boolean white) {
        this.p = p;
        // this.p.putFigure(this);
        this.white = white;
    }

    /**
     * Returns true if this figure can move to position p, false otherwise.
     * @param p test moving to this position
     * @return true or false
     */
    public abstract Step canMove(Position p);

    /**
     * Draws figure on g. 
     * @param g where to draw
     * @param x0 right upper corner's x (position)
     * @param y0 right upper corner's y (position)
     * @param w width of position
     * @param h height of position
     */
    public abstract void draw(Graphics g, int x0, int y0, int w, int h);

    /**
     * Returns position where the figure is.
     * @return the position
     */
    public Position getPosition() {
        return this.p;
    }

    /**
     * Returns all steps (from, to) which this figure can do.
     * @return all possible steps this figure can do
     */
    public abstract List<Step> getSteps();

    /**
     * If this figure is black, returns true, otherwise false.
     * @return true if figures color is black
     */
    public boolean isBlack() {
        return !this.white;
    }

    /**
     * If this figure is white, returns true, otherwise false.
	 * @return true if figures color is white
	 */
    public boolean isWhite() {
        return this.white;
    }

    /**
     * Move this figure to position p.
     * @param p move to this position
     */
    public void move(Position p) {
        this.p.removeFigure();
        this.p = p;
        this.p.putFigure(this);
    }

    /**
     * Removes the position.
     * @return the removed position
     */
    public Position removePosition() {
        Position previous = this.p;
        this.p = null;
        return previous;
    }
}
