package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class CreateItemActivity extends AppCompatActivity {
    EditText name;
    Button insert, view;
    Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_item);

        String listName = getIntent().getStringExtra("listName");
        name = findViewById(R.id.name);
        insert = findViewById(R.id.btnInsert);
        DB = new Database(this);

        Spinner spinnerType = findViewById(R.id.spinnerType);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter1);

        Spinner spinnerQuantype = findViewById(R.id.spinnerQuantype);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.quantity_types, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerQuantype.setAdapter(adapter2);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nametxt = capitalizeString(name.getText().toString());

                boolean itemExists = DB.doesItemExist(nametxt);

                if (itemExists) {
                    Toast.makeText(CreateItemActivity.this, "This item exists already!", Toast.LENGTH_SHORT).show();
                } else {
                    if (nametxt.equals(""))
                        Toast.makeText(CreateItemActivity.this, "Your item needs a name.", Toast.LENGTH_SHORT).show();
                    else {
                        int typePK = 0;
                        String caseStr = spinnerType.getSelectedItem().toString();
                        switch (caseStr) {
                            case "Fruit":
                                typePK = 1;
                                break;
                            case "Vegetable":
                                typePK = 2;
                                break;
                            case "Meat":
                                typePK = 3;
                                break;
                            case "Cereal":
                                typePK = 4;
                                break;
                            case "Drink":
                                typePK = 5;
                                break;
                            case "Frozen Food":
                                typePK = 6;
                                break;
                            case "Snack":
                                typePK = 7;
                                break;
                            case "Canned Food":
                                typePK = 8;
                                break;
                        }

                        String quantxt = spinnerQuantype.getSelectedItem().toString();
                        if (quantxt.equals("(None)")) quantxt = "";
                        Boolean checkinsertdata = DB.insertdata(nametxt, typePK, quantxt);
                        name.setText("");

                        Intent i = new Intent(CreateItemActivity.this, AddItemActivity.class);
                        i.putExtra("listName", listName);
                        CreateItemActivity.this.startActivity(i);

                    }
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (CreateItemActivity.this.getCurrentFocus() != null)
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
