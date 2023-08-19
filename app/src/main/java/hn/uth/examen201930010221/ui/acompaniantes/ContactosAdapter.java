package hn.uth.examen201930010221.ui.acompaniantes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hn.uth.examen201930010221.databinding.ItemContactoBinding;
import hn.uth.examen201930010221.entity.Contacto;

public class ContactosAdapter extends RecyclerView.Adapter<ContactosAdapter.ViewHolder> {

    private List<Contacto> dataset;
    //private OnItemClickListenerNotas<Contacto> manejadorEventoClick;

    public ContactosAdapter(List<Contacto> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ItemContactoBinding binding = ItemContactoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contacto contacto = dataset.get(position);

        if(contacto.getEmail().isEmpty()){
            holder.binding.tvEmail.setVisibility(View.GONE);
        }else{
            holder.binding.tvEmail.setVisibility(View.VISIBLE);
        }

        holder.binding.tvContact.setText(contacto.getName());
        holder.binding.tvTelefono.setText(contacto.getPhone());
        holder.binding.tvEmail.setText(contacto.getEmail());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setItems(List<Contacto> nota){
        this.dataset = nota;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemContactoBinding binding;
        public ViewHolder(@NonNull ItemContactoBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
