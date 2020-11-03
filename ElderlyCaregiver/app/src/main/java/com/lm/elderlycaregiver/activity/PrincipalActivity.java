package com.lm.elderlycaregiver.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Menu;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.api.CuidadorService;
import com.lm.elderlycaregiver.api.UsuarioService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.fragment.HomeFragment;
import com.lm.elderlycaregiver.fragment.MenuFragment;
import com.lm.elderlycaregiver.fragment.PerfilFragment;
import com.lm.elderlycaregiver.model.Cuidador;
import com.lm.elderlycaregiver.model.Usuario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private Retrofit retrofit;
    private UsuarioService usuarioService;
    private String idUsuarioLogado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        retrofit = ApiCliente.getClient();
        usuarioService = retrofit.create(UsuarioService.class);
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            Usuario usuario = (Usuario) bundle.get("usuario");
            configureBottomNavigationView();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.viewPager, new HomeFragment(usuario)).commit();
        }
    }

    private void configureBottomNavigationView()
    {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavigation);

        //configuracoes iniciais
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextVisibility(true);

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

                switch (menuItem.getItemId())
                {
                    case R.id.ic_sair:
                        deslogarUsuario();
                        Intent intent =  new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        return true;
                }
                return false;
            }
        });
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