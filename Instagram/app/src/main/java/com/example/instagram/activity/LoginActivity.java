package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.helper.ConfiguracaoFirabese;
import com.example.instagram.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity
{
    private EditText     campoEmail;
    private EditText     campoSenha;
    private Button       botaoLogin;
    private ProgressBar  progressBar;

    private Usuario      usuario;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicializarComponentes();

        firebaseAuth = ConfiguracaoFirabese.getReferenciaAutencicao();

        verificarUsuarioLogado();

        progressBar.setVisibility(View.GONE);
        botaoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();

                if(validaCampos(textoEmail, textoSenha))
                {
                    usuario = new Usuario();
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    validarLogin(usuario);

                }
            }
        });
    }

//    public void abrirCadastro(View view)
//    {
//        Intent i = new Intent(LoginActivity.this, CadastroActivity.class);
//        startActivity(i);
//    }


    public void inicializarComponentes()
    {
        campoEmail      = findViewById(R.id.editLoginEmail);
        campoSenha      = findViewById(R.id.editLoginSenha);
        botaoLogin      = findViewById(R.id.buttonLoginEntrar);
        progressBar     = findViewById(R.id.progressLogin);

        campoEmail.requestFocus();
    }


    public Boolean validaCampos(String textoEmail, String textoSenha)
    {
        String mensagem = "";
        int i=0;
        Boolean textoValidado = true;

        if(textoEmail.isEmpty())
        {
            mensagem = "E-MAIL";
            i++;
            textoValidado = false;
        }

        if(textoSenha.isEmpty())
        {
            mensagem = mensagem.isEmpty()? "SENHA" : "|SENHA";
            i++;
            textoValidado = false;
        }

        if(!textoValidado)
        {
            if(i>1)
            {
                mensagem = "Campos " + mensagem + " precisam ser preenchidos";
            }
            else
            {
                mensagem = "Campo " + mensagem + " precisa ser preenchido";
            }

            Toast.makeText(this, mensagem, Toast.LENGTH_SHORT);
        }

        return textoValidado;

    }

    public void verificarUsuarioLogado()
    {
        if(firebaseAuth.getCurrentUser() != null)
        {
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
            finish();
        }
    }

    public void validarLogin(Usuario usuario)
    {
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);


            }
        });

    }
}
