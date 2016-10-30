package com.ngustafson.multichoice.maint;

/**
 * Created by Nick on 10/2/2016.
 */

public class Quiz {
    private int id;
    private String quiz_name;

    public Quiz() {
    }

    public Quiz(int id, String quiz_name) {
        this.id = id;
        this.quiz_name = quiz_name;
    }

    public Quiz(String quiz_name) {
        this.quiz_name = quiz_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuiz_name() {
        return quiz_name;
    }

    public void setQuiz_name(String quiz_name) {
        this.quiz_name = quiz_name;
    }
}
