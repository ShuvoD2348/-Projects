package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class EditItemActivity extends AppCompatActivity {

    TextView tvItemNameEditQuantity;
    EditText etEditQuantity;
    Button btnUpdateQuantity;
    Database DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        DB = new Database(this);
        // Retrieves name from intent to display
        String itemName = getIntent().getStringExtra("itemName");
        String itemQuantity = getIntent().getStringExtra("itemQuantity");
        String itemId = getIntent().getStringExtra("itemID");
        String list = getIntent().getStringExtra("listName");

        tvItemNameEditQuantity = findViewById(R.id.tvItemNameEditQuantity);
        tvItemNameEditQuantity.setText(itemName);

        etEditQuantity = findViewById(R.id.etEditQuantity);
        btnUpdateQuantity = findViewById(R.id.btnUpdateQuantity);

        btnUpdateQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = Integer.parseInt(itemId);
                String newQuantity = etEditQuantity.getText().toString();
                if (newQuantity.length() == 0) {
                    Toast.makeText(EditItemActivity.this, "You must input a quantity", Toast.LENGTH_SHORT).show();
                } else if (newQuantity.length() > 10) {
                    Toast.makeText(EditItemActivity.this, "The quantity is too large", Toast.LENGTH_SHORT).show();
                } else {
                    int newIntQuantity = Integer.parseInt(newQuantity);
                    DB.changeQuantityForItem(id, newIntQuantity);
                    Intent i = new Intent(EditItemActivity.this, ListActivity.class);
                    i.putExtra("listName", list);
                    EditItemActivity.this.startActivity(i);
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });
    }
}