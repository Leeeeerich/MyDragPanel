package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements MyDragPanel.OnClickInDragListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyDragPanel myDragPanel = findViewById(R.id.mdp);
        myDragPanel.setOnClickDragListener(this);
    }

    @Override
    public void onClickByButton(int i) {
        Log.d(getLocalClassName(), "Button pressed = " + i);
    }
}
