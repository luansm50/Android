package com.lm.elderlycaregiver.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.api.UsuarioService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.dao.UsuarioDAO;
import com.lm.elderlycaregiver.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CadastroActivity extends AppCompatActivity {

    private EditText    campoNome;
    private EditText    campoDataNascimento;
    private EditText    campoTelefone;
    private EditText    campoCPF;
    private EditText    campoEmail;
    private EditText    campoSenha;
    private EditText    campoSenha2;
    private ProgressBar progressBar;

    private Usuario usuario;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

//        getSupportActionBar().setTitle("Cadastro");

        inicializarComponentes();
    }

    public void inicializarComponentes()
    {
        campoNome           = findViewById(R.id.editCadastroNome);
        campoDataNascimento = findViewById(R.id.editCadastroDataNascimento);
        campoTelefone       = findViewById(R.id.editCadastroTelefone);
        campoCPF            = findViewById(R.id.editCadastroCPF);
        campoEmail          = findViewById(R.id.editCadastroEmail);
        campoSenha          = findViewById(R.id.editCadastroSenha);
        campoSenha2         = findViewById(R.id.editCadastroSenha2);
        progressBar     = findViewById(R.id.progressEnviar);

        campoNome.requestFocus();

        progressBar.setVisibility(View.GONE);

        SimpleMaskFormatter smfDataNascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        SimpleMaskFormatter smfTelefone = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        SimpleMaskFormatter smfCPF = new SimpleMaskFormatter("NNN.NNN.NNN-NN");

        MaskTextWatcher mtwDataNascimento = new MaskTextWatcher(campoDataNascimento, smfDataNascimento);
        MaskTextWatcher mtwTelefone = new MaskTextWatcher(campoTelefone, smfTelefone);
        MaskTextWatcher mtwCPF = new MaskTextWatcher(campoCPF, smfCPF);

        campoDataNascimento.addTextChangedListener(mtwDataNascimento);
        campoTelefone.addTextChangedListener(mtwTelefone);
        campoCPF.addTextChangedListener(mtwCPF);
    }

    public void onClickBotaoCadastrar(View view)
    {
        progressBar.setVisibility(View.VISIBLE);
        String textoNome            = campoNome.getText().toString();
        String textoDataNascimento  = campoDataNascimento.getText().toString();
        String textoTelefone        = campoTelefone.getText().toString();
        String textoCPF             = campoCPF.getText().toString();
        String textoEmail           = campoEmail.getText().toString();
        String textoSenha           = campoSenha.getText().toString();
        String textoSenha2          = campoSenha2.getText().toString();

        if(validaCampos(textoNome, textoDataNascimento, textoTelefone, textoCPF, textoEmail, textoSenha, textoSenha2))
        {
            usuario = new Usuario();
            usuario.setEmail(textoEmail);
            usuario.setSenha(textoSenha);
            usuario.setTelefone(textoTelefone);
            usuario.setNome(textoNome);
            usuario.setDataNascimento(textoDataNascimento);
            usuario.setCpf(textoCPF);

            Intent intent = new Intent(this, Cadastro2Activity.class);
            intent.putExtra("usuario", usuario);
            startActivity(intent);
        }

        progressBar.setVisibility(View.GONE);
    }

    private Boolean validaCampos(String textoNome, String textoDataNascimento, String textoTelefone, String textoCPF, String textoEmail, String textoSenha, String textoSenha2)
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

        if(textoDataNascimento.isEmpty())
        {
            mensagem += mensagem.isEmpty()? "DATA DE NASCIMENTO" : "|DATA DE NASCIMENTO";
            i++;
            textoValidado = false;
        }

        if(textoTelefone.isEmpty())
        {
            mensagem += mensagem.isEmpty()? "TELEFONE" : "|TELEFONE";
            i++;
            textoValidado = false;
        }

        if(textoCPF.isEmpty())
        {
            mensagem += mensagem.isEmpty()? "CPF" : "|CPF";
            i++;
            textoValidado = false;
        }

        if(textoEmail.isEmpty())
        {
            mensagem += mensagem.isEmpty()? "E-MAIL" : "|E-MAIL";
            i++;
            textoValidado = false;
        }

        if(textoSenha.isEmpty())
        {
            mensagem += mensagem.isEmpty()? "SENHA" : "|SENHA";
            i++;
            textoValidado = false;
        }
        else
        {
            if(textoSenha2.isEmpty())
            {
                mensagem += mensagem.isEmpty()? "CONFIRME A SENHA" : "|CONFIRME A SENHA";
                i++;
                textoValidado = false;
            }
            else
            {
                if(!textoSenha2.equals(textoSenha))
                {
                    mensagem += mensagem.isEmpty()? "SENHAS NAO SAO IGUAIS" : " |SENHAS NAO SAO IGUAIS";
                    i++;
                    textoValidado = false;
                }
            }
        }

        Log.i("TAG", textoValidado + "");


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

            Toast.makeText(this, mensagem, Toast.LENGTH_LONG);

            Log.i("TAG", mensagem);

        }

        return textoValidado;

    }

}