package com.lm.elderlycaregiver.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.api.UsuarioService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.fragment.PerfilFragment;
import com.lm.elderlycaregiver.fragment.cuidador.HomeCuidadorFragment;
import com.lm.elderlycaregiver.fragment.cuidador.MenuCuidadorFragment;
import com.lm.elderlycaregiver.fragment.responsavel.HomeResponsavelFragment;
import com.lm.elderlycaregiver.model.Cuidador;
import com.lm.elderlycaregiver.model.Usuario;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PerfilCuidadorActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private UsuarioService usuarioService;
    private String idUsuarioLogado;
    Cuidador cuidador;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_cuidador);

        retrofit = ApiCliente.getClient();
        usuarioService = retrofit.create(UsuarioService.class);
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        configureBottomNavigationView();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        carregarFragment(R.id.ic_home_perfil, fragmentTransaction);
    }

    private void configureBottomNavigationView()
    {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigationCuidador);

        //configuracoes iniciais
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(false);

        //habilitar navegaçao
        habilitarNavegacao(bottomNavigationViewEx);

        //configura item selecionado inicialmente
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem item = menu.getItem(0);
        item.setChecked(true);
    }

    private void habilitarNavegacao (BottomNavigationViewEx viewEx)
    {
        viewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
            {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                carregarFragment(menuItem.getItemId(), fragmentTransaction);
                return false;
            }
        });
    }

    public void carregarFragment(int menuItem, FragmentTransaction fragmentTransaction)
    {
        Call<Usuario> usuarioCall = usuarioService.recuperarDadosUsuario(idUsuarioLogado);
        usuarioCall.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                Usuario usuario = response.body();

                switch (menuItem)
                {
                    case R.id.ic_menu_perfil:
                        fragmentTransaction.replace(R.id.viewPagerCuidador, new MenuCuidadorFragment(usuario)).commit();
                        break;
                    case R.id.ic_perfil:
                        fragmentTransaction.replace(R.id.viewPagerCuidador, new PerfilFragment(usuario)).commit();
                        break;
                    case R.id.ic_home_perfil:
                        fragmentTransaction.replace(R.id.viewPagerCuidador, new HomeCuidadorFragment(usuario)).commit();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }
}