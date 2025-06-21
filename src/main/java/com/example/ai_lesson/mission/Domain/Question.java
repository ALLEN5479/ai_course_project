package com.example.ai_lesson.mission.Domain;

public class Question {
    private int questionId;
    private String questionName;
    private String sectionA;
    private String sectionB;
    private String sectionC;
    private String sectionD;
    private String answer;
    public Question(int questionId, String questionName, String sectionA, String sectionB, String sectionC, String sectionD, String answer){
        this.questionId = questionId;
        this.questionName = questionName;
        this.sectionA = sectionA;
        this.sectionB = sectionB;
        this.sectionC = sectionC;
        this.sectionD = sectionD;
        this.answer = answer;
    }
    public int getQuestionId() {
        return questionId;
    }
    public String getQuestionName() {
        return questionName;
    }
    public String getSectionA() {
        return sectionA;
    }
    public String getSectionB() {
        return sectionB;
    }
    public String getSectionC() {
        return sectionC;
    }
    public String getSectionD() {
        return sectionD;
    }
    public String getAnswer(){
        return answer;
    }
    public void setQuestionId(int questionId){
        this.questionId = questionId;
    }
    public void setQuestionName(String questionName){
        this.questionName = questionName;
    }
    public void setSectionA(String sectionA){
        this.sectionA = sectionA;
    }
    public void setSectionB(String sectionB){
        this.sectionB = sectionB;
    }
    public void setSectionC(String sectionC){
        this.sectionC = sectionC;
    }
    public void setSectionD(String sectionD){
        this.sectionD = sectionD;
    }
    public void setAnswer(String answer){
        this.answer = answer;
    }
    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", questionName='" + questionName + '\'' +
                ", sectionA='" + sectionA + '\'' +
                ", sectionB='" + sectionB + '\'' +
                ", sectionC='" + sectionC + '\'' +
                ", sectionD='" + sectionD + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}