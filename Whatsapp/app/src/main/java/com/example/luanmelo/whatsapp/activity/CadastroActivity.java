package com.example.luanmelo.whatsapp.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.example.luanmelo.whatsapp.helper.Base64Custom;
import com.example.luanmelo.whatsapp.R;
import com.example.luanmelo.whatsapp.config.ConfiguracaoFirebase;
import com.example.luanmelo.whatsapp.helper.UsuarioFirebase;
import com.example.luanmelo.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha;
    private Button btCadastrar;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        campoNome = findViewById(R.id.editNome);
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);
    }

    public void validarCadastroUsuario(View view) {
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        if (validarCampos(textoNome, textoEmail, textoSenha)) {
            Usuario usuario = new Usuario();
            usuario.setEmail(textoEmail);
            usuario.setNome(textoNome);
            usuario.setSenha(textoSenha);
            cadastrarUsuario(usuario);
        }
    }

    public void cadastrarUsuario(final Usuario usuario) {
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        firebaseAuth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                String idUsuario = UsuarioFirebase.getIdUsuario();
                                usuario.setIdUsuario(idUsuario);
                                usuario.salvar();
                                UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                                finish();
                            }catch (Exception e){
                                Toast.makeText(CadastroActivity.this,
                                        "Erro ao cadastrar",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                excecao = "Digite uma senha mais forte";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "Por favor, digite um email válido";
                            } catch (FirebaseAuthUserCollisionException e) {
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


    public boolean validarCampos(String nome, String email, String senha) {
        if (!nome.equals("")) {
            if (!email.equals("")) {
                if (!senha.equals("")) {
                    return true;
                } else {
                    Toast.makeText(this, "Digite uma senha", Toast.LENGTH_SHORT);
                }
            } else {
                Toast.makeText(this, "Digite um email", Toast.LENGTH_SHORT);
            }
        } else {
            Toast.makeText(this, "Digite um nome", Toast.LENGTH_SHORT);
        }
        return false;
    }
}
