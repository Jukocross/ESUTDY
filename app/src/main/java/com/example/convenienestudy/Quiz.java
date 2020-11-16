package com.example.convenienestudy;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Quiz implements Parcelable {

    private String Title;
    private String Description;
    private int totalScore = 0;
    private int currentScore = 0;
    private ArrayList<Question> listOfQuestion = new ArrayList<Question>();
    private int quizNumber;
    private boolean quizCompleted;


    public Quiz(){
    }

    public Quiz(String title, String description, int quizNumber) {
        this.Title = title;
        this.Description = description;
        this.quizNumber = quizNumber;
    }

    public static final Creator<Quiz> CREATOR = new Creator<Quiz>() {
        @Override
        public Quiz createFromParcel(Parcel in){
            return new Quiz(in);
        }
        public Quiz[] newArray(int size){
            return new Quiz[size];
        }
    };

    protected Quiz(Parcel in){
        Title = in.readString();
        Description = in.readString();
        totalScore = in.readInt();
        currentScore = in.readInt();
        in.readList(listOfQuestion, Quiz.class.getClassLoader());
    }

    public String getDescription() {
        if (Description == null){
            return "Description";
        }
        return Description;
    }

    public int getQuizId() {
        return quizNumber;
    }

    public String getTitle() {
        return Title;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public ArrayList<Question> getListOfQuestion() {
        return listOfQuestion;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public String getScoreToString(){
        return ("Score: " + Integer.toString(currentScore) + " / " + Integer.toString(totalScore));
    }

    public boolean addQuestion(Question q){
        boolean added = false;
        if(!listOfQuestion.contains(q)) {
            this.listOfQuestion.add(q);
            added = true;
        }
        return added;
    }

    public boolean removeQuestion(Question q){
        boolean removed = false;
        if(listOfQuestion.contains(q)){
            this.listOfQuestion.remove(q);
            removed = true;
        }
        return removed;
    }

    public void updateMaxScore(int score){
        this.totalScore = this.totalScore + score;
    }

    public void updateCurrentScore(int score){
        this.currentScore = this.currentScore + score;
    }

    @Override
    //Parcel
    public void writeToParcel(Parcel dest, int flags){
        dest.writeString(Title);
        dest.writeString(Description);
        dest.writeInt(totalScore);
        dest.writeInt(currentScore);
        dest.writeList(listOfQuestion);
    }
    public int describeContents() {
        return 0;
    }
}
