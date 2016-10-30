package com.ngustafson.multichoice.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;


/**
 * Created by Nick on 10/20/2016.
 */

public class QuizCursorAdapter extends CursorAdapter {
    public QuizCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
    }
}
