package com.example.foodtoqu;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
public class User_search2 extends AppCompatActivity {
    RecyclerView userview;
    DatabaseReference databaseReference;
    List<User4> userList;
    List<User4> originalUserList; // Store the original data here
    UserAdapte3 userAdapter;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        userview = findViewById(R.id.userView);
        userList = new ArrayList<>();
        originalUserList = new ArrayList<>(); // Initialize the original data list
        userAdapter = new UserAdapte3(userList, this);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("User");

        // Delete button click listener
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),DiaryListActivity2.class);
                startActivity(i);
                finish();
            }
        });



        userview.setLayoutManager(new LinearLayoutManager(this));
        userview.setAdapter(userAdapter);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                originalUserList.clear();

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser(); // Get the current user

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User4 user = snapshot.getValue(User4.class);

                    // Check if the user is authenticated or if there is no current user
                    if (currentUser == null || !TextUtils.equals(currentUser.getEmail(), user.getEmail())) {
                        userList.add(user);
                        originalUserList.add(user);
                    }
                }

                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });


        searchView = findViewById(R.id.search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter the list by username when the user types in the SearchView
                filterUserListByUsername(newText);
                return true;
            }
        });
    }

    private void filterUserListByUsername(String searchText) {
        if (searchText.isEmpty()) {
            // If the search text is empty, restore the original data
            userAdapter.updateList(originalUserList);
        } else {
            String searchQuery = searchText.toLowerCase();

            List<User4> filteredList = new ArrayList<>();
            for (User4 user : userList) {
                String username = user.getName() != null ? user.getName().toLowerCase() : "";

                if (username.contains(searchQuery)) {
                    filteredList.add(user);
                }
            }

            userAdapter.updateList(filteredList);
        }
    }


    @Override
    public void onBackPressed() {
            Intent i = new Intent(getApplicationContext(),DiaryListActivity2.class);
            startActivity(i);
            finish();
            super.onBackPressed();
        }
    }


