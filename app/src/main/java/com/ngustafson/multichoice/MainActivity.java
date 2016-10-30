package com.ngustafson.multichoice;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.ngustafson.multichoice.db.DatabaseHandler;
import com.ngustafson.multichoice.maint.Quiz;

import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DatabaseHandler db = new DatabaseHandler(this);

        Log.d("Insert: ", "Inserting ..");
        db.addQuiz(new Quiz("quiz1"));
        db.addQuiz(new Quiz("quiz2"));

        List<Quiz> quizList = db.getAllQuizzes();

        for (Quiz quiz : quizList) {
            Log.d("Name:", Integer.toString(quiz.getId()));
        }

    }

}
