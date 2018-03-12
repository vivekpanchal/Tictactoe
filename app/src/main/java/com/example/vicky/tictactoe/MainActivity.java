package com.example.vicky.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //0=zero and 1=cross
    int activePlayer=0;

    boolean gameIsActive = true;
    //2 means unplayed
    int[] gameState={2,2,2,2,2,2,2,2,2};



    int[][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

    public void dropin(View view) {
        ImageView icon = (ImageView) view;
        System.out.println(icon.getTag().toString());

        int tapedIcon = Integer.parseInt(icon.getTag().toString());

        if (gameState[tapedIcon] == 2 && gameIsActive) {

            gameState[tapedIcon] = activePlayer;

            icon.setTranslationY(-1000f);

            if (activePlayer == 1) {
                icon.setImageResource(R.drawable.cross);
                activePlayer = 0;
            } else {
                icon.setImageResource(R.drawable.zero);
                activePlayer = 1;
            }


            icon.animate().translationYBy(1000f).setDuration(300);


            for (int[] winningPosition : winningPositions) {

                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {
                    //someone has won the game

                    gameIsActive = false;
                    String winner = "cross";

                    if (gameState[winningPosition[0]] == 0) {

                        winner = "zero";

                    }

                    TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                    winnerMessage.setText(winner + " has won!");
                    LinearLayout layout = (LinearLayout) findViewById(R.id.playAgainLayout);
                    layout.setVisibility(View.VISIBLE);


                } else {

                    boolean gameIsOver = true;

                    for (int counterState : gameState) {

                        if (counterState == 2) gameIsOver = false;

                    }

                    if (gameIsOver) {

                        TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);

                        winnerMessage.setText("It's a draw");

                        LinearLayout layout = (LinearLayout) findViewById(R.id.playAgainLayout);

                        layout.setVisibility(View.VISIBLE);

                    }


                }
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public  void playAgain(View view){
        gameIsActive = true;
        LinearLayout layout = (LinearLayout)findViewById(R.id.playAgainLayout);

        layout.setVisibility(View.INVISIBLE);

        activePlayer = 0;

        for (int i = 0; i < gameState.length; i++) {

            gameState[i] = 2;

        }

        GridLayout gridLayout = (GridLayout)findViewById(R.id.gridLayout);

        for (int i = 0; i< gridLayout.getChildCount(); i++) {

            ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);

        }

    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
