package com.ngustafson.multichoice.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ngustafson.multichoice.maint.Answer;
import com.ngustafson.multichoice.maint.Question;
import com.ngustafson.multichoice.maint.Quiz;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick on 10/2/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper{

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "quizDB";

    // Quiz table name
    private static final String TABLE_QUIZZES = "quizzes";

    // Quiz table column names
    private static final String QUIZ_ID = "id";
    private static final String QUIZ_NAME = "quiz_name";

    // Question table name
    private static final String TABLE_QUESTIONS = "questions";

    // Question table column names
    private static final String QUESTION_ID = "id";
    private static final String QUESTION_QUIZ_ID = "quiz_id";
    private static final String QUESTION_NAME = "question_name";

    // Question table constraint name
    private static final String FK_QUIZ_ID = "fk_quiz_id";

    // Answer table name
    private static final String TABLE_ANSWERS = "answers";

    // Answer table constraint name
    private static final String FK_QUESTION_ID = "fk_question_id";

    // Answer table column names
    private static final String ANSWER_ID = "id";
    private static final String ANSWER_QUESTION_ID = "question_id";
    private static final String ANSWER_TEXT = "answer_text";
    private static final String ANSWER_CORRECT = "correct";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_QUIZ_TABLE = "CREATE TABLE " + TABLE_QUIZZES + "("
                + QUIZ_ID + " INTEGER PRIMARY KEY," + QUIZ_NAME + " TEXT)";

        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + TABLE_QUESTIONS + "("
                + QUESTION_ID + " INTEGER PRIMARY KEY," + QUESTION_QUIZ_ID + "INTEGER "
                + "CONSTRAINT " + FK_QUIZ_ID + " REFERENCES " + TABLE_QUIZZES + "("
                + QUIZ_ID + ") ON DELETE CASCADE," + QUESTION_NAME + " TEXT)";

        String CREATE_ANSWERS_TABLE = "CREATE TABLE " + TABLE_ANSWERS + "("
                + ANSWER_ID + " INTEGER PRIMARY KEY," + ANSWER_QUESTION_ID
                + "INTEGER CONSTRAINT " + FK_QUESTION_ID + " REFERENCES "
                + TABLE_QUESTIONS + "(" + QUESTION_ID + ") ON DELETE CASCADE,"
                + ANSWER_TEXT + " TEXT," + ANSWER_CORRECT + " INTEGER)";

        String CREATE_QUESTION_QUIZ_TRIGGERS =
                // Insert
                "CREATE TRIGGER fki_question_quiz_id "
                + "BEFORE INSERT ON " + TABLE_QUESTIONS + " FOR EACH ROW BEGIN "
                + "SELECT RAISE(ROLLBACK, 'insert on table \"" + TABLE_QUESTIONS
                + "\" violates foreign key constraint \"" + FK_QUIZ_ID
                + "\"') WHERE (SELECT " + QUIZ_ID + " FROM " + TABLE_QUIZZES
                + " WHERE " + QUIZ_ID + " = NEW." + QUESTION_QUIZ_ID + ") IS NULL; END;"
                // Update
                + "CREATE TRIGGER fku_question_quiz_id "
                + "BEFORE UPDATE ON " + TABLE_QUESTIONS + " FOR EACH ROW BEGIN "
                + "SELECT RAISE(ROLLBACK, 'update on table \"" + TABLE_QUESTIONS
                + "\" violates foreign key constraint \"" + FK_QUIZ_ID
                + "\"') WHERE NEW." + QUESTION_QUIZ_ID + " IS NOT NULL AND (SELECT "
                + QUIZ_ID + " FROM " + TABLE_QUIZZES + " WHERE " + QUIZ_ID
                + " = NEW." + QUESTION_QUIZ_ID + ") IS NULL; END;"
                // Delete cascade
                + "CREATE TRIGGER fkd_question_quiz_id "
                + "BEFORE DELETE ON " + TABLE_QUIZZES + " FOR EACH ROW BEGIN "
                + "DELETE FROM " + TABLE_QUESTIONS + " WHERE " + QUESTION_QUIZ_ID
                + " = OLD." + QUIZ_ID + "; END;";

        String CREATE_ANSWER_QUESTION_TRIGGERS =
                // Insert
                "CREATE TRIGGER fki_answer_question_id "
                + "BEFORE INSERT ON " + TABLE_ANSWERS + " FOR EACH ROW BEGIN "
                + "SELECT RAISE(ROLLBACK, 'insert on table \"" + TABLE_ANSWERS
                + "\" violates foreign key constraint \"" + FK_QUESTION_ID
                + "\"') WHERE (SELECT " + QUESTION_ID + " FROM " + TABLE_QUESTIONS
                + " WHERE " + QUESTION_ID + " = NEW." + ANSWER_QUESTION_ID + ") IS NULL; END;"
                // Update
                + "CREATE TRIGGER fku_answer_question_id "
                + "BEFORE UPDATE ON " + TABLE_ANSWERS + " FOR EACH ROW BEGIN "
                + "SELECT RAISE(ROLLBACK, 'update on table \"" + TABLE_ANSWERS
                + "\" violates foreign key constraint \"" + FK_QUESTION_ID
                + "\"') WHERE NEW." + ANSWER_QUESTION_ID + " IS NOT NULL AND (SELECT "
                + QUESTION_ID + " FROM " + TABLE_QUESTIONS + " WHERE " + QUESTION_ID
                + " = NEW." + ANSWER_QUESTION_ID + ") IS NULL; END;"
                // Delete cascade
                + "CREATE TRIGGER fkd_answer_question_id "
                + "BEFORE DELETE ON " + TABLE_QUESTIONS + " FOR EACH ROW BEGIN "
                + "DELETE FROM " + TABLE_ANSWERS + " WHERE " + ANSWER_QUESTION_ID
                + " = OLD." + QUESTION_ID + "; END;";

        db.execSQL(CREATE_QUIZ_TABLE);
        db.execSQL(CREATE_QUESTIONS_TABLE);
        db.execSQL(CREATE_ANSWERS_TABLE);
        db.execSQL(CREATE_QUESTION_QUIZ_TRIGGERS);
        db.execSQL(CREATE_ANSWER_QUESTION_TRIGGERS);
    }

    // Updating database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables and triggers if existed
        db.execSQL("DROP TRIGGER IF EXISTS fki_answer_question_id");
        db.execSQL("DROP TRIGGER IF EXISTS fku_answer_question_id");
        db.execSQL("DROP TRIGGER IF EXISTS fkd_answer_question_id");
        db.execSQL("DROP TRIGGER IF EXISTS fki_question_quiz_id");
        db.execSQL("DROP TRIGGER IF EXISTS fku_question_quiz_id");
        db.execSQL("DROP TRIGGER IF EXISTS fkd_question_quiz_id");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ANSWERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZZES);

        // Create tables again
        onCreate(db);
    }

    // Quiz table methods
    // Add single quiz
    public void addQuiz(Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUIZ_NAME, quiz.getQuiz_name());

        db.insert(TABLE_QUIZZES, null, values);
        db.close();
    }

    // Get a single quiz
    public Quiz getQuiz(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUIZZES, new String[] {QUIZ_ID, QUIZ_NAME},
                QUIZ_ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Quiz quiz = new Quiz(Integer.parseInt(cursor.getString(0)), cursor.getString(1));

        cursor.close();
        return quiz;
    }

    // Get all quizzes
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUIZZES, new String[] {QUIZ_ID, QUIZ_NAME},
                null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Quiz quiz = new Quiz();
                quiz.setId(Integer.parseInt(cursor.getString(0)));
                quiz.setQuiz_name(cursor.getString(1));
                quizList.add(quiz);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return quizList;
    }

    // Update a single quiz
    public int updateQuiz(Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUIZ_NAME, quiz.getQuiz_name());

        return db.update(TABLE_QUIZZES, values, QUIZ_ID + "=?",
                new String[] {String.valueOf(quiz.getId())});
    }

    // Delete single quiz
    public void deleteQuiz(Quiz quiz) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUIZZES, QUIZ_ID + "=?",
                new String[] {String.valueOf(quiz.getId())});
        db.close();
    }

    // Question table methods
    // Add single question
    public void addQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUESTION_QUIZ_ID, question.getQuiz_id());
        values.put(QUESTION_NAME, question.getQuestion_name());

        db.insert(TABLE_QUESTIONS, null, values);
        db.close();
    }

    // Get questions for a single quiz
    public List<Question> getQuestionsForQuiz(int quiz_id) {
        List<Question> questionList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTIONS, new String[] {QUESTION_ID, QUESTION_QUIZ_ID,
                QUESTION_NAME}, QUESTION_QUIZ_ID + "=?", new String[] {String.valueOf(quiz_id)},
                null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Question question = new Question();
                question.setId(Integer.parseInt(cursor.getString(0)));
                question.setQuiz_id(Integer.parseInt(cursor.getString(1)));
                question.setQuestion_name(cursor.getString(2));
                questionList.add(question);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return questionList;
    }

    // Update a single question
    public int updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(QUESTION_QUIZ_ID, question.getQuiz_id());
        values.put(QUESTION_NAME, question.getQuestion_name());

        return db.update(TABLE_QUESTIONS, values, QUIZ_ID + "=?",
                new String[] {String.valueOf(question.getId())});
    }

    // Delete single question
    public void deleteQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTIONS, QUESTION_ID + "=?",
                new String[] {String.valueOf(question.getId())});
        db.close();
    }

    // Answer table methods
    public void addAnswer(Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ANSWER_QUESTION_ID, answer.getQuestion_id());
        values.put(ANSWER_TEXT, answer.getAnswer_text());
        values.put(ANSWER_CORRECT, answer.getCorrect());

        db.insert(TABLE_ANSWERS, null, values);
        db.close();
    }

    // Get answers for a single question
    public List<Answer> getAnswersForQuestion(int question_id) {
        List<Answer> answerList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ANSWERS, new String[] {ANSWER_ID, ANSWER_QUESTION_ID,
            ANSWER_TEXT, ANSWER_CORRECT}, ANSWER_QUESTION_ID + "=?",
            new String[] {String.valueOf(question_id)}, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Answer answer = new Answer();
                answer.setId(Integer.parseInt(cursor.getString(0)));
                answer.setQuestion_id(Integer.parseInt(cursor.getString(1)));
                answer.setAnswer_text(cursor.getString(2));
                answer.setCorrect(Integer.parseInt(cursor.getString(3)));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return answerList;
//TODO
    }

    // Update a single answer
    public int updateAnswer(Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ANSWER_QUESTION_ID, answer.getQuestion_id());
        values.put(ANSWER_TEXT, answer.getAnswer_text());
        values.put(ANSWER_CORRECT, answer.getCorrect());

        return db.update(TABLE_ANSWERS, values, ANSWER_ID + "=?",
                new String[] {String.valueOf(answer.getId())});
    }

    // Delete single answer
    public void deleteAnswer(Answer answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ANSWERS, ANSWER_ID + "=?",
                new String[] {String.valueOf(answer.getId())});
        db.close();
    }
}
