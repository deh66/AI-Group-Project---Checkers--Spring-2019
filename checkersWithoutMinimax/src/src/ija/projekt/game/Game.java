/**
 * 
 */
package ija.projekt.game;

import ija.projekt.basis.Desk;
import ija.projekt.basis.Figure;
import ija.projekt.basis.Position;
import ija.projekt.basis.Round;
import ija.projekt.basis.Step;
import ija.projekt.figures.Pawn;
import ija.projekt.figures.Rook;
import ija.projekt.gui.GuiUpdate.GuiUpdate;
import ija.projekt.log.IFileLog;
import ija.projekt.players.ComputerPlayer;
import ija.projekt.players.HumanPlayer;
import ija.projekt.players.NetworkPlayer.NetworkPlayer;

import java.io.File;
import java.util.List;

/**
 * @author  David Molnar
 */
public class Game extends GuiUpdate {

    /**
	 * 
	 */
    public enum StepStatus {
        /**
         * 
		 */
        CANT_MOVE, 
        /**
         * 
		 */
        MUST_MOVE, 
        /**
         * 
		 */
        NO_FIGURE, 
        /**
         * 
		 */
        NOT_CURRENT_PLAYER, 
        /**
         * 
		 */
        NOT_DEFINED, 
        /**
         * 
		 */
        OK, 
        /**
         * 
		 */
        POS_FIG_NULL
    }

    /**
     * 
     */
    protected static final int DIMENSION = 8;

    /**
     * 
     * @param f
     * @param file
     * @return
     */
    public static Game open(File f, IFileLog file) {
        return file.open(f);
    }

    /**
     * 
	 */
    protected Player currentPlayer;

    /**
     * 
	 */
    protected Desk desk;

    /**
     * 
     */
    protected boolean dirtyFlag;

    /**
     * 
	 */
    protected IFileLog file;
    
    /**
     * 
	 */
    protected boolean finished;
    
    /**
     * 
	 */
    protected StepStatus lastStepStatus;

    /**
     * 
	 */
    protected Log log;

    /**
     * 
	 */
    
    protected Player player1;
    
    /**
     * 
	 */
    protected Player player2;
    
    /**
     * 
     * @param newGame
     */
    public Game(Game newGame) {
        this(null, null);
        this.player1 = createNewPlayer(newGame.player1);
        this.player2 = createNewPlayer(newGame.player2);

        this.finished = newGame.finished;
        this.currentPlayer = newGame.currentPlayer;
        this.dirtyFlag = newGame.dirtyFlag;
        this.file = newGame.file;
        this.lastStepStatus = newGame.lastStepStatus;

        this.desk = new Desk(newGame.desk);
        this.log = new Log(newGame.log, this);
    }

    /**
     * 
     * @param player1
     * @param player2
     */
    public Game(Player player1, Player player2) {
        this.dirtyFlag = true;
        this.finished = false;

        this.desk = createDesk();
        this.log = new Log();

        this.player1 = player1;
        if (this.player1 != null) {
        	this.player1.setGame(this);
        }
        
        this.player2 = player2;
        if (this.player2 != null) {
        	this.player2.setGame(this);
        }
        
        setCurrentPlayer();
    }

    /**
     * 
     * @param round
     * @return
     */
    public StepStatus addRound(Round round) {  
        if (round == null) {
            return StepStatus.POS_FIG_NULL;
        }

        Round lastRound = getLastRound();

        if (lastRound == null && round.getN() != 0) {
            return StepStatus.POS_FIG_NULL;
        }

        if (lastRound != null && lastRound.getN() + 1 != round.getN()) {
            return StepStatus.POS_FIG_NULL;
        }

        Step step1 = round.getStep1();
        Step step2 = round.getStep2();

        if (step1 == null) {
            return StepStatus.POS_FIG_NULL;
        }
        
        StepStatus s = addStep(step1);
        
        if (s != StepStatus.OK){
            return s;
        }
        
        if (step2 == null) {
            return s;
        }
        
        s = addStep(step2);
        
        return s;
    }        
    
    /**
     * 
     * @param step
     * @return
     */
    public StepStatus addStep(Step step) {
        if (this.currentPlayer == null) {
            return StepStatus.NOT_CURRENT_PLAYER;
        }
        
        StepStatus status = canDoStep(step, this.currentPlayer);
        
        if (status == StepStatus.OK) {
            doStep(step);
            
            this.log.add(step);
            
            switchPlayer();
        }
        
        this.lastStepStatus = status;
        return status;
    }
    
