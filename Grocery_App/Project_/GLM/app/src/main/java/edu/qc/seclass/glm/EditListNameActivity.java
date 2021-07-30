package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditListNameActivity extends AppCompatActivity {

    EditText etEditListName;
    Button btnUpdateListName;
    Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_list_name);

        etEditListName = findViewById(R.id.etEditListName);
        btnUpdateListName = findViewById(R.id.btnUpdateListName);
        DB = new Database(this);
        String listName = getIntent().getStringExtra("listName");

        etEditListName.setText(listName);

        btnUpdateListName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newListName = capitalizeString(etEditListName.getText().toString());
                if (newListName.length() == 0) {
                    Toast.makeText(EditListNameActivity.this, "You must give the list a name.", Toast.LENGTH_SHORT).show();
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } else {
                    DB.updateListName(listName, newListName);
                    Intent i = new Intent(EditListNameActivity.this, ListActivity.class);
                    i.putExtra("listName", newListName);
                    EditListNameActivity.this.startActivity(i);
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