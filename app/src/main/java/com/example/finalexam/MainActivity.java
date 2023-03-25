package com.example.finalexam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;



public class MainActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener{

    public static final String EXTRA_MESSAGE =
            "com.example.android.lab5.extra.MESSAGE";
    public static final String EXTRA_REPLY =
            "com.example.android.lab5.extra.REPLY";
     LinkedList<Word> mWordList = new LinkedList<>();

    //ArrayList<Word>  words=new ArrayList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;

    DatabaseHandler DB;

    boolean seeDone = true;
        boolean seeNotDone = true;

    SharedPreferences settingsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingsFile=getSharedPreferences("appSettings", Context.MODE_PRIVATE);
        if (settingsFile.getInt("viewMode",-1)==-1){
            SharedPreferences.Editor editor=settingsFile.edit();
            editor.putInt("viewNode",0);
            editor.commit();
        }


       DB=new DatabaseHandler(this);
       mWordList=DB.getWords();

        Spinner spinner = findViewById(R.id.label_spinner);
        if (spinner != null) {
            spinner.setOnItemSelectedListener(this);
        }
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.labels_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);

// Apply the adapter to the spinner.
        if (spinner != null) {
            spinner.setAdapter(adapter);
        }


      Viewing();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addword:
                Intent intent = new Intent(MainActivity.this,
                        AddWord.class);
                intent.putExtra(EXTRA_MESSAGE,"AddWord");

                startActivityForResult(intent, 1); // settings
                return true;

            default:
                // Do nothing
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK){ // add word
            DB=new DatabaseHandler(this);
            mWordList=DB.getWords();
            mAdapter = new WordListAdapter(this, mWordList);
// Connect the adapter with the RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
// Give the RecyclerView a default layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        }
        else if(requestCode == 2 && resultCode == RESULT_OK){ // update word

            DB=new DatabaseHandler(this);
            mWordList=DB.getWords();
            mAdapter = new WordListAdapter(this, mWordList);
// Connect the adapter with the RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
// Give the RecyclerView a default layout manager.
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        }

    }

    public void Viewing(){
        LinkedList<Word> newWords = new LinkedList<>();
        for(int i = 0; i < mWordList.size(); i++) {
            if(settingsFile.getInt("viewNode",-1)==0) {

                    newWords.add(mWordList.get(i));

            }
            if(settingsFile.getInt("viewNode",-1)==2) {
                if(mWordList.get(i).isDone()) { // seeDone
                    newWords.add(mWordList.get(i));
                }
            }
            if(settingsFile.getInt("viewNode",-1)==1) { //seeNotDone
                if(!mWordList.get(i).isDone()) {
                    newWords.add(mWordList.get(i));
                }
            }
        }
        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerview);
// Create an adapter and supply the data to be displayed.
        mAdapter = new WordListAdapter(this, newWords);
// Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
// Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String spinnerLabel = adapterView.getItemAtPosition(i).toString();
      if (spinnerLabel.equals("Did and did not")){
          SharedPreferences.Editor editor=settingsFile.edit();
          editor.putInt("viewNode",0);
          editor.commit();
          seeDone=true;
          seeNotDone=true;
          System.out.println("2 did");
          Viewing();
      }
      else if (spinnerLabel.equals("Not Did")){
          SharedPreferences.Editor editor=settingsFile.edit();
          editor.putInt("viewNode",1);
          editor.commit();
          seeDone=false;
          seeNotDone=true;
          System.out.println("not did");
          Viewing();
      }
      else{
          SharedPreferences.Editor editor=settingsFile.edit();
          editor.putInt("viewNode",2);
          editor.commit();
          System.out.println("did");
          seeDone=true;
          seeNotDone=false;
          Viewing();
      }
      //  Toast.makeText(this, spinnerLabel, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}