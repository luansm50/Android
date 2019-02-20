package com.example.luanmelo.whatsapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.luanmelo.whatsapp.R;
import com.example.luanmelo.whatsapp.config.ConfiguracaoFirebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail, campoSenha;

    private FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);


    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser userAtual = firebaseAuth.getCurrentUser();
        if(userAtual != null){
            abrirTelaPrincipal();
        }
    }

    public void logarUsuario(View view) {
        //Recupera textos da interface
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        //validar autenticar do usuario
        if (validarCampos(textoEmail, textoSenha)) {
            //autentica usuarios
            firebaseAuth.signInWithEmailAndPassword(textoEmail, textoSenha)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                abrirTelaPrincipal();
                            } else {
                                String excecao = "";
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthInvalidUserException e) {
                                    excecao = "Usuario não está cadastrado.";
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    excecao = "Email e senha não correspondem a um usuário.";
                                } catch (Exception e) {
                                    excecao = "Erro ao logar usuário " + e.getMessage();
                                    e.printStackTrace();
                                }

                                Toast.makeText(LoginActivity.this,
                                        excecao,
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    //valida campos da tela
    public boolean validarCampos(String email, String senha) {
        if (!email.equals("")) {
            if (!senha.equals("")) {
                return true;
            } else {
                Toast.makeText(this, "Digite uma senha", Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(this, "Digite um email", Toast.LENGTH_SHORT);
        }
        return false;
    }


    public void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void abrirTelaCadastro(View view) {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }
}
