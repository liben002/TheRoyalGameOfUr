package com.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.ur.Location;
import com.ur.Piece;
import com.ur.Board;
import java.lang.Math;
import com.example.ec327.R;
import com.ur.Location;

public class GameScreenActivity extends Activity {

    private Button rollButton;
    private Button passButton;

    Location[] squareLocations;
    ImageView[] piecesImageViews;
    Location[] pieces;
    Board board;

    int diceRoll = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_game_screen);


        // setup buttons
        rollButton = findViewById(R.id.rollButton);
        passButton = findViewById(R.id.passButton);

        // initialize squares and pieces
        squareLocations = new Location[20];
        piecesImageViews = new ImageView[14];
        pieces = new Location[14];
        board = new Board(squareLocations, pieces);

        // setup all the squares and pieces
        setup(squareLocations, piecesImageViews, pieces);


        // game loop
        // click roll, choose piece, piece moves, next turn
        rollButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // weighted dice roll 0-4
                for (int i = 0; i < 5; i++) {
                    int individualRoll = (int) (Math.random() * 2);
                    diceRoll += individualRoll;
                }

                Context context = getApplicationContext();
                CharSequence text = "Roll: " + diceRoll;
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                rollButton.setEnabled(false);
            }
        });
    }

    public void setup(Location[] squareLocations, ImageView[] piecesImageViews, Location[] pieces) {
        for (int i = 0; i < 20; i++) {
            //TODO get actual locations
            squareLocations[i] = new Location();
            squareLocations[i].setX(i);
            squareLocations[i].setY(i);
        }

        int piecesImageViewsIndex = 0;

        // iterate to find all the ImageViews, give them an index
        for (int i = 1; i < 3; i++) {
            for (int j = 1; j < 8; j++) {
                String imageViewID = "piece" + j + "_player" + i;
                int resID = getResources().getIdentifier(imageViewID, "id", getPackageName());
                piecesImageViews[piecesImageViewsIndex] = findViewById(resID);
                piecesImageViews[piecesImageViewsIndex].setTag(piecesImageViewsIndex);
                piecesImageViewsIndex++;
            }
        }

        // set initial location of pieces
        for (int i = 0; i < 14; i++) {
            pieces[i] = new Location();
            pieces[i].setX(piecesImageViews[i].getLeft());
            pieces[i].setY(piecesImageViews[i].getTop());
        }
    }
    // when piece is clicked, if it's allowed to, it moves
    public void buttonClicked(View view) {
        if (!rollButton.isEnabled()) {
            int pieceIndex = (int) view.getTag();
            if (board.getTurn() == 1 && pieceIndex < 7) {
                board.updateBoardState(pieceIndex, diceRoll);
                Location p1Loc = board.getPieceScreenLoc(pieceIndex);
                piecesImageViews[pieceIndex].setX(p1Loc.getX());
                piecesImageViews[pieceIndex].setY(p1Loc.getY());
                rollButton.setEnabled(true);
            } else if (board.getTurn() == 2 && pieceIndex >= 7) {
                board.updateBoardState(pieceIndex, diceRoll);
                Location p1Loc = board.getPieceScreenLoc(pieceIndex);
                piecesImageViews[pieceIndex].setX(p1Loc.getX());
                piecesImageViews[pieceIndex].setY(p1Loc.getY());
                rollButton.setEnabled(true);
            }
        }
        diceRoll = 0;
    }
}

