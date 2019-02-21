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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContatosAdapter extends RecyclerView.Adapter<ContatosAdapter.MyViewHolder> {

    public List<Usuario> listaContatos;
    public Context context;

    public ContatosAdapter(Context context, List<Usuario> listaContatos){
        this.listaContatos = listaContatos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLista = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_contatos, viewGroup, false);


        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Usuario usuario = listaContatos.get(i);

        myViewHolder.nome.setText(usuario.getNome());
        myViewHolder.email.setText(usuario.getEmail());

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

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView foto;
        TextView nome, email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.recyclerViewListaContatos);
            nome = itemView.findViewById(R.id.textNomeContato);
            email = itemView.findViewById(R.id.textEmailContato);
        }
    }
}
