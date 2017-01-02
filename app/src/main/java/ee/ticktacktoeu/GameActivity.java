package ee.ticktacktoeu;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

public class GameActivity extends AppCompatActivity {

    private final static String SEPARATOR_1 = "X";
    private final static String SEPARATOR_2 = "O";
    private Map<Coordinate, Button> mCoordinateButtonMap;
    private Map<Button, Coordinate> mButtonCoordinateMap;
    private TextView mScore1TextView;
    private TextView mScore2TextView;
    private TextView mTurnTextView;
    private int mPlayer1Counter;
    private int mPlayer2Counter;


    private Token player;
    private boolean multiplayer;
    private TickTackToeAI ai;
    private Board board;
    private TextView mWinTextView;
    private boolean isGameOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mPlayer1Counter = 0;
        mPlayer2Counter = 0;
        multiplayer = false;
        isGameOn = true;


        mScore1TextView = (TextView) findViewById(R.id.score1);
        mScore2TextView = (TextView) findViewById(R.id.score2);
        mTurnTextView = (TextView) findViewById(R.id.turn);
        mWinTextView = (TextView) findViewById(R.id.win_info);

        mButtonCoordinateMap = new HashMap<>();
        mButtonCoordinateMap.put((Button) findViewById(R.id.b0), new Coordinate(0, 0));
        mButtonCoordinateMap.put((Button) findViewById(R.id.b1), new Coordinate(0, 1));
        mButtonCoordinateMap.put((Button) findViewById(R.id.b2), new Coordinate(0, 2));
        mButtonCoordinateMap.put((Button) findViewById(R.id.b3), new Coordinate(1, 0));
        mButtonCoordinateMap.put((Button) findViewById(R.id.b4), new Coordinate(1, 1));
        mButtonCoordinateMap.put((Button) findViewById(R.id.b5), new Coordinate(1, 2));
        mButtonCoordinateMap.put((Button) findViewById(R.id.b6), new Coordinate(2, 0));
        mButtonCoordinateMap.put((Button) findViewById(R.id.b7), new Coordinate(2, 1));
        mButtonCoordinateMap.put((Button) findViewById(R.id.b8), new Coordinate(2, 2));


        mCoordinateButtonMap = new HashMap<>();
        mCoordinateButtonMap.put(new Coordinate(0, 0), (Button) findViewById(R.id.b0));
        mCoordinateButtonMap.put(new Coordinate(0, 1), (Button) findViewById(R.id.b1));
        mCoordinateButtonMap.put(new Coordinate(0, 2), (Button) findViewById(R.id.b2));
        mCoordinateButtonMap.put(new Coordinate(1, 0), (Button) findViewById(R.id.b3));
        mCoordinateButtonMap.put(new Coordinate(1, 1), (Button) findViewById(R.id.b4));
        mCoordinateButtonMap.put(new Coordinate(1, 2), (Button) findViewById(R.id.b5));
        mCoordinateButtonMap.put(new Coordinate(2, 0), (Button) findViewById(R.id.b6));
        mCoordinateButtonMap.put(new Coordinate(2, 1), (Button) findViewById(R.id.b7));
        mCoordinateButtonMap.put(new Coordinate(2, 2), (Button) findViewById(R.id.b8));


        Button mNewGameButton = (Button) findViewById(R.id.new_game);
        Button mMultiPlayerButton = (Button) findViewById(R.id.multi);

        mNewGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewGame();
            }
        });

        mMultiPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplayer = !multiplayer;
                mScore1TextView.setText(String.valueOf(0));
                mScore2TextView.setText(String.valueOf(0));
                startNewGame();
            }
        });


        mScore1TextView.setText(String.valueOf(mPlayer1Counter));
        mScore2TextView.setText(String.valueOf(mPlayer2Counter));
        mTurnTextView.setText(getSeparator(player));
        startNewGame();
    }

    private void startNewGame() {
        mWinTextView.setText("");
        isGameOn = true;
        player = Token.PLAYER_1;
        ai = new TickTackToeAI();
        board = new Board();
        for (final Button b : mButtonCoordinateMap.keySet()) {
            b.setEnabled(true);
            b.setTextColor(Color.LTGRAY);
            b.setText("");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play(b);
                }
            });
        }
        mTurnTextView.setText(getSeparator(player));
    }


    private boolean winControlOperations() {
        Boolean won = board.hasSomebodyWon();

        if (isGameOn && won) {
            for (Coordinate c : board.getWinningRow(player)) {
                mCoordinateButtonMap.get(c).setTextColor(Color.GREEN);
            }

            isGameOn = false;
            Token winner = board.getWinner();
            mWinTextView.setText(getSeparator(winner));
            switch (winner) {
                case PLAYER_1:
                    mPlayer1Counter++;
                    mScore1TextView.setText(String.valueOf(mPlayer1Counter));
                    break;
                case PLAYER_2:
                    mPlayer2Counter++;
                    mScore2TextView.setText(String.valueOf(mPlayer2Counter));
                    break;
            }
        }
        return won;
    }

    private String getSeparator(Token player) {
        if(player == Token.FREE)return "-";
        return player == Token.PLAYER_1 ? SEPARATOR_1 : SEPARATOR_2;
    }

    private void switchPlayer() {
        player = player == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1;
    }

    private void play(Button button) {
        if (winControlOperations()) return;

        button.setEnabled(false);
        button.setTextColor(Color.BLACK);
        button.setText(getSeparator(player));
        board.play(player, mButtonCoordinateMap.get(button));

        if (winControlOperations()) return;
        switchPlayer();

        if (!multiplayer) {
            Coordinate aiMove = ai.getBestMove(board, player);
            Button b = mCoordinateButtonMap.get(aiMove);
            b.setEnabled(false);
            b.setText(getSeparator(player));
            b.setTextColor(Color.BLACK);
            board.play(player, aiMove);


            if (winControlOperations())
                return;
            switchPlayer();

        } else {
            mTurnTextView.setText(getSeparator(player));
        }
    }
}
