package com.ngustafson.multichoice.maint;

/**
 * Created by Nick on 10/2/2016.
 */

public class Answer {
    private int id;
    private int question_id;
    private String answer_text;
    private int correct;

    public Answer() {
    }

    public Answer(int id, int question_id, String answer_text, int correct) {
        this.id = id;
        this.question_id = question_id;
        this.answer_text = answer_text;
        this.correct = correct;
    }

    public Answer(int question_id, String answer_text, int correct) {
        this.question_id = question_id;
        this.answer_text = answer_text;
        this.correct = correct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }
}
