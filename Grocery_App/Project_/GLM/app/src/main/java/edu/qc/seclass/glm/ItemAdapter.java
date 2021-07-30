package edu.qc.seclass.glm;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    public interface OnLongClickListener {
        void onListLongClicked(int position);
    }

    Context context;
    List<Item> items;
    ItemAdapter.OnLongClickListener longClickListener;
    String listName;
    Database DB;



    public ItemAdapter(Context context, List<Item> items, ItemAdapter.OnLongClickListener longClickListener, String listName, Database DB) {
        this.context = context;
        this.items = items;
        this.longClickListener = longClickListener;
        this.listName = listName;
        this.DB = DB;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox;
        TextView tvItemName;
        TextView tvQuantity;
        TextView tvQuantityType;
        RelativeLayout llItemBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkBox);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            tvQuantityType = itemView.findViewById(R.id.tvQuantityType);
            llItemBox = itemView.findViewById(R.id.llitemBox);
        }

        public void bind(Item item) {
            checkBox.setChecked(item.getChecked());
            tvItemName.setText(item.getItemName());
            tvQuantity.setText(item.getItemQuantity());
            tvQuantityType.setText(item.getItemQuantityType());


            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int itemId = Integer.parseInt(item.getItemId());
                    if (checkBox.isChecked()) {
                        DB.checkOffItem(itemId);
                    } else {
                        DB.checkOnItem(itemId);
                    }
                }
            });

            llItemBox.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "Item deleted!", Toast.LENGTH_SHORT).show();
                    longClickListener.onListLongClicked(getAdapterPosition());

                    return true;
                }
            });

            llItemBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, EditItemActivity.class);
                    i.putExtra("itemName", item.getItemName());
                    i.putExtra("itemQuantity", item.getItemQuantity());
                    i.putExtra("itemID", item.getItemId());
                    i.putExtra("listName", listName);
                    context.startActivity(i);
                }
            });
        }
    }
}
