/**
 * 
 */
package ija.projekt.log;

import ija.projekt.basis.Round;
import ija.projekt.game.Game;
import ija.projekt.game.Game.StepStatus;
import ija.projekt.game.Player;
import ija.projekt.players.ComputerPlayer;
import ija.projekt.players.HumanPlayer;
import ija.projekt.players.NetworkPlayer.NetworkPlayer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 
 * @author David Molnar
 */
public class BasicNotationLog implements IFileLog {

    /**
     * 
     */
    public BasicNotationLog() {

    }

    /**
     * 
     * @param game
     * @return
     */
    public String getLog(Game game) {
        String result = "";

        if (game == null) {
            return result;
        }

        return parseRounds(game.getRounds());
    }

    /**
     * 
     */
    @Override
    public Game open(File file) {
        BufferedReader br = null;
        Game game = null;
        Player tmpPlayer = null;

        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(file));
            int flag = 0;

            while ((sCurrentLine = br.readLine()) != null) {
                // System.out.println(sCurrentLine);

                if (flag > 1) {
                    // steps
                    Round round = Round.parseRound(sCurrentLine, game);
                    StepStatus status = game.addRound(round); 
                            
                    if (status != StepStatus.OK) {
                        return null;
                    }
                } else {
                    // init
                    Player player = parsePlayer(sCurrentLine);

                    if (player == null) {
                        return null;
                    }

                    if (flag == 0) {
                        tmpPlayer = player;
                    } else if (flag == 1) {
                        if (tmpPlayer.isWhite() == player.isWhite()) {
                            return null;
                        }
                        game = new Game(tmpPlayer, player);
                    }

                    flag += 1;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
        }

        if (game.getPlayer1() instanceof NetworkPlayer
                || game.getPlayer2() instanceof NetworkPlayer) {
            game.setFinished(true);
        }

        game.setDirty(false);

        return game;
    }

    /**
     * 
     * @param game
     * @param input
     * @return
     */
    protected Game parseLog(Game game, String input) {
        if (game == null) {
            return null;
        }

        game.reset();

        String lines[] = input.split("\n");

        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }

            Round round = Round.parseRound(line, game);
            StepStatus s = game.addRound(round);

            if (s != StepStatus.OK) {
                return null;
            }
        }

        return game;
    }

    /**
     * 
     * @param player
     * @return
     */
    protected String parsePlayer(Player player) {
        if (player == null) {
            return "null";
        }

        return player.getClass().getSimpleName() + " "
                + (player.isWhite() ? "white" : "black");
    }

    
    /**
     * 
     * @param id
     * @return
     */
    protected Player parsePlayer(String id) {
        if (id == null) {
            return null;
        }

        String parts[] = id.split(" ");

        if (parts.length == 2) {
            String cl = parts[0];
            boolean white = parts[1].equals("white") ? true : false;

            if (cl.equals(HumanPlayer.class.getSimpleName())) {
                return new HumanPlayer(white);
            } else if (cl.equals(ComputerPlayer.class.getSimpleName())) {
                return new ComputerPlayer(white);
            } else if (cl.equals(NetworkPlayer.class.getSimpleName())) {
                return new NetworkPlayer(white);
            }
        }

        return null;
    }

    /**
     * 
     * @param rounds
     * @return
     */
    protected String parseRounds(List<Round> rounds) {
        String result = "";
        if (rounds == null) {
            return result;
        }

        for (Round round : rounds) {
            result += round.toString() + "\n";
        }

        return result;
    }

    /**
     * 
     */
    @Override
    public boolean save(File file, Game game) {
        BufferedWriter bw = null;

        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);

            bw.write(parsePlayer(game.getPlayer1()) + "\n");
            bw.write(parsePlayer(game.getPlayer2()) + "\n");

            List<Round> rounds = game.getRounds();

            bw.write(parseRounds(rounds));

        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
        }

        return true;
    }

    /**
     * 
     * @param game
     * @param input
     * @return
     */
    public Game setLog(Game game, String input) {
        if (parseLog(new Game(game), input) == null) {
            return null;
        }

        return parseLog(game, input);
    }
}
