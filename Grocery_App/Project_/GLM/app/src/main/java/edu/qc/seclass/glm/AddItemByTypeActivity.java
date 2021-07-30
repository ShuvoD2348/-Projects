package edu.qc.seclass.glm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AddItemByTypeActivity extends AppCompatActivity {

    ExpandableListView expandableListView;
    Database db;
    List<String> itemTypeList = new LinkedList<>();
    HashMap<String, List<String>> itemChild = new HashMap<>();


    ItemTypeAdapter itemTypeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_by_type);
        db = new Database(this);
        expandableListView = findViewById(R.id.expItemTypeView);
        String listName = getIntent().getStringExtra("listName");

        for (int i = 0; i < db.getList().getCount(); i++) {
            itemTypeList.add(db.getType(i + 1).getString(0));
            List<String> currList = new LinkedList<>();

            for (int j = 0; j < db.getItem(i + 1).getCount(); j++)
                currList.add(db.getOffset(i + 1, j).getString(0));

            itemChild.put(itemTypeList.get(i), currList);
        }

        // Set up adapter
        itemTypeAdapter = new ItemTypeAdapter(itemTypeList, itemChild, listName);
        expandableListView.setAdapter(itemTypeAdapter);
    }
}