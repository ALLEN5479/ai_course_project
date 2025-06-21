package com.example.ai_lesson.mission.Domain;

public class Paper {
    private int paperId;
    private String paperName;

    public Paper(int paperId, String paperName) {
        this.paperId = paperId;
        this.paperName = paperName;
    }
    public int getPaperId() {
        return paperId;
    }


    public String getPaperName() {
        return paperName;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public void setPaperName(String paperName) {
        this.paperName = paperName;
    }
    @Override
    public String toString() {
        return "Paper{" +
               "paperId=" + paperId +
               ", paperName='" + paperName + '\'' +
               '}';
    }
}
