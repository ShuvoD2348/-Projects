package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity {
    EditText searchItems;
    ListView searchResults;
    Button search, createnew, deletelater;
    Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);
        searchResults = findViewById(R.id.searchList);
        DB = new Database(this);
        search = findViewById(R.id.confirmsearch);
        createnew = findViewById(R.id.createnewitem);
        deletelater = findViewById(R.id.deletelater);

        deletelater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchItemActivity.this, AddItemActivity.class);
                SearchItemActivity.this.startActivity(i);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItems = findViewById(R.id.searchItems);
                String searchtxt = searchItems.getText().toString();
                ArrayList<String> searchList = new ArrayList<>();
                if (searchtxt.equals(""))
                    Toast.makeText(SearchItemActivity.this, "Please enter an input for search.", Toast.LENGTH_SHORT).show();
                else {
                    Cursor cursor = DB.searchList(searchtxt);
                    while (cursor.moveToNext()) {
                        searchList.add(cursor.getString(1));
                        ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, searchList);
                        searchResults.setAdapter(adapter);
                    }
                }
            }
        });

        createnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SearchItemActivity.this, CreateItemActivity.class);
                SearchItemActivity.this.startActivity(i);
            }
        });
    }
}
