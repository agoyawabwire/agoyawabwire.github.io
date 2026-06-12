package com.poultryflow.pro.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.poultryflow.pro.R;
import com.poultryflow.pro.models.ChickenSale;
import java.util.List;

public class ChickenSaleAdapter extends RecyclerView.Adapter<ChickenSaleAdapter.ViewHolder> {

    private final List<ChickenSale> sales;
    private final OnSaleDeleteListener deleteListener;

    public interface OnSaleDeleteListener {
        void onDelete(ChickenSale sale);
    }

    public ChickenSaleAdapter(List<ChickenSale> sales, OnSaleDeleteListener deleteListener) {
        this.sales = sales;
        this.deleteListener = deleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChickenSale sale = sales.get(position);
        holder.tvDate.setText(sale.getDate());
        holder.tvDetail1.setText(sale.getChickenType() + " x" + sale.getQuantity());
        holder.tvDetail2.setText("Total: KES " + sale.getTotalAmount());
        holder.tvStatus.setText(sale.getPaymentStatus());

        holder.btnDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDelete(sale);
            }
        });
    }

    @Override
    public int getItemCount() {
        return sales.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvDetail1, tvDetail2, tvStatus;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDetail1 = itemView.findViewById(R.id.tvDetail1);
            tvDetail2 = itemView.findViewById(R.id.tvDetail2);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
