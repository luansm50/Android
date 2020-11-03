package com.lm.elderlycaregiver.activity.cuidador;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.pattern.MaskPattern;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.api.UsuarioService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.model.Cuidador;
import com.lm.elderlycaregiver.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ValoresCuidadorActivity extends AppCompatActivity {

    private EditText editTextHora;
    private EditText editTextDiurno;
    private EditText editTextNoturno;
    private EditText editTextIntegral;
    private LinearLayout linearLayoutIntegral;

    private Usuario usuario;
    private Cuidador cuidador;

    private Retrofit retrofit;
    private UsuarioService usuarioService;
    private String idUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valores_cuidador);

        retrofit = ApiCliente.getClient();
        usuarioService = retrofit.create(UsuarioService.class);
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        editTextHora = findViewById(R.id.editTextHora);
        editTextDiurno = findViewById(R.id.editTextDiurno);
        editTextNoturno = findViewById(R.id.editTextNoturno);
        editTextIntegral = findViewById(R.id.editTextIntegral);
        linearLayoutIntegral = findViewById(R.id.linearLayoutIntegral);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            usuario = (Usuario) bundle.get("usuario");

            if(usuario != null)
            {
                cuidador = usuario.getCuidador();
                if(cuidador == null)
                {
                    cuidador = Cuidador
                            .builder()
                            .build();
                }
                alterarValoresCampos(cuidador);

            }
        }
    }

    private void alterarValoresCampos(Cuidador cuidador)
    {
        editTextHora.setText(cuidador.getValorHora() + "");
        editTextDiurno.setText(cuidador.getValorDiurno() + "");
        editTextNoturno.setText(cuidador.getValorNoturno() + "");

        if(cuidador.getIntegral())
        {
            linearLayoutIntegral.setVisibility(View.VISIBLE);
            editTextIntegral.setText(cuidador.getValorIntegral() + "");
        }
        else
        {
            linearLayoutIntegral.setVisibility(View.GONE);
        }
    }

    public void ativarIntegral(View view)
    {
        cuidador.setIntegral(true);
        linearLayoutIntegral.setVisibility(View.VISIBLE);
    }

    public void desativarIntegral(View view)
    {
        cuidador.setIntegral(false);
        linearLayoutIntegral.setVisibility(View.GONE);
    }

    public void atualizarHonorarios(View view)
    {
        verificarCampos();
        usuario.setCuidador(cuidador);
        Call<Usuario> usuarioCall = usuarioService.atualizarDadosCuidador(idUsuarioLogado, usuario);
        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                finish();
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    public void verificarCampos()
    {
        String valorHora = editTextHora.getText().toString();
        if(valorHora != null && !valorHora.isEmpty())
        {
            Double valor = Double.parseDouble(valorHora);
            cuidador.setValorHora(valor);
        }

        String valorDiurno = editTextDiurno.getText().toString();
        if(valorDiurno != null && !valorDiurno.isEmpty())
        {
            Double valor = Double.parseDouble(valorDiurno);
            cuidador.setValorDiurno(valor);
        }

        String valorNoturno = editTextNoturno.getText().toString();
        if(valorNoturno != null && !valorNoturno.isEmpty())
        {
            Double valor = Double.parseDouble(valorNoturno);
            cuidador.setValorNoturno(valor);
        }

        if(cuidador.getIntegral())
        {
            String valorIntegral = editTextIntegral.getText().toString();
            if(valorIntegral != null && !valorIntegral.isEmpty())
            {
                Double valor = Double.parseDouble(valorIntegral);
                cuidador.setValorIntegral(valor);
            }
            else
            {
                cuidador.setIntegral(false);
            }
        }
    }

}