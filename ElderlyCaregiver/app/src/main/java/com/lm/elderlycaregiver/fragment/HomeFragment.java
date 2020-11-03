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
import com.lm.elderlycaregiver.activity.PerfilCuidadorActivity;
import com.lm.elderlycaregiver.activity.PerfilResponsavelActivity;
import com.lm.elderlycaregiver.api.CuidadorService;
import com.lm.elderlycaregiver.api.UsuarioService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.model.Cuidador;
import com.lm.elderlycaregiver.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class HomeFragment extends Fragment {

    private FirebaseAuth autenticacao;
    private Usuario usuario;
    private String idUsuarioLogado;

    private Retrofit retrofit;
    private CuidadorService cuidadorService;
    private UsuarioService usuarioService;



    private TextView txtName;

    private Button buttonResponsavel, buttonCuidador;

    public HomeFragment(Usuario usuario)
    {
        this.usuario = usuario;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        retrofit = ApiCliente.getClient();
        usuarioService = retrofit.create(UsuarioService.class);
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        buttonResponsavel = view.findViewById(R.id.buttonResponsavel);
        buttonCuidador = view.findViewById(R.id.buttonCuidador);
        txtName = view.findViewById(R.id.txtName);

        txtName.setText("Ola, " + usuario.getNome());

        buttonCuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Usuario usuario = Usuario
                        .builder()
                        .id(idUsuarioLogado)
                        .tipoUsuario(Usuario.CUIDADOR)
                        .build();

                Call<Usuario> call = usuarioService.atualizarDadosCuidador(idUsuarioLogado, usuario);
                call.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        Intent i = new Intent(getContext(), PerfilCuidadorActivity.class);
                        startActivity(i);
                        getActivity().finish();
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {
                        Log.i("TAG_ERROR", "error", t);
                    }
                });
            }
        });

        return view;
    }

//    public void acaoPerfilCuidador()
//    {
//        Call<Cuidador> call = cuidadorService.recuperarCuidador(idUsuarioLogado);
//        Log.i("TAG", idUsuarioLogado);
//        call.enqueue(new Callback<Cuidador>() {
//            @Override
//            public void onResponse(Call<Cuidador> call, Response<Cuidador> response)
//            {
//                Log.i("TAG", "response");
//                if(response.isSuccessful()) {
//                    Cuidador cuidador = response.body();
//                    acessarPerfilCuidador(cuidador);
//                }
//            }
//            @Override
//            public void onFailure(Call<Cuidador> call, Throwable t) {
//                Log.i("TAG", "error", t);
//
//            }
//        });
//    }

//    private void acessarPerfilCuidador()
//    {
//        if (usuario.getCuidadorAtivado()) {
//                Intent i = new Intent(getContext(), PerfilCuidadorActivity.class);
//                startActivity(i);
//        } else {
//            ativarPerfil (usuario);
//        }
//    }

    private void ativarPerfil(final Usuario u)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Ativar perfil de cuidador");
        builder.setMessage("Seu perfil esta desativado, deseja ativar agora?");
        builder.setCancelable(false);
        builder.setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

//    private void criarPerfil(final Cuidador c)
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("Criar perfil de cuidador");
//        builder.setMessage("Voce nao possui um perfil de cuidador, deseja criar agora?");
//        builder.setCancelable(false);
//        builder.setPositiveButton("Criar agora", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent i = new Intent(getContext(), CadastroCuidadorActivity.class);
//                i.putExtra("cuidador", c);
//                startActivity(i);
//            }
//        });
//
//        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
}