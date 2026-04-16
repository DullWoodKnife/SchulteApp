package com.example.schulteapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
 import android.widget.GridLayout;
 import android.widget.Toast;
 import androidx.appcompat.app.AppCompatActivity;
 import java.util.ArrayList;
 import java.util.Collections;
 import java.util.List;

 public class MainActivity extends AppCompatActivity {

     private static final int GRID_SIZE = 5;
     private static final int NUM_TILES = GRID_SIZE * GRID_SIZE;

     private Button[] tiles = new Button[NUM_TILES];
     private int[] numbers = new int[NUM_TILES];
     private int currentNumber = 1;
     private Chronometer chronometer;
     private long startTime;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         GridLayout gridLayout = findViewById(R.id.gridLayout);
         chronometer = findViewById(R.id.chronometer);

         // Initialize the tiles array from the GridLayout
         for (int i = 0; i < NUM_TILES; i++) {
             tiles[i] = (Button) gridLayout.getChildAt(i);
             final int index = i;
             tiles[i].setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     handleTileClick(index);
                 }
             });
         }

         // Start button
         findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startGame();
             }
         });
     }

     private void startGame() {
         // Reset game state
         currentNumber = 1;
         startTime = SystemClock.elapsedRealtime();
         chronometer.setBase(startTime);
         chronometer.start();

         // Generate and shuffle numbers 1 to 25
         List<Integer> numberList = new ArrayList<>();
         for (int i = 1; i <= NUM_TILES; i++) {
             numberList.add(i);
         }
         Collections.shuffle(numberList);
         for (int i = 0; i < NUM_TILES; i++) {
             numbers[i] = numberList.get(i);
             tiles[i].setText(String.valueOf(numbers[i]));
             tiles[i].setEnabled(true);
             tiles[i].setBackgroundColor(Color.WHITE);
         }
     }

     private void handleTileClick(int index) {
         if (numbers[index] == currentNumber) {
             // Correct tile
             tiles[index].setEnabled(false);
             tiles[index].setBackgroundColor(Color.GREEN);
             currentNumber++;

             if (currentNumber > NUM_TILES) {
                 // Game finished
                 chronometer.stop();
                 long elapsedMillis = SystemClock.elapsedRealtime() - startTime;
                 double elapsedSeconds = elapsedMillis / 1000.0;
                 Toast.makeText(this, "完成! 用时: " + String.format("%.2f", elapsedSeconds) + " 秒", Toast.LENGTH_LONG).show();
             }
         } else {
             // Wrong tile
             tiles[index].setBackgroundColor(Color.RED);
             // Optionally, you can reset the color after a short delay
             new android.os.Handler().postDelayed(
                 new Runnable() {
                     public void run() {
                         tiles[index].setBackgroundColor(Color.WHITE);
                     }
                 }, 500);
         }
     }
 }
