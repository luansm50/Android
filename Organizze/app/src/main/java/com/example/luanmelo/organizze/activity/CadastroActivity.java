package com.example.luanmelo.organizze.activity;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luanmelo.organizze.DAO.UsuarioDAO;
import com.example.luanmelo.organizze.R;
import com.example.luanmelo.organizze.config.ConfiguracaoFirebase;
import com.example.luanmelo.organizze.helper.Base64Custom;
import com.example.luanmelo.organizze.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha;
    private Button botaoCadastrar;
    private FirebaseAuth autenticacao;

    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);

        botaoCadastrar = findViewById(R.id.buttonCadastrar);
        botaoCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoNome = campoNome.getText().toString();
                String textoEmail = campoEmail.getText().toString();
                String textoSenha = campoSenha.getText().toString();


                //Validar se os campos foram preenchidos
                if (validaCampos(textoNome, textoEmail, textoSenha)) {
                    //Configura usuario
                    usuario = new Usuario();
                    usuario.setNome(textoNome);
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    cadastrarUsuario();
                }
            }
        });
    }

    public void cadastrarUsuario() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    String idUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setIdUsuario(idUsuario);
                    usuarioDAO.salvar(usuario);
                    finish();
                }else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "Por favor, digite um email válido";
                    }catch (FirebaseAuthUserCollisionException e){
                        excecao = "Esta conta já foi cadastrada";
                    } catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário " + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    public boolean validaCampos(String textoNome, String textoEmail, String textoSenha) {
        //Validar se os campos foram preenchidos
        if (!textoNome.isEmpty()) {
            if (!textoEmail.isEmpty()) {
                if (!textoSenha.isEmpty()) {
                    return true;
                } else {
                    Toast.makeText(CadastroActivity.this,
                            "Preencha a senha!",
                            Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CadastroActivity.this,
                        "Preencha o email!",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CadastroActivity.this,
                    "Preencha o nome!",
                    Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
