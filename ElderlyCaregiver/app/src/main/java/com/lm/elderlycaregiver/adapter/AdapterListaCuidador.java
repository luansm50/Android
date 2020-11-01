package com.lm.elderlycaregiver.adapter;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.model.Cuidador;
import com.lm.elderlycaregiver.model.PlanoCuidador;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import lombok.AllArgsConstructor;


@AllArgsConstructor
public class AdapterListaCuidador extends RecyclerView.Adapter<AdapterListaCuidador.MyViewHolder>
{
    private List<Cuidador> listaCuidador;
    private Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cuidadores, parent, false);
        return new AdapterListaCuidador.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Cuidador cuidador = listaCuidador.get(position);

//        holder.txtNameCuidadorPesquisa.setText(cuidador.getUsuario().getNome());
//        holder.txtTelefoneCuidadorPesquisa.setText(cuidador.getUsuario().getTelefone());
//        holder.txtClassificacaoCuidadorPesquisa.setText(cuidador.getClassificacao() + "");

        habilitarPlanos(holder, cuidador);

//        Uri uriFotoCuidador = Uri.parse(cuidador.getUsuario().getCaminhoFoto());
//        Glide.with(context).load(uriFotoCuidador).into(holder.imageFotoCuidadorPesquisa);
    }

    private void habilitarPlanos(@NonNull MyViewHolder holder, Cuidador cuidador)
    {
        holder.cbDiariaCuidadorPesquisa.setChecked(false);
        holder.cbSemanalCuidadorPesquisa.setChecked(false);
        holder.cbMensalCuidadorPesquisa.setChecked(false);

//        for(PlanoCuidador planoCuidador : cuidador.getPlanos())
//        {
//            Boolean habilitado = planoCuidador.getHabilitado();
//            if(planoCuidador.getTipoPlano().equals(PlanoCuidador.DIARIA))
//            {
//                holder.cbDiariaCuidadorPesquisa.setChecked(habilitado);
//            }
//
//            if(planoCuidador.getTipoPlano().equals(PlanoCuidador.SEMANAL))
//            {
//                holder.cbSemanalCuidadorPesquisa.setChecked(habilitado);
//            }
//
//            if(planoCuidador.getTipoPlano().equals(PlanoCuidador.MENSAL))
//            {
//                holder.cbMensalCuidadorPesquisa.setChecked(habilitado);
//            }
//        }
    }


    @Override
    public int getItemCount() {
        return listaCuidador.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        CircleImageView imageFotoCuidadorPesquisa;
        TextView txtNameCuidadorPesquisa;
        TextView txtTelefoneCuidadorPesquisa;
        TextView txtClassificacaoCuidadorPesquisa;
        CheckBox cbDiariaCuidadorPesquisa;
        CheckBox cbSemanalCuidadorPesquisa;
        CheckBox cbMensalCuidadorPesquisa;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFotoCuidadorPesquisa           = itemView.findViewById(R.id.imageFotoCuidadorPesquisa);
            txtNameCuidadorPesquisa             = itemView.findViewById(R.id.txtNameCuidadorPesquisa);
            txtTelefoneCuidadorPesquisa         = itemView.findViewById(R.id.txtTelefoneCuidadorPesquisa);
            txtClassificacaoCuidadorPesquisa    = itemView.findViewById(R.id.txtClassificacaoCuidadorPesquisa);
            cbDiariaCuidadorPesquisa            = itemView.findViewById(R.id.cbDiariaCuidadorPesquisa);
            cbSemanalCuidadorPesquisa           = itemView.findViewById(R.id.cbSemanalCuidadorPesquisa);
            cbMensalCuidadorPesquisa            = itemView.findViewById(R.id.cbMensalCuidadorPesquisa);



        }
    }


}
