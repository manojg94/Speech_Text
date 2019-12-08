package com.manoj.speech_text_demo.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.manoj.speech_text_demo.db.entity.Speech;

import java.util.List;

@Dao
public interface SpeechDAO {

    @Insert
    public long addSpeech(Speech speech);

    @Query("select * from speech")
    public List<Speech> getallSpeeches();

    @Query("select * from speech where speech_text ==:speech_id")
    public Speech getSpeech(long speech_id);

    @Query("select * from speech where speech_text ==:speech_text")
    public Speech getSpeechbyName(String speech_text);
}
