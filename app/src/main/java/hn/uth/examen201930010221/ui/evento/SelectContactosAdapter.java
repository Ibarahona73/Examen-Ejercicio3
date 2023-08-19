package hn.uth.examen201930010221.ui.evento;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import hn.uth.examen201930010221.R;
import hn.uth.examen201930010221.databinding.ItemContactoBinding;
import hn.uth.examen201930010221.entity.Contacto;

public class SelectContactosAdapter extends RecyclerView.Adapter<SelectContactosAdapter.ViewHolder> {

    private List<Contacto> dataset;

    private List<Contacto> itemSelectData;
    private OnItemClickListenerContactosSelect<Contacto> manejadorEventoClick;

    public SelectContactosAdapter(List<Contacto> dataset, OnItemClickListenerContactosSelect<Contacto> manejadorEventoClick, List<Contacto> lsActivos) {
        this.dataset = dataset;
        this.manejadorEventoClick = manejadorEventoClick;
        this.itemSelectData = lsActivos;
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

        if(itemSelectData!=null && itemSelectData.size()>0) {
            for (Contacto c : itemSelectData) {
                if(c.getId()== contacto.getId()){
                    holder.itemView.setTag(position);
                    //itemSelectData.remove(c);
                    break;
                }
            }
        }


        if(holder.itemView.getTag()!=null){
            if(Integer.parseInt(holder.itemView.getTag().toString())==position) {
                holder.binding.icono.setBackgroundColor(holder.binding.icono.getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorPrimary}).getColor(0, 0));
                holder.binding.icono.setImageResource(R.drawable.round_check_24);
            }else{
                holder.binding.icono.setBackgroundColor(holder.binding.icono.getContext().getTheme().obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorSecondaryContainer}).getColor(0, 0));
                holder.binding.icono.setImageResource(R.drawable.ic_contact);
            }
        }else{
            holder.binding.icono.setBackgroundColor(holder.binding.icono.getContext().getTheme().obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorSecondaryContainer}).getColor(0, 0));
            holder.binding.icono.setImageResource(R.drawable.ic_contact);
        }

        holder.binding.tvContact.setText(contacto.getName());
        holder.binding.tvTelefono.setText(contacto.getPhone());
        holder.binding.tvEmail.setText(contacto.getEmail());


        holder.setOnClickListener(contacto, manejadorEventoClick);
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

        public void setOnClickListener(Contacto datosContacto, OnItemClickListenerContactosSelect<Contacto> listener) {
            this.itemView.setOnClickListener(v -> {
                if(listener.onItemClick(datosContacto)) {
                    if (binding.getRoot().getTag() == null) {
                        binding.getRoot().setTag(getAdapterPosition());

                        binding.icono.setBackgroundColor(binding.icono.getContext().getTheme().obtainStyledAttributes(new int[]{android.R.attr.colorPrimary}).getColor(0, 0));
                        binding.icono.setImageResource(R.drawable.round_check_24);
                    } else {
                        binding.getRoot().setTag(null);
                        binding.icono.setBackgroundColor(binding.icono.getContext().getTheme().obtainStyledAttributes(new int[]{com.google.android.material.R.attr.colorSecondaryContainer}).getColor(0, 0));
                        binding.icono.setImageResource(R.drawable.ic_contact);
                    }
                }
            });

        }

    }

    public int tamanioLISTA(){
        return itemSelectData.size();
    }
}
