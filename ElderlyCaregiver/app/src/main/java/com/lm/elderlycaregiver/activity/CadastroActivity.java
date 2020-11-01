package com.lm.elderlycaregiver.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.dao.UsuarioDAO;
import com.lm.elderlycaregiver.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText    campoNome;
    private EditText    campoTelefone;
    private EditText    campoEmail;
    private EditText    campoSenha;
    private Button      botaoCadastrar;
    private ProgressBar progressBar;

    private Usuario usuario;
    private UsuarioDAO usuarioDAO;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        usuarioDAO = new UsuarioDAO();
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

//        getSupportActionBar().setTitle("Cadastro");

        inicializarComponentes();

    }

    public void inicializarComponentes()
    {
        campoNome       = findViewById(R.id.editCadastroNome);
        campoTelefone   = findViewById(R.id.editCadastroTelefone);
        campoEmail      = findViewById(R.id.editCadastroEmail);
        campoSenha      = findViewById(R.id.editCadastroSenha);
        botaoCadastrar  = findViewById(R.id.buttonCadastrar);
        progressBar     = findViewById(R.id.progressCadastro);

        campoNome.requestFocus();

        progressBar.setVisibility(View.GONE);
    }

    public void onClickBotaoCadastrar(View view)
    {
        progressBar.setVisibility(View.VISIBLE);
        String textoNome        = campoNome.getText().toString();
        String textoTelefone    = campoNome.getText().toString();
        String textoEmail       = campoEmail.getText().toString();
        String textoSenha       = campoSenha.getText().toString();

        if(validaCampos(textoNome, textoTelefone, textoEmail, textoSenha))
        {
            usuario = new Usuario();
            usuario.setEmail(textoEmail);
            usuario.setSenha(textoSenha);
            usuario.setTelefone(textoTelefone);
            usuario.setNome(textoNome);
            cadastrar();

        }
    }

    private Boolean validaCampos(String textoNome, String textoTelefone, String textoEmail, String textoSenha)
    {
        String mensagem = "";
        int i=0;
        Boolean textoValidado = true;
        if(textoNome.isEmpty())
        {
            mensagem = "NOME";
            i++;
            textoValidado = false;
        }

        if(textoTelefone.isEmpty())
        {
            mensagem = mensagem.isEmpty()? "TELEFONE" : "|TELEFONE";
            i++;
            textoValidado = false;
        }

        if(textoEmail.isEmpty())
        {
            mensagem = mensagem.isEmpty()? "E-MAIL" : "|E-MAIL";
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

    public void cadastrar()
    {
        Task<AuthResult> task =  firebaseAuth.createUserWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(
                this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful())
                        {
                            //Salvar dados no firebase
                            String idUsuario = task.getResult().getUser().getUid();
                            usuario.setId(idUsuario);

                            usuarioDAO.salvar(usuario);

                            //Salvar dados no profile do Firebase
                            UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                            Toast.makeText(CadastroActivity.this, "Cadastro com sucesso!", Toast.LENGTH_SHORT);
                            Intent i = new Intent(getApplicationContext(), PrincipalActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            String erroExcecao = "";
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthWeakPasswordException e)
                            {
                                erroExcecao = "Digite uma senha mais forte!";
                            }
                            catch (FirebaseAuthInvalidCredentialsException e)
                            {
                                erroExcecao = "Digite um email valido!";

                            }
                            catch (FirebaseAuthUserCollisionException e)
                            {
                                erroExcecao = "Esta conta ja foi cadastrada";
                            }
                            catch (Exception e)
                            {
                                erroExcecao = "ao cadastrar usuario: " + e.getMessage();
                            }

                            Toast.makeText(CadastroActivity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT);
                        }

                    }
                }
        );
    }

}