/**
 * 
 */
package ija.projekt.players;

import ija.projekt.basis.Desk;
import ija.projekt.basis.Figure;
import ija.projekt.basis.Position;
import ija.projekt.basis.Step;
import ija.projekt.game.Game;
import ija.projekt.game.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author David Molnar
 */
public class ComputerPlayer extends Player {

	int count = 5;
    /**
     * 
     * @param white
     */
    public ComputerPlayer(boolean white) {
        super(white);
        this.name = "Computer";
    }

    /**
     * 
     * @param newPlayer
     */
    public ComputerPlayer(ComputerPlayer newPlayer) {
        super(newPlayer);
        this.name = newPlayer.name;
    }

    /**
     * 
     * @param game
     * @return
     */
    private Step calculateRandomStep(List<Step> steps, Game game, int num) 
    {    	
    	while (num > 0)
    	{
        	Step step = null;
    		steps = getAllStep(game);
        	int size = steps.size();

        	steps.add(calculateRandomStep(steps, game, num - 1));
        
        	if (size > 0) 
        	{
            	int randomNum = 0 + (int) (Math.random() * (size - 1));
            	step = steps.get(randomNum);
        	}
        	System.out.println(step);
        	System.out.println(steps.size());
        	game.doMove(step.getFrom(), step.getTo());
            return step;
    	}

		return null;
    }

    /**
     * 
     */
    @Override
    public void dispose() {

    }

    /**
     * 
     * @param game
     * @return
     */
    public List<Step> getAllStep(Game game) {
        List<Step> s = new ArrayList<Step>();

        boolean flag = false;

        for (char c = Desk.START_LETTER; c <= game.getEndLetter(); c++) {
            for (int r = 1; r <= game.getDeskDimension(); r++) {
                Position p = game.getPositionAt(c, r);
                Figure f = p.getFigure();

                if (f != null && f.isWhite() == this.white) {
                    List<Step> steps = f.getSteps();

                    if (steps == null) {
                        continue;
                    }

                    for (Step step : steps) {
                        if (!flag && step.getX() != null) {
                            s.clear();
                            flag = true;
                        }

                        if (!flag || (flag && step.getX() != null)) {
                            s.add(step);

                        }
                    }
                }
            }
        }

        return s;
    }

    /**
     * 
     */
    @Override
    public void yourTurn() {
        Runnable r = new Runnable() {
            @Override
            public void run() 
            {
            	List<Step> stepsSend = null;
                calculateRandomStep(stepsSend, game, count);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                
               // doMove(step);
            }
        };

        new Thread(r).start();
    }
}
