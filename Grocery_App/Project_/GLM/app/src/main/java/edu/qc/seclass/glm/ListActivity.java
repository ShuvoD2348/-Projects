package edu.qc.seclass.glm;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    TextView tvListName;
    Button btnAddItem;
    Button btnAddByItemType;
    Button btnClearChecks;
    RecyclerView rvItems;
    ItemAdapter itemAdapter;
    Database DB;

    List<Item> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // catches List name
        String listName = getIntent().getStringExtra("listName");
        tvListName = findViewById(R.id.tvListName);
        tvListName.setText(listName);
        DB = new Database(this);

        // create list
        items = new LinkedList<>();

        Cursor res = DB.getItemsInList(listName);
        if (res.getCount() == 0) {
            Toast.makeText(ListActivity.this, "Add some items!", Toast.LENGTH_SHORT).show();
            //return;
        }
        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            //    public Item(String itemName, int itemQuantity, String itemQuantityType)
            boolean checked = false;
            if (res.getString(8).equals("1")) checked = true;
            Item curr = new Item(res.getString(4), checked, res.getString(1), res.getString(7), res.getString(3));
            items.add(curr);
        }

        rvItems = findViewById(R.id.rvItems);


        ItemAdapter.OnLongClickListener onLongClickListener = new ItemAdapter.OnLongClickListener() {
            @Override
            public void onListLongClicked(int position) {
                int id = Integer.parseInt(items.get(position).getItemId());
                items.remove(position);
                itemAdapter.notifyItemRemoved(position);
                DB.deleteItem(id);
            }
        };
        //Create the adapter
        itemAdapter = new ItemAdapter(this, items, onLongClickListener, listName, DB);

        //Set the adapter on the recycler view
        rvItems.setAdapter(itemAdapter);

        //Set a layout manager on the recycler view
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAddItem = findViewById(R.id.btnAddItem);
        btnAddByItemType = findViewById(R.id.btnAddByItemType);
        btnClearChecks = findViewById(R.id.btnClearChecks);

        tvListName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListActivity.this, EditListNameActivity.class);
                i.putExtra("listName", listName);
                ListActivity.this.startActivity(i);
            }
        });

        btnAddByItemType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListActivity.this, AddItemByTypeActivity.class);
                i.putExtra("listName", listName);
                ListActivity.this.startActivity(i);
            }
        });

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListActivity.this, AddItemActivity.class);
                i.putExtra("listName", listName);
                ListActivity.this.startActivity(i);
            }
        });

        btnClearChecks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Item i: items) {
                    i.checked = false;
                }
                itemAdapter.notifyDataSetChanged();
            }
        });
    }
}