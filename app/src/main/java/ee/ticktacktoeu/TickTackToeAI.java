package ee.ticktacktoeu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TickTackToeAI {

    private final Random random;

    public TickTackToeAI() {
        random = new Random();
    }


    public Coordinate getBestMove(Board board, Token player) {
        List<Coordinate> moves;

        //1. If the player has two in a row, they can place a third to get three in a row.
        moves = getNextWin(player, board);
        if (!moves.isEmpty()) return moves.get(random.nextInt(moves.size()));

        //Block: If the opponent has two in a row, the player must play the third themselves to block the opponent.
        moves = getBlockMoves(player, board);
        if (!moves.isEmpty()) return moves.get(random.nextInt(moves.size()));

        //Fork: Create an opportunity where the player has two threats to win (two non-blocked lines of 2)
        moves = getForkMoves(player, board);
        if (!moves.isEmpty()) return moves.get(random.nextInt(moves.size()));

        //Fork: Block opponent's fork:
        moves = getForkMoves(player == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1, board);
        if (!moves.isEmpty()) return moves.get(random.nextInt(moves.size()));

        //Center
        moves = board.getAvailableMoves();
        if (moves.contains(new Coordinate(1, 1))) return new Coordinate(1, 1);

        //Opposite corner: If the opponent is in the corner, the player plays the opposite corner.
        moves = getCornerMoves(player, board);
        if (!moves.isEmpty()) return moves.get(random.nextInt(moves.size()));

        //Empty corner: The player plays in a corner square.
        moves = getEmptyCorners(board);
        if (!moves.isEmpty()) return moves.get(random.nextInt(moves.size()));

        //Empty side: The player plays in a middle square on any of the 4 sides.
        moves = getEmptySide(board);
        if (!moves.isEmpty()) return moves.get(random.nextInt(moves.size()));

        moves = board.getAvailableMoves();
        return moves.get(random.nextInt(moves.size()));
    }


    private List<Coordinate> getEmptySide(Board board) {
        List<Coordinate> out = new ArrayList<>();
        Coordinate side1 = new Coordinate(0, 1);
        Coordinate side2 = new Coordinate(1, 0);
        Coordinate side3 = new Coordinate(2, 1);
        Coordinate side4 = new Coordinate(1, 2);


        List<Coordinate> available = board.getAvailableMoves();

        if (available.contains(side1))
            out.add(side1);

        if (available.contains(side2))
            out.add(side2);

        if (available.contains(side3))
            out.add(side3);

        if (available.contains(side4))
            out.add(side4);
        return out;
    }

    private List<Coordinate> getEmptyCorners(Board board) {
        Coordinate corner1 = new Coordinate(0, 0);
        Coordinate corner2 = new Coordinate(0, 2);
        Coordinate corner3 = new Coordinate(2, 0);
        Coordinate corner4 = new Coordinate(2, 2);

        List<Coordinate> out = new ArrayList<>();
        List<Coordinate> available = board.getAvailableMoves();

        if (available.contains(corner1))
            out.add(corner1);

        if (available.contains(corner2))
            out.add(corner2);

        if (available.contains(corner3))
            out.add(corner3);

        if (available.contains(corner4))
            out.add(corner4);
        return out;
    }

    private List<Coordinate> getCornerMoves(Token player, Board board) {
        List<Coordinate> available = board.getAvailableMoves();
        List<Coordinate> out = new ArrayList<>();
        Token enemy = player == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1;

        List<Coordinate> enemyTokens = board.getCoordinatesByPlayer(enemy);

        Coordinate corner1 = new Coordinate(0, 0);
        Coordinate corner2 = new Coordinate(0, 2);
        Coordinate corner3 = new Coordinate(2, 0);
        Coordinate corner4 = new Coordinate(2, 2);

        if (enemyTokens.contains(corner1) && available.contains(corner4))
            out.add(corner4);

        if (enemyTokens.contains(corner4) && available.contains(corner1))
            out.add(corner1);

        if (enemyTokens.contains(corner2) && available.contains(corner3))
            out.add(corner3);

        if (enemyTokens.contains(corner3) && available.contains(corner2))
            out.add(corner2);

        return out;
    }

    private List<Coordinate> getForkMoves(Token player, Board board) {
        List<Coordinate> out = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            List<Token> row = board.getRow(i);
            List<Token> col = board.getCol(i);

            if (Collections.frequency(row, Token.FREE) == 2 && row.contains(player))
                out.add(new Coordinate(i, row.indexOf(Token.FREE)));

            if (Collections.frequency(col, Token.FREE) == 2 && col.contains(player))
                out.add(new Coordinate(col.indexOf(Token.FREE), i));
        }

        List<Token> diag1 = board.getDiag(1);
        if (Collections.frequency(diag1, Token.FREE) == 2 && diag1.contains(player)) {
            int index = diag1.indexOf(Token.FREE);
            out.add(new Coordinate(index, index));
        }

        List<Token> diag2 = board.getDiag(1);
        if (Collections.frequency(diag2, Token.FREE) == 2 && diag2.contains(player)) {
            int pos = diag2.indexOf(Token.FREE);
            if (pos == 1)
                out.add(new Coordinate(1, 1));
            else if (pos == 0)
                out.add(new Coordinate(2, 0));
            else
                out.add(new Coordinate(0, 2));
        }
        return out;
    }


    private List<Coordinate> getBlockMoves(Token player, Board board) {
        Token enemy = player == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1;
        return getNextWin(enemy, board);
    }


    private List<Coordinate> getNextWin(Token player, Board board) {
        List<Coordinate> out = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            List<Token> row = board.getRow(i);
            List<Token> col = board.getCol(i);

            if (Collections.frequency(row, player) == 2 && row.contains(Token.FREE))
                out.add(new Coordinate(i, row.indexOf(Token.FREE)));

            if (Collections.frequency(col, player) == 2 && col.contains(Token.FREE))
                out.add(new Coordinate(col.indexOf(Token.FREE), i));
        }

        List<Token> diag1 = board.getDiag(1);
        if (Collections.frequency(diag1, player) == 2 && diag1.contains(Token.FREE)) {
            int index = diag1.indexOf(Token.FREE);
            out.add(new Coordinate(index, index));
        }

        List<Token> diag2 = board.getDiag(1);
        if (Collections.frequency(diag2, player) == 2 && diag2.contains(Token.FREE)) {
            int pos = diag2.indexOf(Token.FREE);
            if (pos == 1)
                out.add(new Coordinate(1, 1));
            else if (pos == 0)
                out.add(new Coordinate(2, 0));
            else
                out.add(new Coordinate(0, 2));
        }
        return out;
    }


}
