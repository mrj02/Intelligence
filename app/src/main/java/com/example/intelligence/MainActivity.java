package com.example.intelligence;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final int YEllOW_CODE = 0;
    public static final int RED_CODE = 1;
    public static final int NOT_PLAYED = 2;
    private static final int NO_WINNER = -1;
    int winner = NO_WINNER;

    int [] gameState = {NOT_PLAYED, NOT_PLAYED, NOT_PLAYED,
            NOT_PLAYED, NOT_PLAYED, NOT_PLAYED,
            NOT_PLAYED, NOT_PLAYED, NOT_PLAYED};
    int activePlayer = RED_CODE;
    int [][] winningPositions = {{0,1,2}, {3,4,5}, {6,7,8},
            {0,3,6}, {1,4,7}, {2,5,8},
            {0,4,8}, {2,4,6}};
    RelativeLayout msgLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msgLayout = findViewById(R.id.msg_layout);
        msgLayout.setVisibility(View.GONE);
        //دکمه Back
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    public void dropIn(View view) {
        int tag = Integer.parseInt((String) view.getTag());
        if (winner != NO_WINNER || gameState[tag] != NOT_PLAYED){
            return;
        }
        ImageView img = (ImageView) view;
        img.setTranslationY(-1000f);

        if (activePlayer == RED_CODE){
            img.setImageResource(R.drawable.red);
            img.animate().translationY(0f).setDuration(500);
            gameState[tag] = RED_CODE;
            activePlayer = YEllOW_CODE;
        } else if (activePlayer == YEllOW_CODE){
            img.setImageResource(R.drawable.yellow);
            img.animate().translationY(0f).setDuration(500);
            gameState[tag] = YEllOW_CODE;
            activePlayer = RED_CODE;
        }
        //check winner
        winnerMsg();
    }

    public void winnerMsg() {
        winner = checkWinner();
        if (winner != NO_WINNER || filled()){
            String msg = "";
            int color = Color.GRAY;
            if (winner == NO_WINNER){
                msg = "NO Winner";
            }else if (winner == RED_CODE){
                msg = "Red Player Won";
                color = Color.RED;
            }else if (winner == YEllOW_CODE){
                msg = "Yellow Player Won";
                color = Color.YELLOW;
            }
            msgLayout.setBackgroundColor(color);
            ((TextView) msgLayout.findViewById(R.id.winner_message)).setText(msg);
            msgLayout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    // no winner = -1
    // red = RED_CODE
    // yellow = YELLOW_CODE
    public int checkWinner(){
        for (int[] position : winningPositions){
            if (gameState[position[0]] == gameState[position[1]] &&
                    gameState[position[1]] == gameState[position[2]] &&
                    gameState[position[0]] != NOT_PLAYED){
                return gameState[position[0]];
            }
        }
        return NO_WINNER;
    }

    public boolean filled(){
        for (int i=0; i< gameState.length; i++){
            if (gameState[i] == NOT_PLAYED){
                return false;
            }
        }
        return true;
    }

    public void reset(View v){
        //active player
        activePlayer = RED_CODE;
        //winner
        winner = NO_WINNER;
        //game state
        for (int i = 0; i< gameState.length; i++){
            gameState[i] = NOT_PLAYED;
        }
        //play ground
        LinearLayout pgLayout = findViewById(R.id.pg_layout);
        for (int i=0; i< pgLayout.getChildCount(); i++){
            //Toast.makeText(this, "pgLayout , ChildCount : " + pgLayout.getChildCount() , Toast.LENGTH_SHORT).show();
            LinearLayout row = (pgLayout.getChildAt(i) instanceof LinearLayout) ?
                    (LinearLayout) pgLayout.getChildAt(i) : null;
            if (row == null) return;
            for (int j=0; j< row.getChildCount(); j++){
                ImageView iv = (row.getChildAt(j) instanceof ImageView) ?
                        (ImageView) row.getChildAt(j) : null;
                if (iv == null) return;
                iv.setImageResource(0);

            }
        }
        msgLayout.setVisibility(View.GONE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem menuReset = menu.add("reset");
        menuReset.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menuReset.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                reset(null);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}