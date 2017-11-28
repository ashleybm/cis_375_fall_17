package edu.umich.cliqus.questionnaire;

import java.util.ArrayList;

/**
 * Created by Ashley Baker on 12/4/2017.
 */

public class Questionnaire {
    private final String ofText = "Of";
    private ArrayList<Question> questionList;
    private int numQuestions;
    private int currQuestionNum;

    private String questionCountStatement;

    public Questionnaire(){
        questionList.clear();
        numQuestions = 0;
        currQuestionNum = 0;
        questionCountStatement = null;
    }

    public Questionnaire(ArrayList<Question> questionList){
        this.questionList = questionList;
        this.numQuestions = questionList.size();
        this.currQuestionNum = 0;
        questionCountStatement = currQuestionNum + ofText + numQuestions;
    }

    public boolean nextQuestion(){
        if (currQuestionNum + 1 != numQuestions) {
            currQuestionNum = currQuestionNum + 1;
            questionCountStatement = currQuestionNum + ofText + numQuestions;
            return true;
        }
        else{
            return false;
        }

    }




}
