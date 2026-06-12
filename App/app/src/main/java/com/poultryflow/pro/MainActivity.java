package com.poultryflow.pro;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView tvTotalEggs, tvFeedUsed, tvLayingRate, tvMortality, tvNetProfit;
    private TextView tvEggRevenue, tvChickenRevenue, tvTotalRevenue, tvTotalExpenses;
    private MaterialButton btnSelectMonth;
    private final Calendar selectedMonth = Calendar.getInstance();
    private final SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        tvTotalEggs = findViewById(R.id.tvTotalEggs);
        tvFeedUsed = findViewById(R.id.tvFeedUsed);
        tvLayingRate = findViewById(R.id.tvLayingRate);
        tvMortality = findViewById(R.id.tvMortality);
        tvNetProfit = findViewById(R.id.tvNetProfit);
        
        tvEggRevenue = findViewById(R.id.tvEggRevenue);
        tvChickenRevenue = findViewById(R.id.tvChickenRevenue);
        tvTotalRevenue = findViewById(R.id.tvTotalRevenue);
        tvTotalExpenses = findViewById(R.id.tvTotalExpenses);
        
        btnSelectMonth = findViewById(R.id.btnSelectMonth);

        CardView cardProduction = findViewById(R.id.cardProduction);
        CardView cardEggSales = findViewById(R.id.cardEggSales);
        CardView cardChickenSales = findViewById(R.id.cardChickenSales);
        CardView cardExpenses = findViewById(R.id.cardExpenses);
        CardView cardSettings = findViewById(R.id.cardSettings);

        // Setup toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("PoultryFlow Pro");
        }

        // Set initial values
        updateDashboard(0, 0, 0, 0, 0, 0, 0, 0, 0);
        btnSelectMonth.setText(monthFormat.format(selectedMonth.getTime()));

        // Set button click listeners
        btnSelectMonth.setOnClickListener(v -> showMonthPicker());

        // Set navigation click listeners
        cardProduction.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, DailyProductionActivity.class)));
        cardEggSales.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EggSalesActivity.class)));
        cardChickenSales.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ChickenSalesActivity.class)));
        cardExpenses.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ExpensesActivity.class)));
        cardSettings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, FlockSettingsActivity.class)));
    }

    private void updateDashboard(int totalEggs, double feedUsed, double layingRate, int mortality, 
                                 double eggRev, double chickenRev, double totalRev, double totalExp, double netProfit) {
        tvTotalEggs.setText(String.valueOf(totalEggs));
        tvFeedUsed.setText(String.format(Locale.getDefault(), "%.1f", feedUsed));
        tvLayingRate.setText(String.format(Locale.getDefault(), "%.1f%%", layingRate));
        tvMortality.setText(String.valueOf(mortality));
        
        tvEggRevenue.setText(String.format(Locale.getDefault(), "KES %.0f", eggRev));
        tvChickenRevenue.setText(String.format(Locale.getDefault(), "KES %.0f", chickenRev));
        tvTotalRevenue.setText(String.format(Locale.getDefault(), "KES %.0f", totalRev));
        tvTotalExpenses.setText(String.format(Locale.getDefault(), "KES %.0f", totalExp));
        
        tvNetProfit.setText(String.format(Locale.getDefault(), "KES %.0f", netProfit));
    }

    private void showMonthPicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                (view, year, month, dayOfMonth) -> {
                    selectedMonth.set(Calendar.YEAR, year);
                    selectedMonth.set(Calendar.MONTH, month);
                    btnSelectMonth.setText(monthFormat.format(selectedMonth.getTime()));
                    // Here you would typically trigger a data reload for the selected month
                    Toast.makeText(this, "Selected: " + monthFormat.format(selectedMonth.getTime()), Toast.LENGTH_SHORT).show();
                },
                selectedMonth.get(Calendar.YEAR),
                selectedMonth.get(Calendar.MONTH),
                selectedMonth.get(Calendar.DAY_OF_MONTH)
        );

        int daySpinnerId = getResources().getIdentifier("day", "id", "android");
        if (daySpinnerId != 0) {
            View daySpinner = datePickerDialog.getDatePicker().findViewById(daySpinnerId);
            if (daySpinner != null) {
                daySpinner.setVisibility(View.GONE);
            }
        }
        
        datePickerDialog.setTitle("Select Month");
        Window window = datePickerDialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }
        datePickerDialog.show();
    }
}
