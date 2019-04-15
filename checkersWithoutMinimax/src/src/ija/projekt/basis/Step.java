/**
 * Basic elements of the game
 */
package ija.projekt.basis;

import ija.projekt.game.Game;

/**
 * Represents one step on the desk. (from->to)
 * @author  David Molnar
 */
public class Step {

    public static final char POSITION_SEPARATOR = '-';
    public static final char POSITION_X_SEPARATOR = 'x';

    /**
     * Creates new step on input s and a game
     * @param s input string
     * @param game where the step will be added
     * @return returns newly created step.
     */
    public static Step parseStep(String s, Game game) {
        if (s == null) {
            return null;
        }
        
        char sep = s.charAt(2);

        if (s.length() == 5 && (sep == POSITION_SEPARATOR || sep == POSITION_X_SEPARATOR)) {
            int fromR = -1;
            int toR = -1;

            try {
                fromR = Integer.parseInt(Character.toString(s.charAt(1)));
                toR = Integer.parseInt(Character.toString(s.charAt(4)));
            } catch (NumberFormatException ex) {
                return null;
            }

            //Position from = new Position(null, s.charAt(0), fromR, true);
            Position from = game.getPositionAt(s.charAt(0), fromR);
            //Position to = new Position(null, s.charAt(3), toR, true);
            Position to = game.getPositionAt(s.charAt(3), toR);
            
            if (from != null && to != null) {
                Step step = new Step(from, to);
                step.readedSeparator = sep;
                
                return step;
            }
        }

        return null;
    }
    
    /**
	 * Stores from part of the step.
	 */
    protected Position from;
    
    /**
     * If the step was constructed from a string this will be set the value between two positions.
     */
    protected char readedSeparator = 0;
    
    /**
     * Stores the to part of the step.
	 */
	protected Position to;

    /**
	 * Stores the position which the step is jumping over.
	 * It has a figure, which is taken, when the step is executed.
	 */
	protected Position x;
	
	/**
	 * Creates new step instance
	 * @param from position
	 * @param to position
	 */
	public Step(Position from, Position to) {
        this.from = from;
        this.to = to;
    }
	
	/**
	 * Creates new position
	 * @param from position
	 * @param to position
	 * @param x position
	 */
	public Step(Position from, Position to, Position x) {
        this(from, to);
        this.x = x;
    }
	
	/**
	 * Creates new step instance. Copies values from newStep.
	 * @param newStep copy values from this instance
	 * @param game step will be addedd in this game.
	 */
	public Step(Step newStep, Game game) {
        this(game.getPositionAt(newStep.from), game.getPositionAt(newStep.to),
                game.getPositionAt(newStep.x));
        this.readedSeparator = newStep.readedSeparator;
    }

	/**
	 * Compares two positions. If both are null returns true, and if not noll
	 * then returns true if are equal.
	 * @param p1 position
	 * @param p2 position
	 * @return true / false
	 */
    protected boolean checkStep(Position p1, Position p2) {
        if (p1 != null) {
            if (p1.equals(p2) == false) {
                return false;
            }
        } else {
            if (p2 != null) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Equal if all positions are true (from, to, x)
     * @return determined value
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Step) {
            Step step = (Step)obj;
            
            if (!checkStep(this.from, step.from)) {
                return false;
            }
            
            if (!checkStep(this.to, step.to)) {
                return false;
            }
            
            if (!checkStep(this.x, step.x)) {
                return false;
            }
            
            return true;
        }
        return false;
    }

    /**
     * Getter for property from.
	 * @return from position.
	 */
    public Position getFrom() {
		return this.from;
	}

    /**
     * Getter for readedSeparator property.
     * @return readedSeparator
     */
    public char getReadedSeparator() {
        return this.readedSeparator;
    }

    /**
     * Getter for to position
	 * @return to position
	 */
    public Position getTo() {
		return this.to;
	}

    /**
     * Getter for x position
	 * @return x position
	 */
    public Position getX() {
		return this.x;
	}

    /**
     * Setter for property x
     * @param x position
     */
    public void setX(Position x) {
        this.x = x;
    }
    
    /** 
     * Override default toString method
     */
    @Override
    public String toString() {
        return from.toString()
                + (x == null ? Character.toString(POSITION_SEPARATOR) : Character.toString(POSITION_X_SEPARATOR))
                + to.toString();
    }
}
