package com.example.wordstack;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import java.util.Stack;

public class StackedLayout extends LinearLayout {

    private Stack<View> tiles = new Stack();

    public StackedLayout(Context context) {
        super(context);
    }

    public void push(View tile) {
        if(!tiles.empty())
        {
            LetterTile t=(LetterTile) tiles.peek();
            this.removeView(t);
        }

        tiles.push(tile);
        this.addView(tile);



    }

    public View pop() {

        LetterTile popped=(LetterTile) tiles.pop();
        this.removeView(popped);
        LetterTile tt=(LetterTile) tiles.peek();
        this.addView(tt);
        return popped;
    }

    public View peek() {
        return tiles.peek();
    }

    public boolean empty() {
        return tiles.empty();
    }

    public void clear() {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }
}
