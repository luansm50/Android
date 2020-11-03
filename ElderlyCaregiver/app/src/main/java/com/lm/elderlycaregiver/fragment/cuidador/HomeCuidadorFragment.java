package com.lm.elderlycaregiver.fragment.cuidador;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.model.Usuario;

public class HomeCuidadorFragment extends Fragment {

    private Usuario usuario;

    private TextView textViewNomeToolbar;

    public HomeCuidadorFragment(Usuario usuario)
    {
        this.usuario = usuario;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_cuidador, container, false);
        textViewNomeToolbar = view.findViewById(R.id.textViewNomeToolbar);
        textViewNomeToolbar.setText(usuario.getNome());

        return view;
    }
}
