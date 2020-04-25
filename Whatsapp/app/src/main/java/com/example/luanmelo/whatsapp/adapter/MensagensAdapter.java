package com.example.luanmelo.whatsapp.adapter;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.luanmelo.whatsapp.R;
import com.example.luanmelo.whatsapp.helper.GlideApp;
import com.example.luanmelo.whatsapp.helper.UsuarioFirebase;
import com.example.luanmelo.whatsapp.model.Mensagem;

import java.util.List;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.MyViewHolder> {

    private List<Mensagem> mensagens;
    private Context context;

    private static final int TIPO_REMETENTE     = 0;
    private static final int TIPO_DESTINATARIO  = 1;

    public MensagensAdapter(List<Mensagem> lista, Context c) {
        this.mensagens = lista;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View item = null;
        if(viewType == TIPO_REMETENTE)
        {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_remetente, parent, false);
        }
        else if(viewType == TIPO_DESTINATARIO)
        {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mensagem_destinatario, parent, false);
        }

        return new MyViewHolder(item);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        Mensagem mensagem = mensagens.get(position);
        String msg = mensagem.getMensagem();
        String img = mensagem.getImagem();

        String nome = mensagem.getNomeUsuario();

        if(!nome.isEmpty())
        {
            holder.nomeUsuario.setText(nome);
        }
        else
        {
            holder.nomeUsuario.setVisibility(View.GONE);
        }

        if(img != null)
        {
            Uri url = Uri.parse(img);
            Glide.with(context).load(url).into(holder.imagem);

            //Esconder
            holder.mensagem.setVisibility(View.GONE);
        }
        else
        {
            holder.mensagem.setText(msg);
            holder.imagem.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        Mensagem mensagem = mensagens.get(position);
        String idUsuario = UsuarioFirebase.getIdUsuario();

        if(idUsuario.equals(mensagem.getIdUsuario()))
        {
            return TIPO_REMETENTE;
        }
        return TIPO_DESTINATARIO;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView mensagem;
        ImageView imagem;
        TextView nomeUsuario;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mensagem = itemView.findViewById(R.id.textMensagemTexto);
            imagem = itemView.findViewById(R.id.imageMensagemFoto);
            nomeUsuario = itemView.findViewById(R.id.textNomeExibicao);
        }
    }
}
