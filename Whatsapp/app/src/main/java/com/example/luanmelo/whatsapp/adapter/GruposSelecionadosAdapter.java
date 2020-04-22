package com.example.luanmelo.whatsapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.luanmelo.whatsapp.R;
import com.example.luanmelo.whatsapp.helper.GlideApp;
import com.example.luanmelo.whatsapp.model.Usuario;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GruposSelecionadosAdapter  extends RecyclerView.Adapter<GruposSelecionadosAdapter.MyViewHolder>
{

    public List<Usuario> listaContatos;
    public Context context;

    public GruposSelecionadosAdapter(Context context, List<Usuario> listaContatos){
        this.listaContatos = listaContatos;
        this.context = context;
    }

    @NonNull
    @Override
    public GruposSelecionadosAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_grupo_selecionado, viewGroup, false);

        return new GruposSelecionadosAdapter.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull GruposSelecionadosAdapter.MyViewHolder myViewHolder, int i) {
        Usuario usuario = listaContatos.get(i);

        myViewHolder.nome.setText(usuario.getNome());

        if(usuario.getFoto() != null){
            Uri uri = Uri.parse(usuario.getFoto());
            GlideApp.with(context).load(uri).into(myViewHolder.foto);
        }else{
            myViewHolder.foto.setImageResource(R.drawable.padrao);
        }
    }

    @Override
    public int getItemCount() {
        return listaContatos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView foto;
        TextView nome;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.imageViewFotoMembroSelecionado);
            nome = itemView.findViewById(R.id.textNomeMembroSelecionado);
        }
    }

}