    /**
     * 
     * @param step
     * @param player
     * @return
     */
    public StepStatus canDoStep(Step step, Player player) {
        if (player == null || step == null) {
            return StepStatus.POS_FIG_NULL;
        }
        
        Position from = step.getFrom();
        Position to = step.getTo();
        
        if (from == null || to == null) {
            return StepStatus.POS_FIG_NULL;
        }
        
        Figure f1 = from.getFigure();
        
        if (f1 == null) {
            return StepStatus.POS_FIG_NULL;
        }
        
        if (f1.isWhite() != player.isWhite()) {
            return StepStatus.NOT_CURRENT_PLAYER;
        }
        
        Step newStep = f1.canMove(to);
        if (newStep == null) {
        }
        
        if (newStep != null) {
            step.setX(newStep.getX());
        }
        
        Position x = step.getX();
        
        if (x == null && isDuty(player)) {
            return StepStatus.MUST_MOVE;
        }
        
        char sep = step.getReadedSeparator();
        
        if (x == null && sep != 0 && sep != Step.POSITION_SEPARATOR) {
            return StepStatus.MUST_MOVE;
        }
        
        if (x != null && sep != 0 && sep != Step.POSITION_X_SEPARATOR) {
            return StepStatus.MUST_MOVE;
        }

        if (newStep != null) {
            return StepStatus.OK;
        }
        
        return StepStatus.CANT_MOVE;
    }
    
    /**
     * 
     * @param pos
     * @return
     */
    protected Rook changePawnToRook(Position pos) {
        Rook rook = null;
        
        if (pos == null) {
            return rook;
        }
        
        Figure f1 = pos.getFigure();
        
        if (f1 == null) {
            return rook;
        }
        
        rook = new Rook(pos, f1.isWhite());
        pos.putFigure(rook);
        
        return rook;
    }
    
    /**
     * 
     */
    public void checkRook() {
        Position pos = isNewRook(true);
        
        if (pos != null) {
            changePawnToRook(pos);
        }
        
        pos = isNewRook(false);
        
        if (pos != null) {
            changePawnToRook(pos);
        }
    }
    
    /**
     * 
     * @return
     */
    protected Desk createDesk() {
        int dim = Game.DIMENSION;
        Desk desk = new Desk(dim);

        createFigures(desk);

        return desk;
    }
    
    /**
     * 
     * @param desk
     */
    protected void createFigures(Desk desk) {
        int dim = desk.getDimension();
        for (char c = Desk.START_LETTER; c <= desk.getEndLetter(); c++) {
            for (int r = 1; r <= dim; r++) {
                Position p = desk.getPositionAt(c, r);

                if (p.isBlack()) {
                    if (r <= 3) {
                        Pawn pawn = new Pawn(p, true);
                        p.putFigure(pawn);
                    } else if (r > (dim - 3)) {
                        Pawn pawn = new Pawn(p, false);
                        p.putFigure(pawn);
                    } else {
                        p.removeFigure();
                    }
                }
            }
        }
    }
    
    /**
     * 
     * @param player
     * @return
     */
    protected Player createNewPlayer(Player player) {
        if (player instanceof HumanPlayer) {
            return new HumanPlayer((HumanPlayer) player);
        } else if (player instanceof ComputerPlayer) {
            return new ComputerPlayer((ComputerPlayer) player);
        } else if (player instanceof NetworkPlayer) {
            return new NetworkPlayer((NetworkPlayer) player);
        }
        return player;
    }
    
    /**
     * 
     */
    public void dispose() {
        if (this.player1 != null) {
            this.player1.dispose();
        }

        if (this.player2 != null) {
            this.player2.dispose();
        }
    }

    /**
     * 
     * @param from
     * @param to
     * @return
     */
    public StepStatus doMove(Position from, Position to) {
        if (from == null || to == null) {
            return StepStatus.POS_FIG_NULL;
        }
        
        StepStatus status = addStep(new Step(from, to));
        
        if (this.currentPlayer != null) {
            this.currentPlayer.yourTurn();
        }
        
        this.finished = isFinished();

        
        
        refreshGui();
        return status;
    }

    /**
     * 
     * @param step
     * @return
     */
    protected Figure doStep(Step step) {
        if (step == null) {
            return null;
        }
        
        Position from = step.getFrom();
        Position to = step.getTo();
        
        if (from == null || to == null) {
            return null;
        }

        Figure f1 = from.getFigure();
        
        if (f1 == null) {
            return null;
        }
        
        f1.move(to);

        Position remove = step.getX();

        if (remove != null) {
            remove.getFigure().removePosition();
            remove.removeFigure();
        }
        
        checkRook();
        
        return f1;
    }

    /**
     * 
	 * @return
	 */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * 
     * @return
     */
    public int getDeskDimension() {
        return desk.getDimension();
    }

    /**
     * 
     * @return
     */
    public char getEndLetter() {
        return this.desk.getEndLetter();
    }

    /**
     * 
     * @return
     */
    public boolean getFinished() {
        return this.finished;
    }

