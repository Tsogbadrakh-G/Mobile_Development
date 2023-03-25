package com.example.finalexam;

import static com.example.finalexam.MainActivity.EXTRA_REPLY;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddWord extends AppCompatActivity {

    int id=-1;
    int index=-1;

    boolean checked1;
    DatePicker datePicker;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_word);
        Intent intent=getIntent();
        datePicker = findViewById(R.id.datePicker);
        editText=findViewById(R.id.editTextTextPersonName);
        if(intent.getStringExtra(MainActivity.EXTRA_MESSAGE).equals("update")){
            String value=intent.getStringExtra("update_value");
            editText.setText(value);
            id=intent.getIntExtra("id",-1);
            index=intent.getIntExtra("index",-1);
            checked1=intent.getBooleanExtra("checked",false);
        }
        else {
            editText.setText("");
        }
    }

    public void btn_back(View view) {
        Intent intent=new Intent();
        intent.putExtra(EXTRA_REPLY, 0);
        setResult(RESULT_OK, intent);

        DatabaseHandler DB=new DatabaseHandler(AddWord.this);
        Word word=new Word();
        word.setValue(String.valueOf(editText.getText()));
        if (id > 0)
        word.setItemID(id);
        else return;

        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(AddWord.this);
        myAlertBuilder.setTitle("Alert");
        myAlertBuilder.setMessage("Click OK to continue, or Cancel to stop:");
        // Set the dialog title and message.



        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("OK", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked the OK button.
                        boolean check=DB.deleteData(word);
                        if (check) Toast.makeText(AddWord.this, word.getValue()+" is deleted", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        myAlertBuilder.setNegativeButton("Cancel", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // User cancelled the dialog.
                        Toast.makeText(getApplicationContext(), "Pressed Cancel",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
        myAlertBuilder.show();

    }

    public void btn_save(View view) {

        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int day = datePicker.getDayOfMonth();
        String finishDate = String.valueOf(year) + "-" + String.valueOf(month) + "-"
                + String.valueOf(day);

        String value= String.valueOf(editText.getText());
        Intent intent=new Intent();
        intent.putExtra(EXTRA_REPLY, value);

        Calendar calendar = Calendar.getInstance();
        String currentString = String.valueOf(calendar.get(Calendar.YEAR)) + "-"
                + String.valueOf(calendar.get(Calendar.MONTH)) + "-"
                + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));

        Word word=new Word();
        word.setFinishDate(finishDate);
        word.setStartDate(currentString);
        word.setDone(checked1);
        word.setValue(String.valueOf(editText.getText()));

        DatabaseHandler DB=new DatabaseHandler(AddWord.this);

        if (id > 0) {
            word.setItemID(id);
         DB.updateData(word);
         intent.putExtra("index",index);
            Toast.makeText(this,  word.getValue()+ " successfully uptaded "+index, Toast.LENGTH_SHORT).show();
        }
        else {
            DB.insertData(word);

        }
        setResult(RESULT_OK, intent);
        finish();
    }
}