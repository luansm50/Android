package com.lm.elderlycaregiver.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.api.CuidadorService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.model.Cuidador;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private String idUsuarioLogado;

    private Retrofit retrofit;
    private CuidadorService cuidadorService;

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

//        //Configura toolbar
//        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
//        toolbar.setTitle("Home Care");
//        setSupportActionBar(toolbar);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        retrofit = ApiCliente.getClient();
        cuidadorService = retrofit.create(CuidadorService.class);
    }

    public void onClickBotaoPerfilCuidador(View view)
    {
        Call<Cuidador> call = cuidadorService.recuperarCuidador(idUsuarioLogado);
        Log.i("TAG", idUsuarioLogado);
        call.enqueue(new Callback<Cuidador>() {
            @Override
            public void onResponse(Call<Cuidador> call, Response<Cuidador> response)
            {
                Log.i("TAG", "response");
                if(response.isSuccessful()) {
                    Cuidador cuidador = response.body();
                    acessarPerfilCuidador(cuidador);
                }
            }

            @Override
            public void onFailure(Call<Cuidador> call, Throwable t) {
                Log.i("TAG", "error", t);

            }
        });
    }

    private void acessarPerfilCuidador(Cuidador cuidador)
    {
        if(cuidador != null)
        {
            if (cuidador.getAtivado()) {
                Intent i = new Intent(PrincipalActivity.this, PerfilCuidadorActivity.class);
                i.putExtra("cuidador", cuidador);
                startActivity(i);
            } else {
                ativarPerfil (cuidador);
            }
        }
        else {
            cuidador = Cuidador
                    .builder()
                    .idUsuario(idUsuarioLogado)
                    .build();
            criarPerfil(cuidador);
        }
    }

    private void ativarPerfil(final Cuidador c)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ativar perfil de cuidador");
        builder.setMessage("Seu perfil esta desativado, deseja ativar agora?");
        builder.setCancelable(false);
        builder.setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Cuidador cuidador = Cuidador
                        .builder()
                        .idUsuario(c.getIdUsuario())
                        .ativado(true)
                        .build();

                Call<Cuidador> call = cuidadorService.atualizarDadosCuidador(idUsuarioLogado, cuidador);
                call.enqueue(new Callback<Cuidador>() {
                    @Override
                    public void onResponse(Call<Cuidador> call, Response<Cuidador> response) {
                        c.setAtivado(true);
                        Intent i = new Intent(PrincipalActivity.this, PerfilCuidadorActivity.class);
                        i.putExtra("cuidador", c);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<Cuidador> call, Throwable t) {
                        Log.i("TAG_ERROR", "error", t);
                    }
                });
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void criarPerfil(final Cuidador c)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Criar perfil de cuidador");
        builder.setMessage("Voce nao possui um perfil de cuidador, deseja criar agora?");
        builder.setCancelable(false);
        builder.setPositiveButton("Criar agora", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent i = new Intent(PrincipalActivity.this, CadastroCuidadorActivity.class);
                i.putExtra("cuidador", c);
                startActivity(i);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }





    public void onClickBotaoPerfilResponsavel(View view)
    {
        startActivity(new Intent(this, PerfilResponsavelActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal2, menu);
        return true;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.menu_sair :
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
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

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}