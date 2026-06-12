package com.poultryflow.pro;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.poultryflow.pro.adapters.ExpenseAdapter;
import com.poultryflow.pro.models.Expense;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpensesActivity extends AppCompatActivity {

    private TextInputEditText etDate, etDescription, etQuantity, etTotalCost;
    private Spinner spCategory;
    private MaterialButton btnSave;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private String currentUserId;
    private List<Expense> expenseList;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);

        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        currentUserId = auth.getCurrentUser().getUid();

        initViews();
        setupRecyclerView();
        loadExpenses();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etDate.setText(sdf.format(new Date()));

        btnSave.setOnClickListener(v -> saveExpense());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etDate = findViewById(R.id.etDate);
        spCategory = findViewById(R.id.spCategory);
        etDescription = findViewById(R.id.etDescription);
        etQuantity = findViewById(R.id.etQuantity);
        etTotalCost = findViewById(R.id.etTotalCost);
        btnSave = findViewById(R.id.btnSave);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        expenseList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new ExpenseAdapter(expenseList, expense -> {
            databaseRef.child("users").child(currentUserId)
                    .child("expensesLog").child(expense.getId()).removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show());
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadExpenses() {
        progressBar.setVisibility(View.VISIBLE);
        databaseRef.child("users").child(currentUserId).child("expensesLog")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        expenseList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Expense expense = data.getValue(Expense.class);
                            if (expense != null) {
                                expense.setId(data.getKey());
                                expenseList.add(expense);
                            }
                        }
                        expenseList.sort((a, b) -> b.getDate().compareTo(a.getDate()));
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ExpensesActivity.this, "Error loading expenses", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveExpense() {
        String date = etDate.getText().toString().trim();
        String costStr = etTotalCost.getText().toString().trim();

        if (date.isEmpty() || costStr.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double cost = Double.parseDouble(costStr);
        String category = spCategory.getSelectedItem().toString();
        String description = etDescription.getText().toString().trim();
        int quantity = etQuantity.getText().toString().isEmpty() ? 1 : Integer.parseInt(etQuantity.getText().toString());

        String key = databaseRef.child("users").child(currentUserId).child("expensesLog").push().getKey();
        Expense expense = new Expense(key, date, category, description, quantity, cost);

        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        databaseRef.child("users").child(currentUserId).child("expensesLog").child(key).setValue(expense)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Expense recorded successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error saving expense", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}