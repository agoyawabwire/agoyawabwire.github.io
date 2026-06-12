package com.poultryflow.pro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.poultryflow.pro.R;
import com.poultryflow.pro.models.DailyRecord;
import java.util.List;
import java.util.Locale;

public class DailyRecordAdapter extends RecyclerView.Adapter<DailyRecordAdapter.ViewHolder> {

    private final List<DailyRecord> records;
    private final OnRecordDeleteListener deleteListener;

    public interface OnRecordDeleteListener {
        void onDelete(DailyRecord record);
    }

    public DailyRecordAdapter(List<DailyRecord> records, OnRecordDeleteListener deleteListener) {
        this.records = records;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_daily_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyRecord record = records.get(position);
        holder.tvDate.setText(record.getDate());
        holder.tvBirds.setText(String.format(Locale.getDefault(), "Birds: %d", record.getActiveBirds()));
        holder.tvEggs.setText(String.format(Locale.getDefault(), "Eggs: %d", record.getEggsCollected()));
        holder.tvDamaged.setText(String.format(Locale.getDefault(), "Damaged: %d", record.getDamagedEggs()));
        holder.tvFeed.setText(String.format(Locale.getDefault(), "Feed: %.1fkg", record.getFeedKg()));
        holder.tvMortality.setText(String.format(Locale.getDefault(), "Mortality: %d", record.getMortality()));

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(record);
            }
        });
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvBirds, tvEggs, tvDamaged, tvFeed, tvMortality;
        ImageButton btnDelete;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBirds = itemView.findViewById(R.id.tvBirds);
            tvEggs = itemView.findViewById(R.id.tvEggs);
            tvDamaged = itemView.findViewById(R.id.tvDamaged);
            tvFeed = itemView.findViewById(R.id.tvFeed);
            tvMortality = itemView.findViewById(R.id.tvMortality);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
