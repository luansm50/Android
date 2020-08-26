package com.example.instagram.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.R;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterMiniaturas extends RecyclerView.Adapter<AdapterMiniaturas.MyViewHolder>
{
    List<ThumbnailItem> listFilters;
    private Context context;

    public AdapterMiniaturas(List<ThumbnailItem> listFilters, Context context) {
        this.listFilters = listFilters;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterMiniaturas.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_filtros, parent, false);
        return new AdapterMiniaturas.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMiniaturas.MyViewHolder holder, int position)
    {
        ThumbnailItem item = listFilters.get(position);

        holder.foto.setImageBitmap(item.image);
        holder.nomeFiltro.setText(item.filterName);
    }

    @Override
    public int getItemCount() {
        return listFilters.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        ImageView foto;
        TextView nomeFiltro;
        public MyViewHolder(View itemView)
        {
            super((itemView));
            foto = itemView.findViewById(R.id.imageFotoFiltro);
            nomeFiltro = itemView.findViewById(R.id.textNameFiltro);
        }
    }
}
