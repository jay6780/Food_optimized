package com.example.foodtoqu;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class FoodDiaryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FoodDiaryAdapter adapter;
    private List<Object> diaryEntries;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_diary);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        diaryEntries = new ArrayList<>();

        // Initialize Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // Initialize the adapter with the list of diary entries
        adapter = new FoodDiaryAdapter(this, diaryEntries);
        recyclerView.setAdapter(adapter);

        // Check if a user is authenticated
        if (currentUser != null) {
            DatabaseReference diaryRef = FirebaseDatabase.getInstance().getReference("diary");

            diaryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    diaryEntries.clear();

                    for (DataSnapshot mealTypeSnapshot : dataSnapshot.getChildren()) {
                        String mealType = mealTypeSnapshot.getKey();
                        diaryEntries.add(mealType); // Add section header for the meal type

                        for (DataSnapshot userSnapshot : mealTypeSnapshot.getChildren()) {
                            if (userSnapshot.getKey().equals(currentUser.getUid())) {
                                for (DataSnapshot foodNameSnapshot : userSnapshot.getChildren()) {
                                    String foodName = foodNameSnapshot.getKey();
                                    diaryEntries.add(foodName); // Add food name as a sub-section header

                                    for (DataSnapshot entrySnapshot : foodNameSnapshot.getChildren()) {
                                        FoodItem2 foodItem = entrySnapshot.getValue(FoodItem2.class);

                                        if (foodItem != null) {
                                            diaryEntries.add(foodItem); // Add food items under the corresponding food name
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Notify the adapter that data has changed
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            });
        }
    }
}
