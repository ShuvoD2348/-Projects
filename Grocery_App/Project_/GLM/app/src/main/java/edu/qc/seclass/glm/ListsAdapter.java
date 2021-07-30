package edu.qc.seclass.glm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListsAdapter extends RecyclerView.Adapter<ListsAdapter.ViewHolder> {

    public interface OnLongClickListener {
        void onListLongClicked(int position);
    }
    Context context;
    List<String> lists;
    OnLongClickListener longClickListener;
    Database DB;


    public ListsAdapter(Context context, List<String> lists, OnLongClickListener longClickListener, Database DB) {
        this.context = context;
        this.lists = lists;
        this.longClickListener = longClickListener;
        this.DB = DB;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create new view and wrap it in view holder
        View listView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(listView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // find position and bind item to view holder
        String list = lists.get(position);
        holder.bind(list);
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvList = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String list) {

            tvList.setText(list);
            tvList.setTextColor(Color.rgb(147,112,238));
            tvList.setTextSize(25);
            tvList.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "List deleted!", Toast.LENGTH_SHORT).show();
                    Cursor cursor = DB.getListPK(list);
                    int id = cursor.getInt(0);
                    Boolean itemsForListDelete = DB.deleteListItems(id);
                    Boolean wasDeleted = DB.deleteListName(list);
                    longClickListener.onListLongClicked(getAdapterPosition());
                    if (wasDeleted && itemsForListDelete) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            tvList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("[ListAdapter]", " list has been clicked");
                    Intent i = new Intent(context, ListActivity.class);
                    i.putExtra("listName", list);
                    context.startActivity(i);
                }
            });
        }
    }
}
