package com.lm.elderlycaregiver.fragment.cuidador;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.activity.cuidador.DadosCuidadorActivity;
import com.lm.elderlycaregiver.model.Usuario;

public class MenuCuidadorFragment extends Fragment {

    private Usuario usuario;

    private TextView textViewNomePerfil;

    private Button buttonPerfilCuidador;

    public MenuCuidadorFragment(Usuario usuario)
    {
        this.usuario = usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu_cuidador, container, false);

        textViewNomePerfil = view.findViewById(R.id.textNamePerfil);
        buttonPerfilCuidador = view.findViewById(R.id.buttonPerfilCuidador);

        textViewNomePerfil.setText(usuario.getNome());

        buttonPerfilCuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DadosCuidadorActivity.class);
                i.putExtra("usuario", usuario);
                startActivity(i);
            }
        });

        return view;
    }
}