package com.manoj.speech_text_demo.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.manoj.speech_text_demo.db.entity.Speech;

@Database(entities = {Speech.class},version = 1)


public abstract class SpeechAppDatabase extends RoomDatabase {

    public abstract SpeechDAO speechDAO();
}
