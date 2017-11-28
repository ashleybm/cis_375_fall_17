package edu.umich.cliqus.questionnaire;

import java.util.ArrayList;

/**
 * Created by Ashley Baker on 12/4/2017.
 */

public class Question {
    private final String TAG = "cliqus";

    private String mainTag;
    private ArrayList<String> tagList;
    private String tagUID;

    public Question(){
        mainTag = null;
        tagList.clear();
        tagUID = null;
    }

    public Question(String tag, String uid, ArrayList<String> list){
        this.mainTag = tag;
        this.tagUID = uid;
        this.tagList = list;
    }

    public String getMainTag(){ return mainTag; }

    public void setMainTag(String tag){ this.mainTag = tag; }

    public String getTagUID(){ return tagUID; }

    public void setTagUID(String uid){this.tagUID = uid; }

    public ArrayList<String> getTagList() { return tagList; }

    public void setTagList(ArrayList<String> tagList){ this.tagList = tagList; }

    public void addTagToList(String newTag){ this.tagList.add(newTag);}


}


