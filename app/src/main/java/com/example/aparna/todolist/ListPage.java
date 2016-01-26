package com.example.aparna.todolist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ListPage extends AppCompatActivity {

    ListView lvItems;

    private final int REQUEST_CODE = 20;

    ArrayList<Item> items = new ArrayList<>();
    CustomItemsAdapter itemsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_page);

        readItems();

        itemsAdapter = new CustomItemsAdapter(this, items);
        // Attach the adapter to a ListView
        lvItems = (ListView)findViewById(R.id.lvItems);
        lvItems.setAdapter(itemsAdapter);


        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position, long id) {
                Item item = (Item) adapter.getItemAtPosition(position);
                Intent intent = new Intent(ListPage.this, EditItemActivity.class);
                intent.putExtra("item", item);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {
                        Item itemToDelete = items.get(pos);
                        if(deleteItem(itemToDelete.getId())) {
                            readItems();
                            return true;
                        }
                        return false;
                    }
                });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    //When the screen loads
    @Override
    public  void onResume() {
        super.onResume();
        readItems();
        runOnUiThread(new Runnable() {
            public void run() {
                itemsAdapter.notifyDataSetChanged();
            }
        });
    }
    public void onAddItem(View v) {
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Item newItem = new Item();
        newItem.setText(itemText);
        etNewItem.setText("");
        newItem.setId(TodoListSqlLiteDB.getInstance(this).addorUpdateItem(newItem));
        itemsAdapter.add(newItem);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etNewItem.getWindowToken(), 0);
    }
    public  void editItem(String editTitle) {

    }
    private void readItems() {
        items.clear();
        items.addAll(TodoListSqlLiteDB.getInstance(this).getAllItems());
        runOnUiThread(new Runnable() {
            public void run() {
                if (itemsAdapter != null){
                    itemsAdapter.notifyDataSetChanged();
                }

            }
        });

    }
    private boolean deleteItem(long id) {
        return TodoListSqlLiteDB.getInstance(this).deleteItem(id);
    }
    public void launchEditView() {
        // first parameter is the context, second is the class of the activity to launch
        Intent i = new Intent(ListPage.this, EditItemActivity.class);
        startActivity(i); // brings up the second activity
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            readItems();
            runOnUiThread(new Runnable() {
                public void run() {
                    itemsAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
