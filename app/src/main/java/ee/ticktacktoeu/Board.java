package ee.ticktacktoeu;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Board implements Serializable {

    private Map<Coordinate, Token> board;
    private boolean hasWon = false;
    private Token winner = Token.FREE;
    private int movesMade;

    Board() {
        movesMade = 0;
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

    public Board copy() {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(this);
            oos.flush();
            oos.close();
            bos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            return (Board) new ObjectInputStream(bais).readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new Board();
        }
    }

    List<Coordinate> getAvailableMoves() {
        return getCoordinatesByPlayer(Token.FREE);
    }


    void play(Token player, Coordinate coordinate) {
        if (board.get(coordinate) != Token.FREE) throw new IllegalArgumentException();
        board.put(coordinate, player);
        if (!getWinningRow(player).isEmpty()) {
            winner = player;
            hasWon = true;
        }
        movesMade++;
        if (movesMade == 9)
            hasWon = true;
    }

    boolean hasSomebodyWon() {
        return hasWon;
    }

    Token getWinner() {
        return winner;
    }

    List<Coordinate> getWinningRow(Token player) {
        List<Coordinate> winningRow = new ArrayList<>();

        if (Collections.frequency(getRow(0), player) == 3)
            winningRow = getRowAsCoordinates(0);

        if (Collections.frequency(getRow(1), player) == 3)
            winningRow = getRowAsCoordinates(1);

        if (Collections.frequency(getRow(2), player) == 3)
            winningRow = getRowAsCoordinates(2);

        if (Collections.frequency(getCol(0), player) == 3)
            winningRow = getColAsCoordinates(0);

        if (Collections.frequency(getCol(1), player) == 3)
            winningRow = getColAsCoordinates(1);

        if (Collections.frequency(getCol(2), player) == 3)
            winningRow = getColAsCoordinates(2);

        if (board.get(new Coordinate(0, 0)) == player && board.get(new Coordinate(1, 1)) == player && board.get(new Coordinate(2, 2)) == player)
            winningRow = Arrays.asList(
                    new Coordinate(0, 0),
                    new Coordinate(1, 1),
                    new Coordinate(2, 2)
            );


        if (board.get(new Coordinate(2, 0)) == player && board.get(new Coordinate(1, 1)) == player && board.get(new Coordinate(0, 2)) == player)
            winningRow = Arrays.asList(
                    new Coordinate(2, 0),
                    new Coordinate(1, 1),
                    new Coordinate(0, 2)
            );


        return winningRow;
    }


    List<Token> getRow(int rowNr) {
        return Arrays.asList(
                board.get(new Coordinate(rowNr, 0)),
                board.get(new Coordinate(rowNr, 1)),
                board.get(new Coordinate(rowNr, 2))
        );
    }

    private List<Coordinate> getRowAsCoordinates(int rowNr) {
        return Arrays.asList(
                new Coordinate(rowNr, 0),
                new Coordinate(rowNr, 1),
                new Coordinate(rowNr, 2)
        );
    }

    private List<Coordinate> getColAsCoordinates(int colNr) {
        return Arrays.asList(
                new Coordinate(0, colNr),
                new Coordinate(1, colNr),
                new Coordinate(2, colNr)
        );
    }


    List<Token> getCol(int colNr) {
        return Arrays.asList(
                board.get(new Coordinate(0, colNr)),
                board.get(new Coordinate(1, colNr)),
                board.get(new Coordinate(2, colNr))
        );
    }

    List<Coordinate> getCoordinatesByPlayer(Token player) {
        List<Coordinate> out = new ArrayList<>();
        for (Coordinate c : board.keySet())
            if (board.get(c) == player) out.add(c);
        return out;
    }


    List<Token> getDiag(int nr) {
        if (nr == 1)
            return Arrays.asList(board.get(new Coordinate(0, 0)), board.get(new Coordinate(1, 1)), board.get(new Coordinate(2, 2)));
        else if (nr == 2)
            return Arrays.asList(board.get(new Coordinate(2, 0)), board.get(new Coordinate(1, 1)), board.get(new Coordinate(0, 2)));
        else throw new RuntimeException();
    }
}
