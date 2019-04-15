/**
 * Basic elements of the game
 */
package ija.projekt.basis;

import ija.projekt.game.Game;

/**
 * Represents a whole round: one step from player1 and one from player2
 * @author  David Molnar
 */
public class Round {

    /**
     * Creates new Round instance based on a string and a game
     * @param s string to parse
     * @param game round will be inserted to this game
     * @return newly created round.
     */
    public static Round parseRound(String s, Game game) {
        if (s == null) {
            return null;
        }

        String parts[] = s.split(" ");

        if (parts.length == 2) {
            int n = -1;
            try {
                n = Integer.parseInt(parts[0].substring(0,
                        parts[0].length() - 1)) - 1;
            } catch (NumberFormatException ex) {
                return null;
            }

            Step step1 = Step.parseStep(parts[1], game);

            if (step1 == null) {
                return null;
            }

            Round round = new Round(n);
            round.setStep1(step1);
            return round;
        }

        if (parts.length == 3) {
            int n = -1;
            try {
                n = Integer.parseInt(parts[0].substring(0,
                        parts[0].length() - 1)) - 1;
            } catch (NumberFormatException ex) {
                return null;
            }

            Step step1 = Step.parseStep(parts[1], game);
            Step step2 = Step.parseStep(parts[2], game);

            if (step1 == null || step2 == null) {
                return null;
            }

            Round round = new Round(n);
            round.setStep1(step1);
            round.setStep2(step2);
            return round;
        }

        return null;
    }

    /**
	 * This round is n'th round. (0..x)
	 */
    protected int n;
    
    /**
	 * Player #1 step.
	 */
    protected Step step1;

    /**
	 * Player #2 step.
	 */
    protected Step step2;

    /**
     * Create new round instance
     * @param n index of this round
     */
    public Round(int n) {
        this.n = n;
    }

    /**
     * Create a new round instance based on a round 
     * @param newRound copy values from this round
     * @param game newly created round will be inserted to this game
     */
    public Round(Round newRound, Game game) {
        this(newRound.n);
        if (newRound != null) {
            if (newRound.step1 != null) {
                this.step1 = new Step(newRound.step1, game);
            }
            if (newRound.step2 != null) {
                this.step2 = new Step(newRound.step2, game);
            }
        }
    }

    /**
     * Return index of round
	 * @return index
	 */
    public int getN() {
		return this.n;
	}

    /**
     * Returns player #1 step
	 * @return step
	 */
    public Step getStep1() {
		return this.step1;
	}

    /**
     * Return player #2 step
	 * @return step
	 */
    public Step getStep2() {
		return this.step2;
	}

    /**
     * Returns step count (0..2)
     * @return count
     */
    public int getStepCount() {
        int sum = 0;

        if (step1 != null) {
            sum += 1;
        }

        if (step2 != null) {
            sum += 1;
        }

        return sum;
    }

    /**
     * Sets player #1 step to s
	 * @param step
	 */
    public void setStep1(Step s) {
		this.step1 = s;
	}

    /**
     * Sets player #2 step to s
	 * @param step
	 */
    public void setStep2(Step s) {
		this.step2 = s;
	}

    /**
     * Override default toString method.
     */
    @Override
    public String toString() {
        return String.valueOf(n + 1) + ". " + step1.toString() + " "
                + (step2 != null ? step2.toString() : "");
    }
}
