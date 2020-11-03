package com.lm.elderlycaregiver.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.activity.CadastroCuidadorActivity;
import com.lm.elderlycaregiver.activity.LoginActivity;
import com.lm.elderlycaregiver.activity.MainActivity;
import com.lm.elderlycaregiver.activity.PerfilCuidadorActivity;
import com.lm.elderlycaregiver.activity.PerfilResponsavelActivity;
import com.lm.elderlycaregiver.activity.PrincipalActivity;
import com.lm.elderlycaregiver.api.CuidadorService;
import com.lm.elderlycaregiver.api.UsuarioService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.model.Cuidador;
import com.lm.elderlycaregiver.model.Usuario;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerfilFragment extends Fragment {


    private FirebaseAuth autenticacao;
    private UsuarioService usuarioService;
    private String idUsuarioLogado;
    private Retrofit retrofit;
    private Usuario usuario;

    private TextView textView;
    private Button buttonPerfilSair;
    private Button buttonTrocarPerfil;

    public PerfilFragment(Usuario usuario)
    {
        this.usuario = usuario;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        retrofit = ApiCliente.getClient();
        usuarioService = retrofit.create(UsuarioService.class);
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();


        textView = view.findViewById(R.id.textNamePerfil);
        buttonPerfilSair = view.findViewById(R.id.buttonPerfilSair);
        buttonTrocarPerfil = view.findViewById(R.id.buttonTrocarPerfil);

        textView.setText(usuario.getNome());

        buttonPerfilSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deslogarUsuario();
                Intent intent =  new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        buttonTrocarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (usuario.getTipoUsuario())
                {
                    case Usuario.CUIDADOR :
                        alterarPerfil(Usuario.RESPONSAVEL, Usuario.CUIDADOR);
                        break;
                    case Usuario.RESPONSAVEL :
                        alterarPerfil(Usuario.CUIDADOR, Usuario.RESPONSAVEL);
                        break;
                }
            }
        });

        return view;
    }

    public void alterarPerfil(String tipoNovoUsuario, String tipoAtigoUsuario) {
        Usuario usuario = Usuario
                .builder()
                .id(idUsuarioLogado)
                .tipoUsuario(tipoNovoUsuario)
                .build();

        Call<Usuario> usuarioCall = usuarioService.atualizarDadosCuidador(idUsuarioLogado, usuario);
        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                switch (tipoAtigoUsuario)
                {
                   case Usuario.CUIDADOR :
                       Intent responsavel =  new Intent(getContext(), PerfilResponsavelActivity.class);
                       responsavel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(responsavel);
                       getActivity().finish();
                       break;
                   case Usuario.RESPONSAVEL :
                       Intent cuidador =  new Intent(getContext(), PerfilCuidadorActivity.class);
                       cuidador.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(cuidador);
                       getActivity().finish();
                       break;
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }


    private void deslogarUsuario()
    {
        try {
            autenticacao.signOut();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }




}