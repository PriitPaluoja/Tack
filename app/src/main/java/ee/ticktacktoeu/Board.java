package ee.ticktacktoeu;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {

    private Map<Coordinate, Token> board;
    private boolean hasWon = false;
    private Token winner = Token.FREE;

    public Board() {
        board = new HashMap<>();
        board.put(new Coordinate(0, 0), Token.FREE);
        board.put(new Coordinate(0, 1), Token.FREE);
        board.put(new Coordinate(0, 2), Token.FREE);
        board.put(new Coordinate(1, 0), Token.FREE);
        board.put(new Coordinate(1, 1), Token.FREE);
        board.put(new Coordinate(1, 2), Token.FREE);
        board.put(new Coordinate(2, 0), Token.FREE);
        board.put(new Coordinate(2, 1), Token.FREE);
        board.put(new Coordinate(2, 2), Token.FREE);
    }

    public void play(Token player, Coordinate coordinate) {
        board.put(coordinate, player);

        if (hasPlayerWon(player)) {
            winner = player;
            hasWon = true;
        }
    }

    public boolean hasSomebodyWon() {
        return hasWon;
    }


    private boolean hasPlayerWon(Token player) {
        return Collections.frequency(getRow(0), player) == 3 ||
                Collections.frequency(getRow(1), player) == 3 ||
                Collections.frequency(getRow(2), player) == 3 ||
                Collections.frequency(getCol(0), player) == 3 ||
                Collections.frequency(getCol(1), player) == 3 ||
                Collections.frequency(getCol(2), player) == 3 ||
                (board.get(new Coordinate(0, 0)) == player && board.get(new Coordinate(1, 1)) == player && board.get(new Coordinate(2, 2)) == player) ||
                (board.get(new Coordinate(2, 0)) == player && board.get(new Coordinate(1, 1)) == player && board.get(new Coordinate(0, 2)) == player);
    }

    public String getWinnerText() {
        return winner.name();
    }

    public List<Token> getRow(int rowNr) {
        return Arrays.asList(board.get(new Coordinate(rowNr, 0)),
                board.get(new Coordinate(rowNr, 1)),
                board.get(new Coordinate(rowNr, 2))
        );
    }

    public List<Token> getCol(int colNr) {
        return Arrays.asList(board.get(new Coordinate(0, colNr)),
                board.get(new Coordinate(1, colNr)),
                board.get(new Coordinate(2, colNr))
        );
    }
}
