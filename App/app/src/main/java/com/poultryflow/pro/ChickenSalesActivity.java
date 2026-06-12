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
import com.poultryflow.pro.adapters.ChickenSaleAdapter;
import com.poultryflow.pro.models.ChickenSale;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChickenSalesActivity extends AppCompatActivity {

    private TextInputEditText etDate, etQuantity, etUnitPrice, etWeight, etCustomer;
    private Spinner spChickenType, spPaymentStatus;
    private MaterialButton btnSave;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference databaseRef;
    private String currentUserId;
    private List<ChickenSale> saleList;
    private ChickenSaleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chicken_sales);

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
        loadSales();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        etDate.setText(sdf.format(new Date()));

        btnSave.setOnClickListener(v -> saveSale());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etDate = findViewById(R.id.etDate);
        spChickenType = findViewById(R.id.spChickenType);
        etQuantity = findViewById(R.id.etQuantity);
        etUnitPrice = findViewById(R.id.etUnitPrice);
        etWeight = findViewById(R.id.etWeight);
        etCustomer = findViewById(R.id.etCustomer);
        spPaymentStatus = findViewById(R.id.spPaymentStatus);
        btnSave = findViewById(R.id.btnSave);
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        saleList = new ArrayList<>();
    }

    private void setupRecyclerView() {
        adapter = new ChickenSaleAdapter(saleList, sale -> {
            databaseRef.child("users").child(currentUserId)
                    .child("chickenSales").child(sale.getId()).removeValue()
                    .addOnSuccessListener(aVoid -> Toast.makeText(this, "Sale deleted", Toast.LENGTH_SHORT).show());
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void loadSales() {
        progressBar.setVisibility(View.VISIBLE);
        databaseRef.child("users").child(currentUserId).child("chickenSales")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        saleList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            ChickenSale sale = data.getValue(ChickenSale.class);
                            if (sale != null) {
                                sale.setId(data.getKey());
                                saleList.add(sale);
                            }
                        }
                        saleList.sort((a, b) -> b.getDate().compareTo(a.getDate()));
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ChickenSalesActivity.this, "Error loading sales", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveSale() {
        String date = etDate.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String priceStr = etUnitPrice.getText().toString().trim();

        if (date.isEmpty() || quantityStr.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int quantity = Integer.parseInt(quantityStr);
        double price = Double.parseDouble(priceStr);
        double total = quantity * price;
        double weight = etWeight.getText().toString().isEmpty() ? 0 : Double.parseDouble(etWeight.getText().toString());
        String chickenType = spChickenType.getSelectedItem().toString();
        String customer = etCustomer.getText().toString().trim();
        String paymentStatus = spPaymentStatus.getSelectedItem().toString();

        String key = databaseRef.child("users").child(currentUserId).child("chickenSales").push().getKey();
        ChickenSale sale = new ChickenSale(key, date, chickenType, quantity, price, weight, total, customer, paymentStatus);

        progressBar.setVisibility(View.VISIBLE);
        btnSave.setEnabled(false);

        databaseRef.child("users").child(currentUserId).child("chickenSales").child(key).setValue(sale)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    btnSave.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Sale recorded successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error saving sale", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}