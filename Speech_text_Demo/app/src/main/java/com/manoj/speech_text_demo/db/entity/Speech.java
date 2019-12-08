package com.manoj.speech_text_demo.db.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "speech")

public class Speech {

    @NonNull
    @ColumnInfo(name = "speech_text")
    @PrimaryKey(autoGenerate = false)
    private String speechText;

    @Ignore
    public Speech(){}
    public Speech(@NonNull String speechText) {
        this.speechText = speechText;
    }

    @NonNull
    public String getSpeechText() {
        return speechText;
    }

    public void setSpeechText(@NonNull String speechText) {
        this.speechText = speechText;
    }
}
