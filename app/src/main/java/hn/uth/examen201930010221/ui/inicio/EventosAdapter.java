package hn.uth.examen201930010221.ui.inicio;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hn.uth.examen201930010221.databinding.ItemNoteBinding;
import hn.uth.examen201930010221.entity.Fitnes;

public class EventosAdapter extends RecyclerView.Adapter<EventosAdapter.ViewHolder>{

    private List<Fitnes> dataset;
    private OnItemClickListener<Fitnes> manejadorEventoClick;

    public EventosAdapter(List<Fitnes> dataset, OnItemClickListener<Fitnes> manejadorEventoClick) {
        this.dataset = dataset;
        this.manejadorEventoClick = manejadorEventoClick;
    }

    @NonNull
    @Override
    public EventosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemNoteBinding binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EventosAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventosAdapter.ViewHolder holder, int position) {
        Fitnes fit = dataset.get(position);


        holder.binding.tvLugar.setText(fit.getLugar());
        holder.binding.tvFecha.setText(fit.getFecha());
        holder.binding.tvEjer.setText(fit.getEjercicio());
        holder.binding.tvCord.setText(fit.getCoordinador());
        /*
        int img = holder.itemView.getResources().getIdentifier("cap" + (fit.getImg()+1), "drawable", holder.itemView.getContext().getPackageName());
        holder.binding.fotografia.setImageResource(img); */

        holder.setOnClickListener(fit, manejadorEventoClick);


    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setItems(List<Fitnes> fit){
        this.dataset = fit;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemNoteBinding binding;
        public ViewHolder(@NonNull ItemNoteBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setOnClickListener(Fitnes datosFit, OnItemClickListener<Fitnes> listener) {
            this.itemView.setOnClickListener(v -> {

                listener.onItemClick(datosFit);
            });

            this.binding.btnDelete.setOnClickListener(v -> listener.onItemClickDelete(datosFit));
            this.binding.btnEdit.setOnClickListener(v -> listener.onItemClick(datosFit));
            this.binding.btnShared.setOnClickListener(v -> listener.onItemClickShared(datosFit));
        }
    }
}
