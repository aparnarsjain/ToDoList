package com.example.aparna.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class EditItemActivity extends AppCompatActivity {
    public String editTitle;
    public EditText editTextView;
    Integer position;
    private Item item;
    ArrayList<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        item = (Item) getIntent().getSerializableExtra("item");
        editTextView = (EditText)findViewById(R.id.editText);
        editTextView.setText(item.text);
        editTextView.setSelection(item.text.length());
    }
    public  void onSave(View v) {
        Intent data = new Intent();
        String editedText = editTextView.getText().toString();
        item.text = editedText;
        TodoListSqlLiteDB.getInstance(this).addorUpdateItem(item);
        setResult(RESULT_OK, data);
        finish();
    }
}
