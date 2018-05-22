package com.sec.server.domain;

import java.util.List;

public class Annotation {

    private int taskOrderId;

    private int pictureNum;

    private String sentence;

    private String coordinates;//json str

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
}
