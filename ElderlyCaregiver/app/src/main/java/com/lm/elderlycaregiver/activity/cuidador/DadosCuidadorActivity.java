package com.lm.elderlycaregiver.activity.cuidador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.api.UsuarioService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DadosCuidadorActivity extends AppCompatActivity {


    private Usuario usuario;

    private Retrofit retrofit;
    private UsuarioService usuarioService;
    private String idUsuarioLogado;

    private TextView textViewEditHonorarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dados_cuidador);

        retrofit = ApiCliente.getClient();
        usuarioService = retrofit.create(UsuarioService.class);
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        textViewEditHonorarios = findViewById(R.id.textViewEditHonorarios);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            usuario = (Usuario) bundle.get("usuario");
        }

        textViewEditHonorarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Call<Usuario> usuarioCall = usuarioService.recuperarDadosUsuario(idUsuarioLogado);
                usuarioCall.enqueue(new Callback<Usuario>() {
                    @Override
                    public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                        Usuario usuario = response.body();
                        Intent i = new Intent(getApplicationContext(), ValoresCuidadorActivity.class);
                        i.putExtra("usuario", usuario);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<Usuario> call, Throwable t) {

                    }
                });



            }
        });
    }
}