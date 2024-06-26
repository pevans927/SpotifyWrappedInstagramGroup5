package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotifywrappedinstagramgroup5.databinding.FragmentHomepageBinding;
import com.example.spotifywrappedinstagramgroup5.databinding.FragmentPostpageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PostPage extends AppCompatActivity {
    FragmentHomepageBinding binding; // Corrected binding class
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentHomepageBinding.inflate(getLayoutInflater()); // Corrected binding initialization
        setContentView(binding.getRoot());

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFilter = parent.getItemAtPosition(position).toString();
                if (selectedFilter.equals("Filter by Likes")) {
                    WrappedModel.loadData(mStore, auth, "likes", new DataCallback() {
                        @Override
                        public void onCallback(List<WrappedModel> wrappedModelList) {
                            WrappedModelAdapter adapter = new WrappedModelAdapter(PostPage.this, wrappedModelList);
                            binding.recyclerView.setAdapter(adapter);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(PostPage.this));
                        }

                        @Override
                        public void onError(Exception e) {
                            // Handle error
                        }
                    });
                } else if (selectedFilter.equals("Filter by Comments")) {
                    WrappedModel.loadData(mStore, auth, "comments", new DataCallback() {
                        @Override
                        public void onCallback(List<WrappedModel> wrappedModelList) {
                            WrappedModelAdapter adapter = new WrappedModelAdapter(PostPage.this, wrappedModelList);
                            binding.recyclerView.setAdapter(adapter);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(PostPage.this));
                        }

                        @Override
                        public void onError(Exception e) {
                            // Handle error
                        }
                    });
                } else {
                    WrappedModel.loadData(mStore, auth, "all", new DataCallback() {
                        @Override
                        public void onCallback(List<WrappedModel> wrappedModelList) {
                            WrappedModelAdapter adapter = new WrappedModelAdapter(PostPage.this, wrappedModelList);
                            binding.recyclerView.setAdapter(adapter);
                            binding.recyclerView.setLayoutManager(new LinearLayoutManager(PostPage.this));
                        }

                        @Override
                        public void onError(Exception e) {
                            // Handle error
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });





        // Set up bottom navigation menu
        BottomNavigationView bottomMenu = binding.bottomMenu; // Corrected reference to bottom menu
        bottomMenu.setBackground(null); // Set background to null if needed
        binding.bottomMenu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home_button) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.search_button) {
                Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.profile_button) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            }
            return true;
        });

        binding.addPostButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), PublishPage.class);
            startActivity(intent);
        });
    }
}