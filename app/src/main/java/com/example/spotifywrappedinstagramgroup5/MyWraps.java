package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.spotifywrappedinstagramgroup5.databinding.FragmentHomepageBinding;
import com.example.spotifywrappedinstagramgroup5.databinding.FragmentMywrapsBinding;
import com.example.spotifywrappedinstagramgroup5.databinding.FragmentPostpageBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyWraps extends AppCompatActivity {
    FragmentMywrapsBinding binding; // Corrected binding class
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore mStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMywrapsBinding.inflate(getLayoutInflater()); // Corrected binding initialization
        setContentView(binding.getRoot());

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();

        List<WrappedModel> data = WrappedModel.loadMyData(mStore, auth, new DataCallback() {
            @Override
            public void onCallback(List<WrappedModel> wrappedModelList) {
                WrappedModelAdapter adapter = new WrappedModelAdapter(MyWraps.this, wrappedModelList);
                binding.recyclerView.setAdapter(adapter);
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(MyWraps.this));
            }

            @Override
            public void onError(Exception e) {
                // Handle error
            }
        });

        // Set up bottom navigation menu
        BottomNavigationView bottomMenu = binding.bottomMenu; // Corrected reference to bottom menu
        bottomMenu.setBackground(null); // Set background to null if needed
        binding.bottomMenu.setBackground(null);
        binding.bottomMenu.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.post_button) {
                Intent intent = new Intent(getApplicationContext(), PostPage.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.search_button) {
                Intent intent = new Intent(getApplicationContext(), SearchPage.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            } else if (item.getItemId() == R.id.home_button) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            }
            return true;
        });
    }
}