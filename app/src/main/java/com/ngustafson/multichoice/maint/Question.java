package com.ngustafson.multichoice.maint;

/**
 * Created by Nick on 10/2/2016.
 */

public class Question {
    private int id;
    private int quiz_id;
    private String question_name;

    public Question() {
    }

    public Question(int id, int quiz_id, String question_name) {
        this.id = id;
        this.quiz_id = quiz_id;
        this.question_name = question_name;
    }

    public Question(int quiz_id, String question_name) {
        this.quiz_id = quiz_id;
        this.question_name = question_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuiz_id() {
        return quiz_id;
    }

    public void setQuiz_id(int quiz_id) {
        this.quiz_id = quiz_id;
    }

    public String getQuestion_name() {
        return question_name;
    }

    public void setQuestion_name(String question_name) {
        this.question_name = question_name;
    }
}
