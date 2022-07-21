package com.rottenbeetle.testtaskforbars;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.control.CheckBox;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Contract {
    private int id;
    private int number;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String date ;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String lastUpdate;
    @JsonIgnore
    private CheckBox relevance;



    public Contract() {
        relevance = new CheckBox(){
            @Override
            public void arm() {
                //Только чтение
            }
        };
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(Date date) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        this.date = df.format(date.getTime());
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        this.lastUpdate = df.format(lastUpdate.getTime());
    }

    public CheckBox getRelevance() {
        return relevance;
    }

    public void setRelevance(CheckBox relevance) {
        this.relevance = relevance;
    }


}
