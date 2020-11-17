package com.example.convenienestudy;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Assignment implements Parcelable {
    private int score;
    private String feedback = "Await Feedback";
    private boolean completed;
    private String studentId, quizNumber, dueDate;

    public Assignment(){}

    public Assignment(String quizNumber, String studentId) {
        this.quizNumber = quizNumber;
        this.studentId = studentId;
        this.dueDate = makeDueDate();
    }

    public static final Creator<Assignment> CREATOR = new Creator<Assignment>() {
        @Override
        public Assignment createFromParcel(Parcel source) {
            return new Assignment(source);
        }

        public Assignment[] newArray(int size) {
            return new Assignment[size];
        }
    };

    protected Assignment(Parcel in){
        score = in.readInt();
        feedback = in.readString();
        completed = in.readInt() == 1;
        studentId = in.readString();
        quizNumber = in.readString();
        dueDate = in.readString();
    }

    public String getQuizNumber() {
        return quizNumber;
    }

    public String getStudentId() {
        return studentId;
    }

    public int getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setScore(int score) {
        this.score = this.score + score;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    private String makeDueDate(){
        LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusDays(7);
        return localDateTime.toString();
    }

    @Override
    public String toString(){
        return "Assignment: " + quizNumber + " belong to " + studentId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(score);
        dest.writeString(feedback);
        dest.writeInt(completed ? 1 : 0);
        dest.writeString(studentId);
        dest.writeString(quizNumber);
        dest.writeString(dueDate);
    }
}
