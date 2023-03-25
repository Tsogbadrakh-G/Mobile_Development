package com.example.finalexam;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder>
{


    class WordViewHolder extends RecyclerView.ViewHolder {
        public final TextView wordItemView;
        final WordListAdapter mAdapter;
        LinearLayout layout;
        CheckBox checkBox;
        public WordViewHolder(@NonNull View itemView, WordListAdapter adapter) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.word);
            layout=itemView.findViewById(R.id.itemLayout);
            checkBox=itemView.findViewById(R.id.checkbox);
            this.mAdapter = adapter;
        }


    }


    private LayoutInflater mInflater;
    private final LinkedList<Word> mWordList;


    public WordListAdapter(Context context,
                           LinkedList<Word> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList = wordList;
    }

    @NonNull
    @Override
    public WordListAdapter.WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mItemView = mInflater.inflate(R.layout.wordlist_item,
                parent, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter.WordViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Word mCurrent = mWordList.get(position);
        holder.wordItemView.setText(mCurrent.getValue());
        holder.checkBox.setChecked(mCurrent.isDone());
        if (mCurrent.isDone()){
            holder.wordItemView.setTextColor(Color.rgb(0,255,0));
        }
        else{
            holder.wordItemView.setTextColor(Color.rgb(0,0,0));
        }
        holder.layout.setOnLongClickListener(new View.OnLongClickListener() {
            public boolean onLongClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), AddWord.class);
                intent.putExtra(MainActivity.EXTRA_MESSAGE, "update");
                intent.putExtra("update_value",mCurrent.getValue());
                intent.putExtra("index",position);
                intent.putExtra("id",mCurrent.getItemID());
                intent.putExtra("checked",mCurrent.isDone());
                ((Activity)holder.itemView.getContext()).startActivityForResult(intent, 2);
                return true;
            }
        });

       holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               DatabaseHandler DB = new DatabaseHandler(holder.itemView.getContext());
               mCurrent.setDone(b);
               mWordList.set(position,mCurrent);
               DB.updateData(mCurrent);
               if (mCurrent.isDone()){
                   holder.wordItemView.setTextColor(Color.rgb(0,255,0));
               }
               else{
                   holder.wordItemView.setTextColor(Color.rgb(0,0,0));
               }
           }
       });
    }

    @Override
    public int getItemCount() {
        return mWordList.size();
    }
}
