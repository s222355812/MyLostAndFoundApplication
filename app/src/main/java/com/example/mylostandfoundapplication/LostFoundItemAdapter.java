package com.example.mylostandfoundapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class LostFoundItemAdapter extends RecyclerView.Adapter<LostFoundItemAdapter.ViewHolder> {
    private List<LostFoundItem> itemList;
    private Context context;

    //    public LostFoundItemAdapter() {
//        itemList = getAllLostFoundItemsFromDatabase(); // Retrieve the list of items from the database
//    }
    public LostFoundItemAdapter(Context context, List<LostFoundItem> itemList) {
        this.context = context;
        this.itemList = getAllLostFoundItemsFromDatabase();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lost_found, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LostFoundItem item = itemList.get(position);

        holder.textName.setText(item.getName());
        holder.textDescription.setText(item.getDescription());
        holder.textDate.setText(item.getDate());
        holder.textLocation.setText(item.getLocation());

        holder.buttonRemove.setOnClickListener(v -> {
            removeItemFromDatabase(item.getId());
            removeItemFromList(position);
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView textDescription;
        TextView textDate;
        TextView textLocation;
        Button buttonRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.textName);
            textDescription = itemView.findViewById(R.id.textDescription);
            textDate = itemView.findViewById(R.id.textDate);
            textLocation = itemView.findViewById(R.id.textLocation);
            buttonRemove = itemView.findViewById(R.id.buttonRemove);
        }
    }

    private List<LostFoundItem> getAllLostFoundItemsFromDatabase() {
        // Retrieve the list of items from the database using the getAllLostFoundItems() method from LostFoundItemDAO
        LostFoundItemDAO itemDAO = new LostFoundItemDAO(context.getApplicationContext());
        return itemDAO.getAllLostFoundItems();
    }


    private void removeItemFromDatabase(int itemId) {
        // Remove the item from the database using the deleteLostFoundItem() method from LostFoundItemDAO
        LostFoundItemDAO itemDAO = new LostFoundItemDAO(context);
        itemDAO.deleteLostFoundItem(itemId);
    }

    private void removeItemFromList(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }
}