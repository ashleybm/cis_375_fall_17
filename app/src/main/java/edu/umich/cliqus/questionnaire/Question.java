package edu.umich.cliqus.questionnaire;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable {
    private final String TAG = "cliqus";

    private String questionStr;
    private String questionAnsChoices;
    private String questionUID;
    private ArrayList questionAnsChoicesList;

    public Question(){
        questionStr = null;
        questionAnsChoices = null;
        questionAnsChoicesList = null;
        questionUID = null;
    }

    public Question(String questionStr, String questionUID, String questionAnsChoices){
        this.questionStr = questionStr;
        this.questionUID = questionUID;
        this.questionAnsChoicesList = questionAnsChoicesList;
        breakDownList();
    }

    public String getQuestionStr(){ return questionStr; }

    public void setQuestionStr(String tag){ this.questionStr = tag; }

    public String getQuestionUID(){ return questionUID; }

    public void setQuestionUID(String uid){this.questionUID = uid; }

    public String getQuestionAnsChoices() { return questionAnsChoices; }

    public void setQuestionAnsChoices( String questionAnsChoices) {
        this.questionAnsChoices = questionAnsChoices;
    }

    public ArrayList<String> getQuestionAnsChoicesList() { return questionAnsChoicesList; }

    public void setQuestionAnsChoicesList(ArrayList<String> tagList){
        this.questionAnsChoicesList = questionAnsChoicesList;
    }

    public void addTagToList(String newTag){ this.questionAnsChoicesList.add(newTag);}

    public void breakDownList() {
        String quesAns = questionAnsChoices;
        if(quesAns != null)
            while(!quesAns.isEmpty()) {
                questionAnsChoicesList.add(
                    quesAns.substring(quesAns.indexOf('{') + 1, quesAns.indexOf('}') - 1));
                quesAns.replace(
                    "{" + questionAnsChoicesList.get(questionAnsChoicesList.size()).toString() +"}",
                    "");
            }
    }

}


