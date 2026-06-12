package com.poultryflow.pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class FlockSettingsActivity extends AppCompatActivity {

    private TextInputEditText etFlockName, etTotalBirds;
    private Spinner spBirdType, spHousingType;
    private MaterialButton btnSave;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flock_settings);

        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        currentUserId = auth.getCurrentUser().getUid();

        initViews();
        loadSettings();

        btnSave.setOnClickListener(v -> saveSettings());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etFlockName = findViewById(R.id.etFlockName);
        spBirdType = findViewById(R.id.spBirdType);
        etTotalBirds = findViewById(R.id.etTotalBirds);
        spHousingType = findViewById(R.id.spHousingType);
        btnSave = findViewById(R.id.btnSave);
        progressBar = findViewById(R.id.progressBar);
    }

    private void loadSettings() {
        progressBar.setVisibility(View.VISIBLE);
        databaseRef.child("users").child(currentUserId).child("config").child("flockSettings")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            etFlockName.setText(snapshot.child("flockName").getValue(String.class));
                            etTotalBirds.setText(String.valueOf(snapshot.child("totalBirds").getValue(Long.class)));

                            String birdType = snapshot.child("birdType").getValue(String.class);
                            if (birdType != null) {
                                for (int i = 0; i < spBirdType.getCount(); i++) {
                                    if (spBirdType.getItemAtPosition(i).toString().equals(birdType)) {
                                        spBirdType.setSelection(i);
                                        break;
                                    }
                                }
                            }

                            String housingType = snapshot.child("housingType").getValue(String.class);
                            if (housingType != null) {
                                for (int i = 0; i < spHousingType.getCount(); i++) {
                                    if (spHousingType.getItemAtPosition(i).toString().equals(housingType)) {
                                        spHousingType.setSelection(i);
                                        break;
                                    }
                                }
                            }
                        }
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(FlockSettingsActivity.this, "Error loading settings", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveSettings() {
        String flockName = etFlockName.getText().toString().trim();
        String totalBirdsStr = etTotalBirds.getText().toString().trim();

        if (flockName.isEmpty() || totalBirdsStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int totalBirds = Integer.parseInt(totalBirdsStr);
        String birdType = spBirdType.getSelectedItem().toString();
        String housingType = spHousingType.getSelectedItem().toString();

        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        DatabaseReference ref = databaseRef.child("users").child(currentUserId).child("config").child("flockSettings");
        ref.child("flockName").setValue(flockName);
        ref.child("birdType").setValue(birdType);
        ref.child("totalBirds").setValue(totalBirds);
        ref.child("housingType").setValue(housingType);
        ref.child("updatedAt").setValue(System.currentTimeMillis())
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Settings saved successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error saving settings", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}