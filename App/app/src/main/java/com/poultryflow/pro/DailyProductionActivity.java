package com.poultryflow.pro;

import android.os.Bundle;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.poultryflow.pro.adapters.DailyRecordAdapter;
import com.poultryflow.pro.models.DailyRecord;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyProductionActivity extends AppCompatActivity {

    private TextInputEditText etDate, etActiveBirds, etEggsCollected, etDamagedEggs, etFeedKg, etMortality;
    private MaterialButton btnSave;
    private ProgressBar progressBar;
    private DatabaseReference databaseRef;
    private String currentUserId;
    private List<DailyRecord> recordList;
    private DailyRecordAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_production);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = auth.getCurrentUser();
        
        if (user == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        currentUserId = user.getUid();

        initViews();
        setupRecyclerView();
        loadRecords();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etDate.setText(sdf.format(new Date()));

        btnSave.setOnClickListener(v -> saveRecord());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        etDate = findViewById(R.id.etDate);
        etActiveBirds = findViewById(R.id.etActiveBirds);
        etEggsCollected = findViewById(R.id.etEggsCollected);
        etDamagedEggs = findViewById(R.id.etDamagedEggs);
        etFeedKg = findViewById(R.id.etFeedKg);
        etMortality = findViewById(R.id.etMortality);
        btnSave = findViewById(R.id.btnSave);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recordList = new ArrayList<>();
        
        adapter = new DailyRecordAdapter(recordList, record -> databaseRef.child("users").child(currentUserId)
                .child("dailyRecords").child(record.getId()).removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(DailyProductionActivity.this, "Record deleted", Toast.LENGTH_SHORT).show()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupRecyclerView() {
        // Already initialized in initViews to avoid warning about field being local
    }

    private void loadRecords() {
        progressBar.setVisibility(View.VISIBLE);
        databaseRef.child("users").child(currentUserId).child("dailyRecords")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        recordList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            DailyRecord record = data.getValue(DailyRecord.class);
                            if (record != null) {
                                record.setId(data.getKey());
                                recordList.add(record);
                            }
                        }
                        recordList.sort((a, b) -> b.getDate().compareTo(a.getDate()));
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(DailyProductionActivity.this, "Error loading records", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveRecord() {
        Editable dateText = etDate.getText();
        Editable birdsText = etActiveBirds.getText();
        Editable eggsText = etEggsCollected.getText();
        Editable damagedText = etDamagedEggs.getText();
        Editable feedText = etFeedKg.getText();
        Editable mortalityText = etMortality.getText();

        if (dateText == null || birdsText == null || eggsText == null || feedText == null) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = dateText.toString().trim();
        String birdsStr = birdsText.toString().trim();
        String eggsStr = eggsText.toString().trim();
        String damagedStr = damagedText != null ? damagedText.toString().trim() : "0";
        String feedStr = feedText.toString().trim();
        String mortalityStr = mortalityText != null ? mortalityText.toString().trim() : "0";

        if (date.isEmpty() || birdsStr.isEmpty() || eggsStr.isEmpty() || feedStr.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int birds = Integer.parseInt(birdsStr);
            int eggs = Integer.parseInt(eggsStr);
            int damaged = damagedStr.isEmpty() ? 0 : Integer.parseInt(damagedStr);
            double feed = Double.parseDouble(feedStr);
            int mortality = mortalityStr.isEmpty() ? 0 : Integer.parseInt(mortalityStr);

            String key = databaseRef.child("users").child(currentUserId).child("dailyRecords").push().getKey();
            if (key == null) return;

            DailyRecord record = new DailyRecord(key, date, birds, eggs, damaged, feed, mortality);

            progressBar.setVisibility(View.VISIBLE);
            btnSave.setEnabled(false);

            databaseRef.child("users").child(currentUserId).child("dailyRecords").child(key).setValue(record)
                    .addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);
                        btnSave.setEnabled(true);
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Record saved successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error saving record", Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid number format", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
