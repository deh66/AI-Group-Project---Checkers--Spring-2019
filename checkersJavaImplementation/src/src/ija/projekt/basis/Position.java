/**
 * Basic elements of the game
 */
package ija.projekt.basis;

import ija.projekt.figures.Pawn;
import ija.projekt.figures.Rook;

import java.awt.Color;
import java.awt.Graphics;

/**
 * Represents the position on the desk.
 * @author  David Molnar
 */
public class Position {

    /**
     * Converts address (column): from form 0..x to a..x
     * @param col column to be converted
     * @return converted column
     */
    public static char getImagCol(int col) {
        return (char) (col + Desk.START_LETTER);
    }

    /**
     * Converts address (row): from form 0..x to 1..x
     * @param row to be converted
     * @return converted row
     */
    public static int getImagRow(int row) {
        return row + 1;
    }

    /**
     * Converts column address: from form a..x to 0..x
     * @param c input column
     * @return converted value
     */
    public static int getRealCol(char c) {
        char ch = Character.toLowerCase(c);
        return ch - Desk.START_LETTER;
    }

    /**
     * Converts row address: from form 1..x to 0..x
     * @param row input
     * @return converted value
     */
    public static int getRealRow(int row) {
        return row - 1;
    }

    /**
     * Brown color of the desk.
     */
    public static final Color BROWN_COLOR = new Color(92, 51, 23);
    
    /**
     * This variable stores the column part of address
     */
    protected char c;
    
    /**
	 * Stores the desk, where the position is.  
	 */
    protected Desk desk;

    /**
	 * Stores the figure on this position.
	 */
    protected Figure f;

    /**
     * Row part of the address.
     */
    protected int r;

    /**
	 * If the user clicks on a position, it is selected.
	 */
    protected boolean selected;

    /**
	 * Color of the position.
	 */
    protected boolean white;
    
    /**
     * Create new position based on col and row value
     * desk will be null and color: white
     * @param c column
     * @param r row
     */
    public Position(char c, int r) {
        this(null, c, r, true);
    }

    /**
     * Creates new position object
     * @param d desk
     * @param c column
     * @param r row
     * @param white color
     */
    public Position(Desk d, char c, int r, boolean white) {
        this.c = c;
        this.r = r;
        this.desk = d;
        this.white = white;
        this.selected = false;
    }

    /**
     * Creates new position
     * @param newPos copy values from this position object
     * @param d desk where the position is stored
     */
    public Position(Position newPos, Desk d) {
        this(d, newPos.c, newPos.r, newPos.white);
        this.selected = newPos.selected;
        if (newPos.f != null) {
            if (newPos.f instanceof Pawn) {
                this.f = new Pawn((Pawn) (newPos.f), this);
            } else if (newPos.f instanceof Rook) {
                this.f = new Rook((Rook) (newPos.f), this);
            }
        }
    }

    /**
     * Draw a graphical representation of this object
     * @param g where to draw
     * @param x0 left upper corner's x
     * @param y0 left upper corner's y
     * @param w width of position
     * @param h height of position
     */
    public void draw(Graphics g, int x0, int y0, int w, int h) {
        g.setColor(this.white ? (this.selected ? Color.white : Color.white)
                : (this.selected ? BROWN_COLOR : BROWN_COLOR));

        int x = x0 + getRealCol() * w;
        int y = y0 + getRealRow() * h;
        g.fillRect(x, y, w, h);

        // label
        // g.setColor(Color.red);
        // g.drawString(this.getPos(), x, y+30);
    }
    
    /**
     * Override default equals function.
     * Two object are identical if hashCode is equal.
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Position && o.hashCode() == hashCode()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get desk where the position is.
	 * @return desk
	 */
    public Desk getDesk() {
        return this.desk;
    }

    /**
     * Get figure stored in this position.
     * @return figure
     */
    public Figure getFigure() {
        return f;
    }

    /**
     * Get column value
     * @return column value: a..x
     */
    public char getImagCol() {
        return c;
    }

    /**
     * Get row value
     * @return row value: 1..x
     */
    public int getImagRow() {
        return r;
    }

    /**
     * Get column value
     * @return column value: 0..x
     */
    public int getRealCol() {
        char ch = Character.toLowerCase(c);
        return ch - Desk.START_LETTER;
    }

    /**
     * Get row value
     * @return row value: 0..x
     */
    public int getRealRow() {
        return r - 1;
    }

    /**
     * Hash Code for positition.
     */
    @Override
    public int hashCode() {
        String s = Integer.toString(this.c) + Integer.toString(this.r);
        return Integer.parseInt(s);
    }

    public boolean isBlack() {
        return !this.white;
    }

    /**
     * Returns true if this position is selected
	 * @return boolean: true/false
	 */
    public boolean isSelected() {
        return this.selected;
    }

    /**
     * Returns true if color is white
	 * @return color
	 */
    public boolean isWhite() {
        return this.white;
    }

    /**
     * Returns a position from desk, offsets dC and dR from current pos.
     * @param dC offset column
     * @param dR offset row
     * @return position from desk
     */
    public Position nextPosition(int dC, int dR) {
        int c = getRealCol(this.c) + dC;
        int r = getRealRow(this.r) + dR;

        return this.desk.getPositionAt(getImagCol(c), getImagRow(r));
    }

    /**
     * Stores figure f in the position.
     * Removes this position from figure.
     * @param f new figure to store
     * @return returns old figure
     */
    public Figure putFigure(Figure f) {
        Figure previous = this.f;
        if (previous != null) {
            previous.removePosition();
        }
        this.f = f;
        return previous;
    }

    /**
     * Removes current figure.
     * Removes this pos from figure too.
     * @return removed figure.
     */
    public Figure removeFigure() {
        Figure previous = this.f;
        if (previous != null) {
            previous.removePosition();
        }
        this.f = null;
        return previous;
    }

    /**
     * Compares this pos to position p
     * Returns true if they are in same column
     * @param p position to compare
     * @return determined value
     */
    public boolean sameColumn(Position p) {
        return p.c == this.c;
    }

    /**
     * Compare this position to position p
     * Returns true if they are in same row
     * @param p position to compare
     * @return determined value
     */
    public boolean sameRow(Position p) {
        return p.r == this.r;
    }

    /**
     * Selects this position.
     */
    public void select() {
        this.selected = true;
    }

    @Override
    public String toString() {
        return this.c + String.valueOf(this.r);
    }

    /** 
     * Removes selected flag.
     */
    public void unSelect() {
        this.selected = false;
    }
}