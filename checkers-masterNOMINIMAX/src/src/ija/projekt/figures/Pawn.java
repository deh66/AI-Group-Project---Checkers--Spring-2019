/**
 * 
 */
package ija.projekt.figures;

import ija.projekt.basis.Figure;
import ija.projekt.basis.Position;
import ija.projekt.basis.Step;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * @author David Molnar
 * 
 */
public class Pawn extends Figure {

    /**
     * 
     */
    protected int way;

    /**
     * 
     * @param newPawn
     * @param p
     */
    public Pawn(Pawn newPawn, Position p) {
        this(p, newPawn.white);
    }

    /**
     * 
     * @param p
     * @param white
     */
    public Pawn(Position p, boolean white) {
        super(p, white);
        this.way = this.white ? 1 : -1;
    }

    /**
     * 
     */
    @Override
    public Step canMove(Position p) {
        if (p == null || p.equals(this.p)) {
            return null;
        }

        Position right1 = this.p.nextPosition(1, this.way);
        Position left1 = this.p.nextPosition(-1, this.way);
        Position right2 = this.p.nextPosition(2, 2 * this.way);
        Position left2 = this.p.nextPosition(-2, 2 * this.way);

        Position pos[][] = { { right1, right2 }, { left1, left2 } };
        boolean flag = false;

        for (int i = 0; i < pos.length; i++) {
            Position p1 = pos[i][0];
            Position p2 = pos[i][1];

            if (p2 != null && p2.getFigure() == null && p1 != null
                    && p1.getFigure() != null
                    && p1.getFigure().isWhite() != this.isWhite()) {
                if (p2.equals(p)) {
                    return new Step(this.p, p, p1);
                } else {
                    flag = true;
                }
            }
        }

        if (flag) {
            return null;
        }

        if (((right1 != null && right1.equals(p)) || (left1 != null && left1
                .equals(p))) && (p.getFigure() == null)) {
            return new Step(this.p, p);
        }

        return null;
    }

    /**
     * 
     */
    @Override
    public void draw(Graphics g, int x0, int y0, int w, int h) {
        int x = x0 + this.p.getRealCol() * w;
        int y = y0 + this.p.getRealRow() * h;
        int r = w < h ? w : h;
        r -= r / 3;

        g.setColor(this.white ? (this.p.isSelected() ? Color.lightGray
                : Color.white) : (this.p.isSelected() ? Color.darkGray
                : Color.black));
        g.fillOval(x + (w - r) / 2, y + (h - r) / 2, r, r);

        // g.setColor(Color.red);
        // g.drawString(this.pos.getPos(), x, y+15);
    }

    /**
     * 
     */
    @Override
    public List<Step> getSteps() {
        List<Step> steps = new ArrayList<Step>();

        int incr = this.white ? 1 : -1;
        int co[][] = { { 1, incr }, { -1, incr }, { 2, 2 * incr },
                { -2, 2 * incr } };

        for (int i = 0; i < co.length; i++) {
            Position pos = this.p.nextPosition(co[i][0], co[i][1]);

            Step step = this.canMove(pos);

            if (step != null) {
                steps.add(step);
            }
        }

        return steps;
    }
}
