package edu.qc.seclass.glm;

import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.List;

public class ItemTypeAdapter extends BaseExpandableListAdapter {

    List<String> itemTypeList;
    HashMap<String, List<String>> itemChild;
    String listName;

    public ItemTypeAdapter(List<String> itemTypeList, HashMap<String, List<String>> itemChild, String listName) {
        this.itemTypeList = itemTypeList;
        this.itemChild = itemChild;
        this.listName = listName;
    }

    @Override
    public int getGroupCount() {
        return itemTypeList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemChild.get(itemTypeList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // returns a group item
        return itemTypeList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return itemChild.get(itemTypeList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // Initialize view
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_expandable_list_item_1, parent, false);

        TextView tvItemType = convertView.findViewById(android.R.id.text1);
        // Grab value of string
        String currentItemGroup = String.valueOf(getGroup(groupPosition));
        // Set text on text view
        tvItemType.setText(currentItemGroup);
        tvItemType.setTypeface(null, Typeface.BOLD);
        // return view
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_selectable_list_item, parent, false);

        TextView item = convertView.findViewById(android.R.id.text1);
        String currentItemChild = String.valueOf(getChild(groupPosition, childPosition));
        item.setText(currentItemChild);

        //Set onClickListener
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(parent.getContext(), AddItemActivity.class);
                i.putExtra("itemName",currentItemChild);
                i.putExtra("listName", listName);
                parent.getContext().startActivity(i);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
