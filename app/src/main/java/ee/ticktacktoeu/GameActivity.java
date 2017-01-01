package ee.ticktacktoeu;

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
    private Map<Button, Coordinate> mButtonCoordinateMap;
    private Button mNewGameButton;
    private Button mMultiPlayerButton;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mPlayer1Counter = 0;
        mPlayer2Counter = 0;
        multiplayer = false;


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


        mNewGameButton = (Button) findViewById(R.id.new_game);
        mMultiPlayerButton = (Button) findViewById(R.id.multi);

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
                mScore1TextView.setText(Integer.toString(0));
                mScore2TextView.setText(Integer.toString(0));
                startNewGame();
            }
        });


        mScore1TextView.setText(Integer.toString(mPlayer1Counter));
        mScore2TextView.setText(Integer.toString(mPlayer2Counter));
        mTurnTextView.setText(Token.PLAYER_1.name());
        startNewGame();
    }

    private void startNewGame() {
        player = Token.PLAYER_1;
        ai = new TickTackToeAI();
        board = new Board();
        for (final Button b : mButtonCoordinateMap.keySet()) {
            b.setEnabled(true);
            b.setText("");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    play(b);
                }
            });
        }
        mTurnTextView.setText(player.name());
    }


    private void play(Button button) {
        if (board.hasSomebodyWon()) {
            return;
        }

        button.setEnabled(false);
        button.setText(player == Token.PLAYER_1 ? SEPARATOR_1 : SEPARATOR_2);
        board.play(player, mButtonCoordinateMap.get(button));

        if (board.hasSomebodyWon()) {
            mWinTextView.setText(player.name());
            return;
        }


        if (!multiplayer) {
            Coordinate aiMove = ai.getBestMove(board, Token.PLAYER_2);


            for (Button b : mButtonCoordinateMap.keySet()) {
                if (mButtonCoordinateMap.get(b).equals(aiMove)) {
                    b.setEnabled(false);
                    b.setText(SEPARATOR_2);
                    board.play(Token.PLAYER_2, aiMove);
                    if (board.hasSomebodyWon()) {
                        mWinTextView.setText(Token.PLAYER_2.name());
                    }
                    break;
                }
            }
        } else {
            player = player == Token.PLAYER_1 ? Token.PLAYER_2 : Token.PLAYER_1;
            mTurnTextView.setText(player.name());
        }
    }
}
