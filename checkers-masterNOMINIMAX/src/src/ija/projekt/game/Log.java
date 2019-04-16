/**
 * 
 */
package ija.projekt.game;

import ija.projekt.basis.Round;
import ija.projekt.basis.Step;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Molnar
 */
public class Log {

    /**
     * 
     */
    protected int n;
    
    /**
     * 
     */
    protected List<Round> rounds;

    /**
     * 
     */
    public Log() {
        this.n = 0;
        this.rounds = new ArrayList<Round>();
    }

    /**
     * 
     * @param newLog
     * @param game
     */
    public Log(Log newLog, Game game) {
        this();
        this.n = newLog.n;
        for (Round round : newLog.rounds) {
            this.rounds.add(new Round(round, game));
        }
    }

    /**
     * 
     * @param step
     * @return
     */
    public Round add(Step step) {
        Round r = null;
        if (rounds.size() > 0) {
            r = rounds.get(rounds.size() - 1);
        }

        if (r != null && r.getStep2() == null) {
            r.setStep2(step);
        } else {
            r = new Round(n);
            this.n += 1;
            r.setStep1(step);
            rounds.add(r);
        }
        return r;
    }

    /**
     * 
     */
    public void clear() {
        this.n = 0;
        this.rounds.clear();
    }

    /**
     * 
     * @return
     */
    public Round getLastRound() {
        if (this.rounds != null && this.rounds.size() > 0) {
            return this.rounds.get(this.rounds.size() - 1);
        }

        return null;
    }

    /**
     * 
     * @return
     */
    public Step getLastStep() {
        if (this.rounds != null && this.rounds.size() > 0) {
            Round round = this.rounds.get(this.rounds.size() - 1);
            Step white = round.getStep1();
            Step black = round.getStep2();

            return black == null ? white : black;
        }

        return null;
    }

    /**
     * 
     * @return
     */
    public List<Round> getLog() {
        return rounds;
    }

    /**
     * 
     * @return
     */
    public List<Step> getSteps() {
        List<Step> steps = new ArrayList<Step>();

        if (this.rounds != null) {
            for (Round round : rounds) {
                Step step1 = round.getStep1();
                Step step2 = round.getStep2();

                if (step1 != null) {
                    steps.add(step1);
                }

                if (step2 != null) {
                    steps.add(step2);
                }
            }
        }

        return steps;
    }
}
