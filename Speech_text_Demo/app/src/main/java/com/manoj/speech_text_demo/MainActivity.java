package com.manoj.speech_text_demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.manoj.speech_text_demo.Adapter.SpeechrecyclerViewAdapter;
import com.manoj.speech_text_demo.db.SpeechAppDatabase;
import com.manoj.speech_text_demo.db.entity.Speech;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText txtSpeechInput;
    private ImageButton btnSpeak,btnrefresh;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    RecyclerView recyclerView;
    private SpeechAppDatabase speechAppDatabase;
    private ArrayList<Speech> speechArrayList = new ArrayList<>();
    public SpeechrecyclerViewAdapter adapter;
    private boolean Speechflag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtSpeechInput = (EditText) findViewById(R.id.speechtext);
        btnSpeak = (ImageButton) findViewById(R.id.btnSpeak);
        btnrefresh = (ImageButton) findViewById(R.id.btnRefresh);

        recyclerView = findViewById(R.id.my_recycler_view);
        speechAppDatabase = Room.databaseBuilder(getApplicationContext(),
                SpeechAppDatabase.class, "speechDB").build();
        adapter = new SpeechrecyclerViewAdapter(speechArrayList);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        new getAllSpeechsAsyncTask().execute();
        btnSpeak.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                recyclerView.getRecycledViewPool().clear();
                promptSpeechInput();
            }
        });

        btnrefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Page Refreshed..", Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter("");
            }
        });

    }

    /**
     * Showing google speech input dialog
     */
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Receiving speech input
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeechInput.setText(result.get(0));
                    Log.d("textspeech", result.get(0).toString());

//                    Speech speech=new Speech(result.get(0).trim());
//                    speechArrayList.add(speech);
//                    adapter.notifyDataSetChanged();
                    new createSpeechAsyncTask().execute(new Speech(result.get(0).trim()));

                }
                break;
            }

        }
    }


    public class createSpeechAsyncTask extends AsyncTask<Speech, Void, Void> {

        String speechtextdata=null;
        @Override
        protected Void doInBackground(Speech... speeches) {

            speechtextdata=speeches[0].getSpeechText();
            Speech checkdatapresent = speechAppDatabase.speechDAO().getSpeechbyName(speeches[0].getSpeechText());
            if (checkdatapresent == null) {
                Speechflag = true;
                speechAppDatabase.speechDAO().addSpeech(speeches[0]);
                speechArrayList.add(speeches[0]);
            } else {
                Speechflag = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.getRecycledViewPool().clear();
            adapter.notifyDataSetChanged();
            if (Speechflag) {
                Toast.makeText(MainActivity.this, "Saved Successfully..", Toast.LENGTH_SHORT).show();
                adapter.getFilter().filter("");
            } else {
                Toast.makeText(MainActivity.this, "Already Saved..", Toast.LENGTH_SHORT).show();
                if (speechtextdata!=null){
                    adapter.getFilter().filter(speechtextdata);
                }
            }


        }
    }


    public class getAllSpeechsAsyncTask extends AsyncTask<Speech,Void,Void> {

        @Override
        protected Void doInBackground(Speech... speeches) {
            speechArrayList.addAll(speechAppDatabase.speechDAO().getallSpeeches());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.getRecycledViewPool().clear();
            adapter.notifyDataSetChanged();
            for (int i=0;i<speechArrayList.size();i++){
                Log.d("allbookmarks",speechArrayList.get(i).getSpeechText().toString());

            }
        }
    }
}
