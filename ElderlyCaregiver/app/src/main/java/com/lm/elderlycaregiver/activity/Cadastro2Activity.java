package com.lm.elderlycaregiver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.lm.elderlycaregiver.api.CEPService;
import com.lm.elderlycaregiver.api.CuidadorService;
import com.lm.elderlycaregiver.api.UsuarioService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.dao.UsuarioDAO;
import com.lm.elderlycaregiver.model.Endereco;
import com.lm.elderlycaregiver.model.Usuario;

import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class Cadastro2Activity extends AppCompatActivity {

    private EditText    campoCEP;
    private EditText    campoEndereco;
    private EditText    campoNumero;
    private EditText    campoBairro;
    private EditText    campoCidade;
    private EditText    campoUF;
    private EditText    campoComplemento;
    private Button      botaoCadastrar;
    private ProgressBar progressBar;

    private Usuario usuario;
    private UsuarioDAO usuarioDAO;
    private FirebaseAuth firebaseAuth;

    private Retrofit retrofit;
    private Retrofit retrofitCEP;
    private UsuarioService usuarioService;
    private CEPService cepService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_2);
        usuarioDAO = new UsuarioDAO();
        firebaseAuth = ConfiguracaoFirebase.getFirebaseAutenticacao();

//        getSupportActionBar().setTitle("Cadastro");

        retrofit = ApiCliente.getClient();
        retrofitCEP = ApiCliente.getClienteCEP();

        usuarioService = retrofit.create(UsuarioService.class);
        cepService = retrofitCEP.create(CEPService.class);



        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            usuario = (Usuario) bundle.get("usuario");
            Log.i("TAG", usuario.toString());
        }

        inicializarComponentes();
    }

    public void inicializarComponentes()
    {
        campoCEP            = findViewById(R.id.editCadastroCEP);
        campoEndereco       = findViewById(R.id.editCadastroEndereco);
        campoNumero         = findViewById(R.id.editCadastroNumero);
        campoBairro         = findViewById(R.id.editCadastroBairro);
        campoCidade         = findViewById(R.id.editCadastroCidade);
        campoUF             = findViewById(R.id.editCadastroUF);
        campoComplemento    = findViewById(R.id.editCadastroComplemento);

        botaoCadastrar  = findViewById(R.id.buttonCadastrar);
        progressBar     = findViewById(R.id.progressCadastrar);

        progressBar.setVisibility(View.GONE);

        campoCEP.requestFocus();

        SimpleMaskFormatter smfCEP = new SimpleMaskFormatter("NNNNN-NNN");
        MaskTextWatcher mtwCEP = new MaskTextWatcher(campoCEP, smfCEP);
        campoCEP.addTextChangedListener(mtwCEP);

        campoCEP.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus)
                {
                    progressBar.setVisibility(View.VISIBLE);
                    alterarStatusCampos(false);
                    Call<Endereco> callSync = cepService.consultarCEP(campoCEP.getText().toString());
                    callSync.enqueue(new Callback<Endereco>() {
                        @Override
                        public void onResponse(Call<Endereco> call, Response<Endereco> response) {
                            progressBar.setVisibility(View.GONE);
                            Endereco endereco = response.body();
                            Log.i("TAG",  endereco.toString());

                            alterarStatusCampos(true);
                            alterarValoresCampos(endereco);
                        }

                        @Override
                        public void onFailure(Call<Endereco> call, Throwable t) {
                            Log.i("TAG", "error", t);
                            progressBar.setVisibility(View.GONE);
                            alterarStatusCampos(true);
                        }
                    });

                }

            }
        });
    }

    public void alterarStatusCampos(Boolean enable)
    {
        campoCEP.setEnabled(enable);
        campoEndereco.setEnabled(enable);
        campoNumero.setEnabled(enable);
        campoBairro.setEnabled(enable);
        campoCidade.setEnabled(enable);
        campoUF.setEnabled(enable);
        campoComplemento.setEnabled(enable);
        botaoCadastrar.setEnabled(enable);
    }

    public void alterarValoresCampos(Endereco endereco)
    {
        campoCEP.setText(endereco.getCep());
        campoEndereco.setText(endereco.getLogradouro());

        if(endereco.getBairro() != null)
        {
            campoBairro.setText(endereco.getBairro());
        }

        campoCidade.setText(endereco.getLocalidade());
        campoUF.setText(endereco.getUf());
    }

    public void onClickBotaoCadastrar(View view)
    {
        progressBar.setVisibility(View.VISIBLE);

        String textoCEP         = campoCEP.getText().toString();
        String textoEndereco    = campoEndereco.getText().toString();
        String textoNumero      = campoNumero.getText().toString();
        String textoBairro      = campoBairro.getText().toString();
        String textoCidade      = campoCidade.getText().toString();
        String textoUF          = campoUF.getText().toString();
        String textoComplemento = campoComplemento.getText().toString();

        if(validaCampos(textoCEP, textoEndereco, textoNumero, textoBairro, textoCidade, textoUF))
        {
            Endereco endereco = Endereco
                    .builder()
                    .cep(textoCEP)
                    .logradouro(textoEndereco)
                    .numero(textoNumero)
                    .bairro(textoBairro)
                    .localidade(textoCidade)
                    .uf(textoUF)
                    .build();

            usuario.setEndereco(endereco);

            cadastrar();
        }
    }

    private Boolean validaCampos(String textoCEP, String textoEndereco, String textoNumero, String textoBairro, String textoCidade, String textoUF)
    {
        String mensagem = "";
        int i=0;
        Boolean textoValidado = true;
        if(textoCEP.isEmpty())
        {
            mensagem = "CEP";
            i++;
            textoValidado = false;
        }

        if(textoEndereco.isEmpty())
        {
            mensagem = mensagem.isEmpty()? "ENDERECO" : "|ENDERECO";
            i++;
            textoValidado = false;
        }

        if(textoNumero.isEmpty())
        {
            mensagem = mensagem.isEmpty()? "NUMERO" : "|NUMERO";
            i++;
            textoValidado = false;
        }

        if(textoBairro.isEmpty())
        {
            mensagem = mensagem.isEmpty()? "BAIRRO" : "|BAIRRO";
            i++;
            textoValidado = false;
        }

        if(textoCidade.isEmpty())
        {
            mensagem = mensagem.isEmpty()? "CIDADE" : "|CIDADE";
            i++;
            textoValidado = false;
        }

        if(textoUF.isEmpty())
        {
            mensagem = mensagem.isEmpty()? "UF" : "|UF";
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

            Toast.makeText(this, mensagem, Toast.LENGTH_LONG);
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

                            Call<Usuario> call = usuarioService.salvarUsuario(idUsuario, usuario);
                            call.enqueue(new Callback<Usuario>() {
                                @Override
                                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                                    //Salvar dados no profile do Firebase
                                    UsuarioFirebase.atualizarNomeUsuario(usuario.getNome());
                                    Toast.makeText(Cadastro2Activity.this, "Cadastro com sucesso!", Toast.LENGTH_SHORT);
                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    i.putExtra("usuario", usuario);
                                    startActivity(i);
                                    finish();
                                }

                                @Override
                                public void onFailure(Call<Usuario> call, Throwable t) {
                                    Log.i("TAG_ERROR", "error", t);
                                }
                            });


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

                            Toast.makeText(Cadastro2Activity.this, "Erro: " + erroExcecao, Toast.LENGTH_SHORT);
                        }

                    }
                }
        );
    }

}