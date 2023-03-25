package com.example.finalexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private LinkedList<Word> wordsList=new LinkedList<>();

    public DatabaseHandler(@Nullable Context context) {
        super(context,Constants.DATABASE_NAME,null,Constants.DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

         String query="CREATE TABLE "+Constants.TABLE_NAME+ " ("
                 +Constants.KEY_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                 +Constants.VALUE+ " TEXT,"
                 +Constants.S_DATE+ " TEXT, "
                 +Constants.IS_DONE+ " TEXT, "
                 +Constants.F_DATE+ " TEXT)";

        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop Table if exists "+Constants.TABLE_NAME);
    }

    public boolean insertData(Word word){
        SQLiteDatabase DB=this.getWritableDatabase();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(word.getStartDate());
        String startDate = calendar.get(Calendar.YEAR) + "-"
                + calendar.get(Calendar.MONTH) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(word.getFinishDate());
        String endDate = calendar.get(Calendar.YEAR) + "-"
                + calendar.get(Calendar.MONTH) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);


        ContentValues contentValues=new ContentValues();
        contentValues.put(Constants.VALUE, word.getValue());
        contentValues.put(Constants.S_DATE, startDate);
        contentValues.put(Constants.F_DATE, endDate);
        contentValues.put(Constants.IS_DONE,Boolean.FALSE.toString(word.isDone()));

        long result=DB.insert(Constants.TABLE_NAME, null,contentValues);
        DB.close();
        if (result==-1){
            return  false;
        }
        else return true;
    }
    public boolean updateData(Word word){

        SQLiteDatabase DB=this.getWritableDatabase();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(word.getStartDate());
        String startDate = calendar.get(Calendar.YEAR) + "-"
                + calendar.get(Calendar.MONTH) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(word.getFinishDate());
        String endDate = calendar.get(Calendar.YEAR) + "-"
                + calendar.get(Calendar.MONTH) + "-"
                + calendar.get(Calendar.DAY_OF_MONTH);


        ContentValues contentValues=new ContentValues();
        contentValues.put(Constants.VALUE, word.getValue());
        contentValues.put(Constants.S_DATE, startDate);
        contentValues.put(Constants.F_DATE, endDate);
        contentValues.put(Constants.IS_DONE,Boolean.FALSE.toString(word.isDone()));

        Cursor cursor=DB.rawQuery("Select * from "+Constants.TABLE_NAME+" where "+Constants.KEY_ID+"= ? " ,
                new String[]{String.valueOf(word.getItemID())});

        if (cursor.getCount() > 0){
            long result=DB.update(Constants.TABLE_NAME,contentValues,Constants.KEY_ID+"= ? " ,
                    new String[]{String.valueOf(word.getItemID())});
            if (result==-1){
                DB.close();
                return false;
            }
            else {
                DB.close();
                return true;
            }

        }
        else {
            DB.close();
            return false;
        }
    }

    public boolean deleteData(Word word) {
        SQLiteDatabase DB = this.getWritableDatabase();

        Cursor cursor = DB.rawQuery(" Select * from " + Constants.TABLE_NAME + " where " + Constants.KEY_ID + " = ?",
                new String[]{String.valueOf(word.getItemID())});
        if (cursor.getCount() > 0) {
            long result = DB.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ? ",
                    new String[]{String.valueOf(word.getItemID())});

            if (result == -1) return false;
            else return true;
        } else {
            return false;
        }

    }

    public Cursor getData() {
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("Select * from "+Constants.TABLE_NAME,null);
        return cursor;
    }
    public LinkedList<Word> getWords(){
        wordsList.clear();
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor=DB.rawQuery("Select * from "+Constants.TABLE_NAME,null);
        if (cursor.getCount()==0){
            Log.i("DB","getwords: empty error");
            return wordsList;
        }
        else{
            while (cursor.moveToNext()){
                Word word=new Word();
                word.setItemID(cursor.getInt(0));
                word.setValue(cursor.getString(1));
                word.setStartDate(cursor.getString(2));
                word.setDone(Boolean.parseBoolean(cursor.getString(3)));
                word.setFinishDate(cursor.getString(4));

                wordsList.add(word);
            }
        }
        DB.close();
        return wordsList;
    }
}
