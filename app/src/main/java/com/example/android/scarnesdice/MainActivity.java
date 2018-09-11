package com.example.android.scarnesdice;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.TooManyListenersException;

public class MainActivity extends AppCompatActivity {

    private static final String SCORE = "Your score: %s Computer score: %s",
            TURN_PLAYER_SCORE = "Your score: %s Computer score: %s Your turn score: %s",
            TURN_COMPUTER_SCORE = "Your score: %s Computer score: %s Computer turn score: %s",
            ROLLS_ONE = "Your score: %s Computer score: %s %s rolled a one",
            HOLDS = "Your score: %s Computer score: %s %s hold %s";
    private final ArrayList<Drawable> drawables = new ArrayList<>();
    private int totalPlayerScore = 0, totalComputerScore = 0,
            turnPlayerScore = 0, turnComputerScore = 0;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Create an array of the dice images
        drawables.add(getResources().getDrawable(R.drawable.dice1));
        drawables.add(getResources().getDrawable(R.drawable.dice2));
        drawables.add(getResources().getDrawable(R.drawable.dice3));
        drawables.add(getResources().getDrawable(R.drawable.dice4));
        drawables.add(getResources().getDrawable(R.drawable.dice5));
        drawables.add(getResources().getDrawable(R.drawable.dice6));

        Button buttonRoll = findViewById(R.id.roll);
        buttonRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int diceNo = rollDice();
                if (diceNo == 1) {
                    turnPlayerScore = 0;
                    controlButtons(false);
                    RollsOne("Player");
                    updateScore();
                    computerTurn();
                } else {
                    turnPlayerScore += diceNo;
                    updatePlayerTurnScore();
                }
            }
        });

        Button buttonHold = findViewById(R.id.hold);
        buttonHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Hold Click ----------", "onClick: Hello");
                controlButtons(false);
                Log.d("Hola", "------------------ Hola");
                Holds("Player");
                Log.d("Hola", "------------Hola");
                updateScore();
                Log.d("Turn: ", "Computers turn----------------");
                computerTurn();
            }
        });

        Button buttonReset = findViewById(R.id.reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalPlayerScore = 0;
                totalComputerScore = 0;
                turnPlayerScore = 0;
                turnComputerScore = 0;
                updateScore();
            }
        });
    }

    private int rollDice() {
        Random random = new Random();
        int diceNo = random.nextInt(6);

        //Change the image on the imageView
        ImageView imageView = findViewById(R.id.dice_image);
        imageView.setImageDrawable(drawables.get(diceNo));

        return diceNo + 1;
    }

    private void updatePlayerTurnScore() {
        TextView textView = findViewById(R.id.score_text);
        textView.setText(String.format(TURN_PLAYER_SCORE, totalPlayerScore, totalComputerScore, turnPlayerScore));
        handler.postDelayed(runnable, 1000);
    }

    private void updateComputerTurnScore() {
        TextView textView = findViewById(R.id.score_text);
        textView.setText(String.format(TURN_COMPUTER_SCORE, totalPlayerScore, totalComputerScore, turnComputerScore));
        //halt();
    }

    private void updateScore() {
        totalPlayerScore += turnPlayerScore;
        totalComputerScore += turnComputerScore;
        turnPlayerScore = 0;
        turnComputerScore = 0;

        TextView textView = findViewById(R.id.score_text);
        textView.setText(String.format(SCORE, totalPlayerScore, totalComputerScore));
        handler.postDelayed(runnable, 1000);
        //halt();
    }

    private void RollsOne(String player) {
        TextView textView = findViewById(R.id.score_text);
        textView.setText(String.format(ROLLS_ONE, totalPlayerScore, totalComputerScore, player));
        Toast.makeText(this, player + " rolled a one", Toast.LENGTH_SHORT).show();
        //halt();
    }

    private void Holds(String player) {
        int turnScore = player.equals("Player") ? turnPlayerScore : turnComputerScore;

        TextView textView = findViewById(R.id.score_text);
        textView.setText(String.format(HOLDS, totalPlayerScore, totalComputerScore, player, turnScore));
        Toast.makeText(this, player + " halted", Toast.LENGTH_SHORT).show();
        //halt();
    }

    private void controlButtons(boolean bool) {
        //bool = false;
        //Disable/Enable the ROLL button
        Button buttonRoll = findViewById(R.id.roll);
        buttonRoll.setEnabled(bool);

        //Disable/Enable the HOLD button
        Button buttonHold = findViewById(R.id.hold);
        buttonHold.setEnabled(bool);
    }

    private void endComputerTurn() {
        updateScore();
        controlButtons(true);
    }

    private void computerTurn() {
        turnComputerScore = 0;
        while(turnComputerScore < 20) {
            int diceNo = rollDice();
            if (diceNo == 1) {
                turnComputerScore = 0;
                RollsOne("Computer");
                break;
            } else {
                turnComputerScore += diceNo;
                updateComputerTurnScore();
            }
        }

        if(turnComputerScore >= 20)
            Holds("Computer");

        endComputerTurn();
    }
}
