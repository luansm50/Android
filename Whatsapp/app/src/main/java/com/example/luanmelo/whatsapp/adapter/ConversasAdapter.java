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
import com.example.luanmelo.whatsapp.model.Conversa;
import com.example.luanmelo.whatsapp.model.Usuario;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConversasAdapter extends RecyclerView.Adapter<ConversasAdapter.MyViewHolder> {

    private List<Conversa> conversas;
    private Context context;

    public ConversasAdapter(List<Conversa> conversas, Context c)
    {
        this.conversas = conversas;
        this.context   = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_contatos,parent, false);
        return new MyViewHolder(itemList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position)
    {
        Conversa conversa = conversas.get(position);
        myViewHolder.ultimaMensagem.setText(conversa.getUltimaMensagem());

        Usuario usuario = conversa.getUsuarioExibicao();
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
        return conversas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView foto;
        TextView nome, ultimaMensagem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            foto = itemView.findViewById(R.id.circleImageViewFotoContato);
            nome = itemView.findViewById(R.id.textNomeContato);
            ultimaMensagem = itemView.findViewById(R.id.textEmailContato);
        }
    }
}
