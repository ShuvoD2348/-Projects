package edu.qc.seclass.glm;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {

    Button btnCreateItem;
    Button btnAddItemToList;
    EditText etItemName;
    EditText etQuantity;
    ListView searchResults;
    Button search;
    Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        String listName = getIntent().getStringExtra("listName");
        String itemName = getIntent().getStringExtra("itemName");
        ArrayList<String> searchList = new ArrayList<>();
        searchResults = findViewById(R.id.searchList);
        search = findViewById(R.id.confirmsearch);
        btnCreateItem = findViewById(R.id.btnCreateItem);
        btnAddItemToList = findViewById(R.id.btnAddItemToList);
        etItemName = findViewById(R.id.et_itemname);
        etQuantity = findViewById(R.id.et_qtmt);
        DB = new Database(this);

        if (itemName != null) etItemName.setText(itemName);


        btnCreateItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddItemActivity.this, CreateItemActivity.class);
                i.putExtra("listName", listName);
                AddItemActivity.this.startActivity(i);
            }
        });

        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etItemName.setText(searchList.get((int) id));
                searchList.clear();
                ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, searchList);
                searchResults.setAdapter(adapter);
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchtxt;
                if (etItemName != null) {
                    searchtxt = etItemName.getText().toString();
                    Cursor cursor = DB.searchList(searchtxt);
                    if (cursor.getCount() == 0) {
                        Toast.makeText(AddItemActivity.this, "Item doesn't exist. Please, create it.", Toast.LENGTH_SHORT).show();
                    }
                    while (cursor.moveToNext()) {
                        searchList.add(cursor.getString(1));
                        ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, searchList);
                        searchResults.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(AddItemActivity.this, "Please enter an input for search.", Toast.LENGTH_SHORT).show();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }
        });

        btnAddItemToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String itemName = capitalizeString(etItemName.getText().toString());

                if (itemName.equals("") || etQuantity.getText().toString().equals(""))
                    Toast.makeText(AddItemActivity.this, "Must input value for all fields.", Toast.LENGTH_SHORT).show();
                else {
                    if (etQuantity.getText().toString().length() > 10) {
                        Toast.makeText(AddItemActivity.this, "The quantity is too large.", Toast.LENGTH_SHORT).show();
                    } else {
                        Cursor resList = DB.getListPK(listName);
                        Boolean itemExists = DB.doesItemExist(itemName);
                        if (itemExists) {
                            Cursor resItem = DB.getItemPK(itemName);

                            int listPK = Integer.parseInt(resList.getString(0));
                            int itemPK = Integer.parseInt(resItem.getString(0));
                            int quantity = Integer.parseInt(etQuantity.getText().toString());
                            Boolean insertedValue = DB.insertItemToList(listPK, itemPK, quantity);


                            Intent i = new Intent(AddItemActivity.this, ListActivity.class);
                            i.putExtra("listName", listName);
                            AddItemActivity.this.startActivity(i);
                        } else {
                            Toast.makeText(AddItemActivity.this, "Item doesn't exist. Please, create it.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (AddItemActivity.this.getCurrentFocus() != null)
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }



    public String capitalizeString(String listName) {
        char[] listNameArray = listName.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < listNameArray.length; i++) {
            char currentLetter = listNameArray[i];
            if (!found && Character.isLetter(currentLetter)) {
                listNameArray[i] = Character.toUpperCase(currentLetter);
                found = true;
            } else if (Character.isWhitespace(currentLetter)){
                found = false;
            }
        }
        return String.valueOf(listNameArray);
    }
}