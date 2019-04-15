/**
 * Basic elements of the game
 */
package ija.projekt.basis;

/**
 * Represents the game desk.
 * It stores the positions on this desk.
 * @author  David Molnar
 */
public class Desk {

    public static final char START_LETTER = 'a';
    
    /** 
     * Dimension of the desk.
     */
    protected int dim;
    
    /**
     * Desk's positions are addressed bz a letter and a number.
     * This variable represents the last possible letter.
	 */
    protected char endLetter;
    
    /**
     * This variable stores the positions.
	 */
    protected Position pos[][];

    /**
     * Creates  new desk instance based on an existing desk.
     * Copies all the values from existing desk.
     */
    public Desk(Desk newDesk) {
        this(newDesk.dim);
        boolean flag = false;

        // Copy positions
        for (char c = Desk.START_LETTER; c <= this.endLetter; c++) {
            for (int r = 1; r <= dim; r++) {
                pos[c - Desk.START_LETTER][r - 1] = new Position(newDesk.getPositionAt(c, r), this);
                flag = !flag;
            }
            flag = !flag;
        }
    }

    /**
     * Create new desk, dimension: dim x dim
     * @param dim dimension of desk
     */
    public Desk(int dim) {
        this.dim = dim;
        this.endLetter = (char) (START_LETTER + this.dim - 1);
        boolean flag = false; // a1 is white

        this.pos = new Position[dim][];

        for (char c = Desk.START_LETTER; c <= this.endLetter; c++) {
            pos[c - Desk.START_LETTER] = new Position[dim];

            for (int r = 1; r <= dim; r++) {
                pos[c - Desk.START_LETTER][r - 1] = new Position(this, c, r,
                        flag);
                flag = !flag;
            }
            flag = !flag;
        }
    }

    /**
     * Get the dimension of desk
     * @return dimension
     */
    public int getDimension() {
        return this.dim;
    }

    /**
     * Return last column letter.
	 * @return column last letter
	 */
    public char getEndLetter() {
        return this.endLetter;
    }

    /**
     * Returns the figure on the given position.
     * If address is not valid, returns null.
     * @param c column
     * @param r row
     * @return figure
     */
    public Figure getFigureAt(char c, int r) {
        Position pos = getPositionAt(c, r);
        if (pos == null) {
            return null;
        }
        return pos.getFigure();
    }

    /**
     * Returns a position on the given position.
     * If address is not valid, returns null.
     * @param c column
     * @param r row
     * @return position
     */
    public Position getPositionAt(char c, int r) {
        if (outOfBoundaries(c, r)) {
            return null;
        }
        return pos[c - Desk.START_LETTER][r - 1];
    }
    
    /**
     * Determines if the address is a valid address or not.
     * @param c column
     * @param r row
     * @return true if valid, false if not valid.
     */
    protected boolean outOfBoundaries(char c, int r) {
        return c < Desk.START_LETTER || c > this.endLetter || r < 1
                || r > this.dim;
    }
}
