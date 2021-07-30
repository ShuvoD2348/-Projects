package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<String> lists;

    Button btnAddList;
    EditText etListName;
    RecyclerView rvLists;
    ListsAdapter listsAdapter;
    Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnAddList = findViewById(R.id.btnAddList);
        etListName = findViewById(R.id.etListName);
        rvLists = findViewById(R.id.rvLists);
        DB = new Database(this);

        lists = new LinkedList<>();


        Cursor res = DB.getListNames();
        if (res.getCount() == 0) {
            Toast.makeText(MainActivity.this, "No Lists Exists", Toast.LENGTH_SHORT).show();
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            lists.add(res.getString(1));
        }

        ListsAdapter.OnLongClickListener onLongClickListener = new ListsAdapter.OnLongClickListener() {
            @Override
            public void onListLongClicked(int position) {
                String listName = lists.get(position);
                lists.remove(position);
                listsAdapter.notifyItemRemoved(position);

            }
        };

        listsAdapter = new ListsAdapter(this, lists, onLongClickListener, DB);
        rvLists.setAdapter(listsAdapter);
        rvLists.setLayoutManager(new LinearLayoutManager(this));

        btnAddList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newListNameStart = etListName.getText().toString();
                String newListName = capitalizeString(newListNameStart);
                if (newListName.length() == 0) {
                    Toast.makeText(MainActivity.this, "You must input a name", Toast.LENGTH_SHORT).show();
                } else {
                    DB.insertListName(newListName);
                    lists.add(newListName);
                    listsAdapter.notifyItemInserted(lists.size() - 1);
                    etListName.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
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