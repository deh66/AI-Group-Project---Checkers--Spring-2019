/**
 * 
 */
package ija.projekt.log;

import ija.projekt.basis.Position;
import ija.projekt.basis.Round;
import ija.projekt.basis.Step;
import ija.projekt.game.Game;
import ija.projekt.game.Game.StepStatus;
import ija.projekt.game.Player;
import ija.projekt.players.ComputerPlayer;
import ija.projekt.players.HumanPlayer;
import ija.projekt.players.NetworkPlayer.NetworkPlayer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author David Molnar
 */
public class XMLLog implements IFileLog {

    /**
     * 
     */
    public XMLLog() {

    }

    /**
     * 
     * @param game
     * @return
     */
    protected Document createDocument(Game game) {
        if (game == null) {
            return null;
        }

        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("checkers");

        Element players = root.addElement("players");
        createPlayerElement(players, "player1", game.getPlayer1());
        createPlayerElement(players, "player2", game.getPlayer2());

        Element roundsElement = root.addElement("rounds");

        List<Round> rounds = game.getRounds();

        for (Round round : rounds) {
            Element roundElement = roundsElement.addElement("round")
                    .addAttribute("n", String.valueOf(round.getN()));

            createStepElement(roundElement, round.getStep1());
            createStepElement(roundElement, round.getStep2());
        }

        return document;
    }

    /**
     * 
     * @param root
     * @param name
     * @param player
     * @return
     */
    protected Element createPlayerElement(Element root, String name,
            Player player) {
        if (player == null) {
            return null;
        }

        return root.addElement(name)
                .addAttribute("type", player.getClass().getSimpleName())
                .addAttribute("color", player.isWhite() ? "white" : "black");
    }

    /**
     * 
     * @param root
     * @param step
     * @return
     */
    protected Element createStepElement(Element root, Step step) {
        if (root == null || step == null) {
            return null;
        }
        Element stepElement = root.addElement("step");

        Position from = step.getFrom();
        Position to = step.getTo();

        if (from == null || to == null) {
            return null;
        }

        stepElement.addElement("from")
                .addAttribute("column", Character.toString(from.getImagCol()))
                .addAttribute("row", String.valueOf(from.getImagRow()));
        stepElement.addElement("to")
                .addAttribute("column", Character.toString(to.getImagCol()))
                .addAttribute("row", String.valueOf(to.getImagRow()));

        return stepElement;
    }

    /**
     * 
     */
    @Override
    public Game open(File file) {
        Document document = null;

        try {
            document = parseDocument(file);
        } catch (DocumentException ex) {
            ex.printStackTrace();
            return null;
        }

        Game game = null;
        Player player1 = null;
        Player player2 = null;

        Element root = document.getRootElement();

        for (Iterator i = root.elementIterator(); i.hasNext();) {
            Element element = (Element)i.next();

            if (element.getName().equals("players")) {
                for (Iterator j = element.elementIterator(); j.hasNext();) {
                    Element playerElement = (Element)j.next();
                    String playerElementName = playerElement.getName();

                    if (playerElementName.equals("player1")
                            || playerElementName.equals("player2")) {
                        Player newPlayer = parsePlayer(playerElement);

                        if (newPlayer == null) {
                            return null;
                        }

                        if (playerElementName.equals("player1")) {
                            player1 = newPlayer;
                        } else {
                            player2 = newPlayer;
                        }
                    }
                }

            } else if (element.getName().equals("rounds")) {
                if (player1.isWhite() == player2.isWhite()) {
                    return null;
                }
                
                game = new Game(player1, player2);

                for (Iterator j = element.elementIterator(); j.hasNext();) {
                    Element roundElement = (Element)j.next();

                    if (roundElement.getName().equals("round")) {
                        Round round = parseRound(roundElement, game);
                        StepStatus status = game.addRound(round);
                        
                        if (status != StepStatus.OK) {
                            return null;
                        }
                    }
                }
            }
        }

        if (game.getPlayer1() instanceof NetworkPlayer
                || game.getPlayer2() instanceof NetworkPlayer) {
            game.setFinished(true);
        }

        return game;
    }

    /**
     * 
     * @param file
     * @return
     * @throws DocumentException
     */
    protected Document parseDocument(File file) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(file);
        return document;
    }

    /**
     * 
     * @param playerElement
     * @return
     */
    protected Player parsePlayer(Element playerElement) {
        String className = "";
        boolean white = true;

        for (Iterator i = playerElement.attributeIterator(); i.hasNext();) {
            Attribute attr = (Attribute)i.next();
            String name = attr.getName();

            if (name.equals("type")) {
                className = attr.getValue();
            } else if (name.equals("color")) {
                white = attr.getValue().equals("white") ? true : false;
            }
        }

        Player newPlayer = null;

        if (className.equals(HumanPlayer.class.getSimpleName())) {
            newPlayer = new HumanPlayer(white);
        } else if (className.equals(ComputerPlayer.class.getSimpleName())) {
            newPlayer = new ComputerPlayer(white);
        } else if (className.equals(NetworkPlayer.class.getSimpleName())) {
            newPlayer = new NetworkPlayer(white);
        }

        return newPlayer;
    }

    /**
     * 
     * @param roundElement
     * @param game
     * @return
     */
    protected Round parseRound(Element roundElement, Game game) {
        int n = -1;

        for (Iterator i = roundElement.attributeIterator(); i.hasNext();) {
            Attribute attr = (Attribute)i.next();

            if (attr.getName().equals("n")) {
                try {
                    n = Integer.parseInt(attr.getValue());
                } catch (NumberFormatException ex) {
                    return null;
                }
            }
        }

        Step step1 = null, step2 = null;

        for (Iterator j = roundElement.elementIterator(); j.hasNext();) {
            Element stepElement = (Element)j.next();

            if (stepElement.getName().equals("step")) {
                Step step = parseStep(stepElement, game);

                if (step1 == null) {
                    step1 = step;
                } else {
                    step2 = step;
                }
            }
        }

        Round round = new Round(n);
        round.setStep1(step1);
        round.setStep2(step2);

        return round;
    }

    /**
     * 
     * @param stepElement
     * @param game
     * @return
     */
    protected Step parseStep(Element stepElement, Game game) {
        Position from = null, to = null;

        for (Iterator m = stepElement.elementIterator(); m.hasNext();) {
            Element posElement = (Element)m.next();
            String name = posElement.getName();

            if (name.equals("from") || name.equals("to")) {
                char c = 'a';
                int r = -1;

                for (Iterator o = posElement.attributeIterator(); o.hasNext();) {
                    Attribute attr = (Attribute)o.next();
                    String attrName = attr.getName();

                    if (attrName.equals("column")) {
                        c = attr.getValue().charAt(0);
                    } else if (attrName.equals("row")) {
                        try {
                            r = Integer.parseInt(attr.getValue());
                        } catch (NumberFormatException ex) {
                            return null;
                        }
                    }
                }

                Position newPos = new Position(null, c, r, true);

                if (name.equals("from")) {
                    from = newPos;
                } else {
                    to = newPos;
                }
            }
        }

        return new Step(game.getPositionAt(from), game.getPositionAt(to));
    }

    /**
     * 
     */
    @Override
    public boolean save(File file, Game game) {
        Document document = createDocument(game);
        FileWriter out = null;

        try {
            out = new FileWriter(file);
            document.write(out);
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        return true;
    }
}
