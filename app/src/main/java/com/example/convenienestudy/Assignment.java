package com.example.convenienestudy;

public class Assignment {
    private int quizNumber, studentId, score;
    private String feedback = "Await Feedback";
    private boolean completed;

    public Assignment(){}

    public Assignment(int quizNumber, int studentId) {
        this.quizNumber = quizNumber;
        this.studentId = studentId;
    }

    public int getQuizNumber() {
        return quizNumber;
    }

    public int getStudentId() {
        return studentId;
    }

    public int getScore() {
        return score;
    }

    public String getFeedback() {
        return feedback;
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
}