    /**
     * 
     * @return
     */
    public Round getLastRound() {
        if (this.log != null) {
            return this.log.getLastRound();
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public Step getLastStep() {
        if (this.log != null) {
            return this.log.getLastStep();
        }

        return null;
    }

    /**
     * 
	 * @return
	 */
    public StepStatus getLastStepStatus() {
        return this.lastStepStatus;
    }

    /**
     * 
	 * @return
	 */
    public Player getPlayer1() {
        return this.player1;
    }

    /**
     * 
	 * @return
	 */
    public Player getPlayer2() {
        return this.player2;
    }

    /**
     * 
     * @param c
     * @param r
     * @return
     */
    public Position getPositionAt(char c, int r) {
        return this.desk.getPositionAt(c, r);
    }

    /**
     * 
     * @param p
     * @return
     */
    public Position getPositionAt(Position p) {
        if (p == null) {
            return null;
        }

        char c = p.getImagCol();
        int r = p.getImagRow();
        return getPositionAt(c, r);
    }

    /**
     * 
     * @return
     */
    public List<Round> getRounds() {
        if (this.log != null) {
            return this.log.getLog();
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public int getStepCount() {
        int sum = 0;

        if (this.log != null) {
            List<Round> rounds = this.log.getLog();
            for (Round round : rounds) {
                sum += round.getStepCount();
            }
        }

        return sum;
    }

    /**
     * 
     * @return
     */
    public List<Step> getSteps() {
        if (this.log != null) {
            return this.log.getSteps();
        }
        return null;
    }

    /**
     * 
     * @return
     */
    public Player getWinner() {
        if (this.finished) {
            return this.currentPlayer == this.player1 ? this.player2
                    : this.player1;
        }

        return null;
    }

    /**
     * 
     * @return
     */
    public boolean isDirty() {
        return this.dirtyFlag;
    }

    /**
     * 
     * @param player
     * @return
     */
    private boolean isDuty(Player player) {
        for (char c = Desk.START_LETTER; c <= this.getEndLetter(); c++) {
            for (int r = 1; r <= this.desk.getDimension(); r++) {
                Position p = this.getPositionAt(c, r);
                Figure f = p.getFigure();

                if (f != null && f.isWhite() == player.isWhite()) {
                    List<Step> steps = f.getSteps();

                    if (steps == null) {
                        continue;
                    }

                    for (Step step : steps) {
                        if (step.getX() != null) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * 
	 * @return
	 */
    private boolean isFinished() {
        int count = 0;
        int steps = 0;

        for (char c = Desk.START_LETTER; c <= this.getEndLetter(); c++) {
            for (int r = 1; r <= this.desk.getDimension(); r++) {
                Position p = this.getPositionAt(c, r);
                Figure f = p.getFigure();

                if (f != null && f.isWhite() == this.currentPlayer.isWhite()) {
                    count += 1;
                    steps += f.getSteps().size();
                }
            }
        }

        return count == 0 || steps == 0;
    }

    /**
     * 
     * @param white
     * @return
     */
    public Position isNewRook(boolean white) {
        for (char c = Desk.START_LETTER; c <= this.getEndLetter(); c++) {
            int row = white ? this.desk.getDimension() : 1;
            Position p = this.getPositionAt(c, row);

            if (p != null && p.isBlack()) {
                Figure f = p.getFigure();

                if (f != null && f.isWhite() == white
                        && f.getClass() != Rook.class) {
                    return p;
                }
            }
        }

        return null;
    }

    /**
     * 
     */
    public void reset() {
        this.log.clear();
        this.desk = createDesk();
        setCurrentPlayer();
    }

    /**
     * 
     * @param f
     * @return
     */
    public boolean save(File f) {
        boolean result = file.save(f, this);
        this.dirtyFlag = !result;
        return result;
    }

    /**
     * 
     */
    public void setCurrentPlayer() {
        if (this.player1 != null || this.player2 != null) {
            this.currentPlayer = this.player1.isWhite() ? this.player1 : this.player2;
        }
    }

    /**
     * 
     * @param dirty
     */
    public void setDirty(boolean dirty) {
        this.dirtyFlag = dirty;
    }

    /**
     * 
     * @param file
     */
    public void setFileType(IFileLog file) {
        this.file = file;
    }

    /**
     * 
	 * @param finished
	 */
    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    /**
     * 
     */
    public void start() {
        if (this.currentPlayer != null) {
            this.currentPlayer.yourTurn();
        }

        refreshGui();
    }
    
    /**
     * 
     */
    public void switchPlayer() {
        this.currentPlayer = (this.currentPlayer == this.player1) ? this.player2
                : this.player1;
    }

    /**
     * 
     */
    @Override
    public String toString() {
        String result = "Game";
        if (this.player1 != null && this.player2 != null) {
            result += ": " + this.player1.toString() + " - "
                    + this.player2.toString();
        }

        /*
         * Round round = this.getLastRound(); int n = 0;
         * 
         * if (round != null) { n = round.getN() + 1; }
         */

        result += " [" + getStepCount() + "]";

        return result;
    }
}
