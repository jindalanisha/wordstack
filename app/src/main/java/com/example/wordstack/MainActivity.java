package com.example.wordstack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private static final int WORD_LENGTH = 5;
    public static final int LIGHT_BLUE = Color.rgb(176, 200, 255);
    public static final int LIGHT_GREEN = Color.rgb(200, 255, 200);
    private ArrayList<String> words = new ArrayList<>();
    private Random random = new Random();
    private StackedLayout stackedLayout;
    private String word1, word2;
    private String scrambled="";
    Stack<View> placedtiles = new Stack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AssetManager assetManager = getAssets();
        try {
            InputStream inputStream = assetManager.open("words.txt");
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while((line = in.readLine()) != null) {
                String word = line.trim();
                if(word.length()==WORD_LENGTH)
                    words.add(word);
            }
        } catch (IOException e) {
            Toast toast = Toast.makeText(this, "Could not load dictionary", Toast.LENGTH_LONG);
            toast.show();
        }
        LinearLayout verticalLayout = (LinearLayout) findViewById(R.id.vertical_layout);
        stackedLayout = new StackedLayout(this);
        verticalLayout.addView(stackedLayout, 3);

        View word1LinearLayout = findViewById(R.id.word1);
       // word1LinearLayout.setOnTouchListener(new TouchListener());
        word1LinearLayout.setOnDragListener(new DragListener());
        View word2LinearLayout = findViewById(R.id.word2);
       // word2LinearLayout.setOnTouchListener(new TouchListener());
        word2LinearLayout.setOnDragListener(new DragListener());


    }

//    private class TouchListener implements View.OnTouchListener {
//
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if (event.getAction() == MotionEvent.ACTION_DOWN && !stackedLayout.empty()) {
//                LetterTile tile = (LetterTile) stackedLayout.peek();
//                tile.moveToViewGroup((ViewGroup) v);
//                placedtiles.push(tile);
//                if (stackedLayout.empty()) {
//                    TextView messageBox = (TextView) findViewById(R.id.message_box);
//                    messageBox.setText(word1 + " " + word2);
//                }
//
//
//            }
//            return false;
//        }
//    }

    private class DragListener implements View.OnDragListener {

        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(LIGHT_GREEN);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(LIGHT_BLUE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundColor(Color.WHITE);
                    v.invalidate();
                    return true;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign Tile to the target Layout
                    LetterTile tile = (LetterTile) event.getLocalState();
                    tile.moveToViewGroup((ViewGroup) v);
                    placedtiles.push(tile);
                    if (stackedLayout.empty()) {
                        TextView messageBox = (TextView) findViewById(R.id.message_box);
                        messageBox.setText(word1 + " " + word2);
                    }
                    /**
                     **
                     **  YOUR CODE GOES HERE
                     **
                     **/
                    return true;
            }
            return false;
        }
    }

    public boolean onStartGame(View view) {

        LinearLayout word1LinearLayout = (LinearLayout) findViewById(R.id.word1);
        word1LinearLayout.removeAllViews();
        LinearLayout word2LinearLayout = (LinearLayout) findViewById(R.id.word2);
        word2LinearLayout.removeAllViews();
        stackedLayout.removeAllViews();
        TextView messageBox = (TextView) findViewById(R.id.message_box);
        messageBox.setText("Game started");
        scrambled="";
        int rand_int1 = random.nextInt(words.size()/2);
        int rand_int2 = random.nextInt(words.size()/2) + words.size()/2 -2;
        word1=words.get(rand_int1);
        word2=words.get(rand_int2);
        int i=0,j=0;
        while(i<word1.length() || j<word2.length()) {
            int choice = random.nextInt(2);
            if (choice == 0 && i < word1.length()) {
                scrambled += word1.charAt(i);
                i++;
            } else if (choice == 1 && j < word2.length()) {
                scrambled += word2.charAt(j);
                j++;
            } else if (choice == 0 && i==word1.length()) {
                while (j < word2.length()) {
                    scrambled += word2.charAt(j);
                    j++;
                }
            } else {
                while (i < word1.length()) {
                    scrambled += word1.charAt(i);
                    i++;
                }
            }
        }
            messageBox.setText(scrambled);
            Log.e("y",word1);
            Log.e("y",word2);
            Log.e("y",scrambled);


            for(int k=scrambled.length()-1 ; k>=0 ; k--)
            {
                LetterTile letter=new LetterTile(this,scrambled.charAt(k));
                stackedLayout.push(letter);
            }
        return true;
    }

    public boolean onUndo(View view)
    {
         LetterTile tile=(LetterTile)placedtiles.pop();
         tile.moveToViewGroup(stackedLayout);
         return true;
    }
}
