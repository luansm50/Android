package com.lm.elderlycaregiver.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.api.UsuarioService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.fragment.HomeFragment;
import com.lm.elderlycaregiver.helper.Permissao;
import com.lm.elderlycaregiver.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends IntroActivity {

    private FirebaseAuth autenticacao;

    private Retrofit retrofit;
    private UsuarioService usuarioService;
    private String idUsuarioLogado;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        //Validar Permissoes
        Permissao.validarPermissoes(this, permissoesNecessarias, 1);

        retrofit = ApiCliente.getClient();
        usuarioService = retrofit.create(UsuarioService.class);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_1)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_2)
                .build());
        addSlide(new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.intro_cadastro)
                .canGoForward(false)
                .build());
    }


    @Override
    protected void onStart() {
        super.onStart();
        verificarUsuarioLogado();
    }

    public void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

            abrirTelaPrincipal();
        }
    }

    public void abrirTelaPrincipal() {
        Call<Usuario> call = usuarioService.recuperarDadosUsuario(idUsuarioLogado);
        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Usuario usuario = response.body();

                if(usuario != null)
                {
                    switch(usuario.getTipoUsuario())
                    {
                        case Usuario.NOVO           :
                            Intent principal =  new Intent(getApplicationContext(), PrincipalActivity.class);
                            principal.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            principal.putExtra("usuario", usuario);
                            startActivity(principal);
                            finish();
                            break;
                        case Usuario.RESPONSAVEL    :
                            Intent responsavel =  new Intent(getApplicationContext(), PerfilResponsavelActivity.class);
                            responsavel.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(responsavel);
                            finish();
                            break;
                        case Usuario.CUIDADOR       :
                            Intent cuidador =  new Intent(getApplicationContext(), PerfilCuidadorActivity.class);
                            cuidador.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(cuidador);
                            finish();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                deslogarUsuario();
                Intent intent =  new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    public void btEntrar(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void btCadastrar(View view) {
        startActivity(new Intent(this, CadastroActivity.class));
    }



    private void deslogarUsuario()
    {
        try {
            autenticacao.signOut();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}