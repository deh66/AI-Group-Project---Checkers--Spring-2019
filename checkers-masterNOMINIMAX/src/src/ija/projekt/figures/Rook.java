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
public class Rook extends Figure {

    /**
     * 
     * @param p
     * @param white
     */
    public Rook(Position p, boolean white) {
        super(p, white);
    }

    /**
     * 
     * @param newRook
     * @param p
     */
    public Rook(Rook newRook, Position p) {
        this(p, newRook.white);
    }

    /**
     * 
     */
    @Override
    public Step canMove(Position p) {
        if (p == null || p.equals(this.p)) {
            return null;
        }

        int co[][] = { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };

        boolean found = false;

        for (int j = 0; j < co.length; j++) {
            Position x = null;

            for (int i = 1; i < 8; i++) {
                Position pos = this.p.nextPosition(co[j][0] * i, co[j][1] * i);

                if (pos == null) {
                    break;
                }

                if (pos.getFigure() != null) {
                    if (x != null) {
                        break;
                    }

                    if (pos.getFigure().isWhite() == this.isWhite()) {
                        break;
                    } else {
                        x = pos;
                        continue;
                    }
                }

                if (x != null) {
                    if (pos.equals(p)) {
                        return new Step(this.p, p, x);
                    } else {
                        found = true;
                    }
                }
            }
        }

        if (found) {
            return null;
        }

        for (int j = 0; j < co.length; j++) {
            boolean flag = false;

            for (int i = 1; i < 8; i++) {
                Position pos = this.p.nextPosition(co[j][0] * i, co[j][1] * i);

                if (pos == null || (pos != null && pos.getFigure() != null)) {
                    flag = true;
                }

                if (!flag && pos.equals(p)) {
                    return new Step(this.p, p);
                }
            }
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
        int r2 = r / 3;

        g.setColor(this.white ? (this.p.isSelected() ? Color.lightGray
                : Color.white) : (this.p.isSelected() ? Color.gray
                : Color.black));
        g.fillOval(x + (w - r) / 2, y + (h - r) / 2, r, r);

        g.setColor(Position.BROWN_COLOR);
        g.fillOval(x + (w - r2) / 2, y + (h - r2) / 2, r2, r2);

        // g.setColor(Color.red);
        // g.drawString("R", x + w/2 - 5,
        // y + h/2 + 5);
    }

    /**
     * 
     */
    @Override
    public List<Step> getSteps() {
        List<Step> steps = new ArrayList<Step>();
        int dim = this.p.getDesk().getDimension();

        int co[][] = { { 1, 1 }, { -1, -1 }, { 1, -1 }, { -1, 1 } };

        for (int j = 0; j < co.length; j++) {
            for (int i = 1; i < dim; i++) {
                Position pos = this.p.nextPosition(co[j][0] * i, co[j][1] * i);

                Step step = this.canMove(pos);

                if (step != null) {
                    steps.add(step);
                }
            }
        }

        return steps;
    }
}
