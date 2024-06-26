package com.example.spotifywrappedinstagramgroup5;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrappedinstagramgroup5.databinding.FragmentPostpageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class HomePage extends AppCompatActivity {
    FragmentPostpageBinding binding; // Corrected binding class
    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseFirestore mStore;

    SpotifyApiManager manager;

    TimeFrame selectedTimeFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentPostpageBinding.inflate(getLayoutInflater()); // Corrected binding initialization
        setContentView(binding.getRoot());

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance();
        mStore = FirebaseFirestore.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();
        DocumentReference docRef = mStore.collection("UserData").document(userId);

        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String token = (String)documentSnapshot.get("Spotify Token");
                String code = (String)documentSnapshot.get("Spotify Code");
                manager = new SpotifyApiManager(HomePage.this, code, token, mStore, auth);
            } else {
                Log.d("Document", "No such document");
            }
        }).addOnFailureListener(e -> Log.d("Document", "Failed with: ", e));

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
            } else if (item.getItemId() == R.id.profile_button) {
                Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                return true;
            }
            return true;
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> test = new HashMap<>();
        test.put("FirstPost", "One");

        EditText editTextContent = binding.editTextContent;

        Spinner spinner = findViewById(R.id.timeframe_picker);

        // Convert enum values to a string array
        TimeFrame[] timeFrames = TimeFrame.values();
        String[] items = new String[timeFrames.length];
        for (int i = 0; i < timeFrames.length; i++) {
            if (timeFrames[i].toString().equals("long_term")) {
                items[i] = "1 year";
            } else if (timeFrames[i].toString().equals("medium_term")) {
                items[i] = "6 months";
            } else if (timeFrames[i].toString().equals("short_term")) {
                items[i] = "1 months";
            } else {
                items[i] = timeFrames[i].toString();
            }
        }

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Optional: Handle spinner item selections
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Handle item selection
                selectedTimeFrame = TimeFrame.values()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTimeFrame = TimeFrame.long_term;
            }
        });
//        Button viewWrapTempButton = findViewById(R.id.view_wrap_temp);
//        viewWrapTempButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Inflate the SpotifyWrappedInitialPage activity
//                Intent intent = new Intent(getApplicationContext(), SpotifyWrappedInitialPage.class);
//                startActivity(intent);
//            }
//        });


        binding.buttonPost.setOnClickListener(view -> {
            // Get the content from EditText
            String content = editTextContent.getText().toString().trim();

            if (!content.isEmpty()) {
                if (manager != null) {
                    try {
                        String newWrappedId = manager.generateWrapped(HomePage.this, 5, selectedTimeFrame, "", content);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
                Toast.makeText(HomePage.this, "Successfully Generated, See in \"My Wraps\" on the profile page", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Handle case where EditText is empty
                Toast.makeText(HomePage.this, "Post content cannot be empty.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}