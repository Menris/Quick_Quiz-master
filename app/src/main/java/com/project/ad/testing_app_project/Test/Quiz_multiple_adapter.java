package com.project.ad.testing_app_project.Test;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;
import java.util.Objects;

/**
 * Created by menri on 13.03.2017.
 */

public class Quiz_multiple_adapter {

    String answerA;
    String answerB;
    String answerC;
    String answerD;
    String answerE;
    String answerF;
    String answerG;
    String answerH;
    String answerI;
    String answerJ;

    String answer;
    String myAnswer;

    String question;
    String questionNumber;
    String quizTitle;
    String correctAnswer;
    String teacherID;

    public Quiz_multiple_adapter() {

    }

    public String getAnswer(Character a) {
        if (Objects.equals(a, 'A')) {
            return getAnswerA();
        } else if (Objects.equals(a, 'B')) {
            return getAnswerB();
        } else if (Objects.equals(a, 'C')) {
            return getAnswerC();
        } else if (Objects.equals(a, 'D')) {
            return getAnswerD();
        } else if (Objects.equals(a, 'E')) {
            return getAnswerE();
        } else if (Objects.equals(a, 'F')) {
            return getAnswerF();
        } else if (Objects.equals(a, 'G')) {
            return getAnswerG();
        } else if (Objects.equals(a, 'H')) {
            return getAnswerH();
        } else if (Objects.equals(a, 'I')) {
            return getAnswerI();
        } else if (Objects.equals(a, 'J')) {
            return getAnswerJ();
        }
        return null;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getMyAnswer() {
        return myAnswer;
    }

    public void setMyAnswer(String myAnswer) {
        this.myAnswer = myAnswer;
    }

    public String getAnswerA() {
        return answerA;
    }

    public void setAnswerA(String answerA) {
        this.answerA = answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public void setAnswerB(String answerB) {
        this.answerB = answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public void setAnswerC(String answerC) {
        this.answerC = answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public void setAnswerD(String answerD) {
        this.answerD = answerD;
    }

    public String getAnswerE() {
        return answerE;
    }

    public void setAnswerE(String answerE) {
        this.answerE = answerE;
    }

    public String getAnswerF() {
        return answerF;
    }

    public void setAnswerF(String answerF) {
        this.answerF = answerF;
    }

    public String getAnswerG() {
        return answerG;
    }

    public void setAnswerG(String answerG) {
        this.answerG = answerG;
    }

    public String getAnswerH() {
        return answerH;
    }

    public void setAnswerH(String answerH) {
        this.answerH = answerH;
    }

    public String getAnswerI() {
        return answerI;
    }

    public void setAnswerI(String answerI) {
        this.answerI = answerI;
    }

    public String getAnswerJ() {
        return answerJ;
    }

    public void setAnswerJ(String answerJ) {
        this.answerJ = answerJ;
    }
}
