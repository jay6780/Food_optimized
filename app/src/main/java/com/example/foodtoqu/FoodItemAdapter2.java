package com.example.foodtoqu;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodItemAdapter2 extends RecyclerView.Adapter<FoodItemAdapter2.ViewHolder> {
    private List<FoodItem2> foodItemList;
    private Context context;

    public FoodItemAdapter2(List<FoodItem2> foodItemList, Context context) {
        this.foodItemList = foodItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodItem2 foodItem = foodItemList.get(position);

        // Bind food item data to ViewHolder views
        holder.foodNameTextView.setText(foodItem.getName());

        // Load the food item image using Picasso
        Picasso.get().load(foodItem.getImage()).into(holder.foodItemImageView);

        // Set an OnClickListener to handle item clicks
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an Intent to start the new activity
                Intent intent = new Intent(context,DiaryListActivity3.class);

                // Pass the food item data to the new activity
                intent.putExtra("uid", foodItem.getUid());
                // Replace "foodItemId" and foodItem.getUid() with your actual data

                // Start the new activity
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItemList.size();
    }

    public void updateList(List<FoodItem2> newList) {
        foodItemList.clear();
        foodItemList.addAll(newList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView foodItemImageView;
        TextView foodNameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            foodItemImageView = itemView.findViewById(R.id.userImageView);
            foodNameTextView = itemView.findViewById(R.id.usernameTextView);
        }
    }
}
