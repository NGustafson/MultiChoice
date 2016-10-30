package com.ngustafson.multichoice;

import android.support.v4.content.CursorLoader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class QuizListActivity extends AppCompatActivity {

    SimpleCursorAdapter qAdapter;

    CursorLoader temp = new CursorLoader()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list);
    }
}
