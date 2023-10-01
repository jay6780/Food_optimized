package com.example.foodtoqu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodDiaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_SECTION_HEADER = 0;
    private static final int VIEW_TYPE_FOOD_ITEM = 1;

    private List<Object> items; // Use a list of Objects to handle both section headers and food items
    private Context context;

    public FoodDiaryAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);

        if (viewType == VIEW_TYPE_SECTION_HEADER) {
            view = inflater.inflate(R.layout.item_section_header, parent, false);
            return new SectionHeaderViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.item_diary_entry, parent, false);
            return new FoodItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = items.get(position);

        if (holder instanceof SectionHeaderViewHolder) {
            SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) holder;
            String sectionTitle = (String) item;
            sectionHeaderViewHolder.sectionTitleTextView.setText(sectionTitle);
        } else if (holder instanceof FoodItemViewHolder) {
            FoodItemViewHolder foodItemViewHolder = (FoodItemViewHolder) holder;
            FoodItem2 foodItem = (FoodItem2) item;

            // Bind data to the ViewHolder's views
            foodItemViewHolder.foodNameTextView.setText(foodItem.getFoodName());

            // Load the image using Picasso
            Picasso.get()
                    .load(foodItem.getImageUrl())
                    .placeholder(R.drawable.ic_baseline_local_pizza_24)
                    .error(R.drawable.ic_baseline_local_pizza_24)
                    .into(foodItemViewHolder.imageView);
            // Add other bindings for other data here...
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (item instanceof String) {
            return VIEW_TYPE_SECTION_HEADER;
        } else if (item instanceof FoodItem2) {
            return VIEW_TYPE_FOOD_ITEM;
        }
        return super.getItemViewType(position);
    }

    // ViewHolder for section header
    public static class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView sectionTitleTextView;

        public SectionHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionTitleTextView = itemView.findViewById(R.id.text_section_title);
        }
    }

    // ViewHolder for food item
    public static class FoodItemViewHolder extends RecyclerView.ViewHolder {
        TextView foodNameTextView;
        ImageView imageView; // ImageView for the food item image

        public FoodItemViewHolder(@NonNull View itemView) {
            super(itemView);
            foodNameTextView = itemView.findViewById(R.id.text_food_name);
            imageView = itemView.findViewById(R.id.image_view); // Initialize the ImageView
        }
    }
}
