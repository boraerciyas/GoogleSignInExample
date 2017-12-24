package com.example.ybe.googlesigninexample;

/**
 * Created by ybe on 17.12.2017.
 */

public class DailyPlan {

    public String architecture, literature, music, painting, sculpture, theater;

    DailyPlan(){

    }

    public DailyPlan(String architecture, String literature, String music, String painting, String sculpture, String theater) {
        this.architecture = architecture;
        this.literature = literature;
        this.music = music;
        this.painting = painting;
        this.sculpture = sculpture;
        this.theater = theater;
    }

    String getArchitecture() {
        return architecture;
    }

    void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    String getLiterature() {
        return literature;
    }

    void setLiterature(String literature) {
        this.literature = literature;
    }

    String getMusic() {
        return music;
    }

    void setMusic(String music) {
        this.music = music;
    }

    String getPainting() {
        return painting;
    }

    void setPainting(String painting) {
        this.painting = painting;
    }

    String getSculpture() {
        return sculpture;
    }

    void setSculpture(String sculpture) {
        this.sculpture = sculpture;
    }

    String getTheater() {
        return theater;
    }

    void setTheater(String theater) {
        this.theater = theater;
    }

    @Override
    public String toString() {
        return "DailyPlan{" +
                "architecture='" + architecture + '\'' +
                ", literature='" + literature + '\'' +
                ", music='" + music + '\'' +
                ", painting='" + painting + '\'' +
                ", sculpture='" + sculpture + '\'' +
                ", theater='" + theater + '\'' +
                '}';
    }
}

