package com.example.myapplication4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ConsumoAdapter extends RecyclerView.Adapter<ConsumoAdapter.ConsumoViewHolder> {

    private List<Consumo> consumos;
    private Context context;

    public ConsumoAdapter(List<Consumo> consumos, Context context) {
        this.consumos = consumos;
        this.context = context;
    }

    @NonNull
    @Override
    public ConsumoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consumo, parent, false);
        return new ConsumoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumoViewHolder holder, int position) {
        Consumo consumo = consumos.get(position);
        holder.tvNombre.setText(consumo.getNombre());
        holder.tvRacion.setText(consumo.getRacion());
        holder.tvCalorias.setText(String.valueOf(consumo.getCalorias()));
        holder.tvCategoria.setText(consumo.getCategoria());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Resumen:\n" + consumo.getNombre() + "\n" +
                    "Ración: " + consumo.getRacion() + "\n" +
                    "Calorías: " + consumo.getCalorias() + "\n" +
                    "Categoría: " + consumo.getCategoria(), Toast.LENGTH_LONG).show();
        });
    }

    @Override
    public int getItemCount() {
        return consumos.size();
    }

    static class ConsumoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvRacion, tvCalorias, tvCategoria;

        public ConsumoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvRacion = itemView.findViewById(R.id.tvRacion);
            tvCalorias = itemView.findViewById(R.id.tvCalorias);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
        }
    }
}
