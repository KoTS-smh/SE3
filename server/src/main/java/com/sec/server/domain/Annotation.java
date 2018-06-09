package com.sec.server.domain;

public class Annotation {

    private int taskOrderId;

    private int pictureNum;

    private String sentence;

    private String words;

    private String coordinates;

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }


    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public int getTaskOrderId() {
        return taskOrderId;
    }

    public void setTaskOrderId(int taskOrderId) {
        this.taskOrderId = taskOrderId;
    }

    public int getPictureNum() {
        return pictureNum;
    }

    public void setPictureNum(int pictureNum) {
        this.pictureNum = pictureNum;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }
}
