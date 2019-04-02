package ija.projekt.replay;

import ija.projekt.basis.Step;
import ija.projekt.game.Game;
import ija.projekt.game.Player;

import java.util.List;

/**
 * @author David Molnar
 */
public class GameReplay extends Game {

    /**
     * 
	 */
    protected int actualStep;

    /**
     * 
     * @param newG
     */
    public GameReplay(Game newG) {
        super(newG);
        setActualStepToLast();
    }

    /**
     * 
     * @param player1
     * @param player2
     */
    public GameReplay(Player player1, Player player2) {
        super(player1, player2);
    }

    /**
     * 
	 * @return
	 */
    public Step getActualStep() {
        List<Step> steps = getSteps();
        int index = getActualStepIndex();
        if (index >= 0 && index < steps.size()) {
            return steps.get(index);
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public int getActualStepIndex() {
        return this.actualStep;
    }

    /**
     * 
     * @param stepIndex
     */
    public void goToStep(int stepIndex) {
        if (stepIndex < -1 || stepIndex >= this.getStepCount()) {
            return;
        }

        this.actualStep = stepIndex;

        createFigures(this.desk);
        int currentStep = -1;

        for (Step step : this.log.getSteps()) {
            if (currentStep >= stepIndex) {
                break;
            }
            
            doStep(step);
           
            currentStep += 1;
        }
    }

    /**
     * 
     */
    public void nextStep() {
        goToStep(this.actualStep + 1);
    }

    /**
     * 
     */
    public void setActualStepToLast() {
        this.actualStep = getStepCount() - 1;
    }

    /**
     * 
     */
    public void stepBack() {
        goToStep(this.actualStep - 1);
    }
}
