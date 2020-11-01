package com.lm.elderlycaregiver.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.lm.elderlycaregiver.R;
import com.lm.elderlycaregiver.api.CuidadorService;
import com.lm.elderlycaregiver.config.ApiCliente;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;
import com.lm.elderlycaregiver.config.UsuarioFirebase;
import com.lm.elderlycaregiver.model.Cuidador;
import com.lm.elderlycaregiver.model.Usuario;

import retrofit2.Call;
import retrofit2.Retrofit;

public class PerfilCuidadorActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private CuidadorService cuidadorService;

    Cuidador cuidador;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_cuidador);

        retrofit = ApiCliente.getClient();
        cuidadorService = retrofit.create(CuidadorService.class);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            cuidador = (Cuidador) bundle.get("cuidador");
        }

        alterarStatusCuidador();
        Log.i("TAG", cuidador.toString());
    }

    private void alterarStatusCuidador()
    {
        if(!cuidador.getAtivado())
        {

        }
    }



//    @RequiresApi(api = Build.VERSION_CODES.N)
//    private Cuidador vereficarStatusPerfil(String idUsuarioLogado) {
//
//        cuidadorDAO.buscar(idUsuarioLogado);
//
//        if(cuidador != null)
//        {
//            if(cuidador.getAtivado())
//            {
//                return cuidador;
//            }
//
//            return ativarPerfil("Para acessar essas funçoes voce precisa ativar o perfil de cuidador. Deseja ativar?", cuidador, true);
//        }
//        Cuidador c = Cuidador
//                .builder()
//                .idUsuario(idUsuarioLogado)
//                .build();
//        return ativarPerfil("Para acessar essas funçoes voce precisa criar um perfil de cuidador. Deseja criar agora?", c, false);
//    }

//    private Cuidador ativarPerfil(String texto, final Cuidador c, final Boolean alreadyExist)
//    {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Ativar perfil de cuidador");
//        builder.setMessage(texto);
//        builder.setCancelable(false);
//        builder.setPositiveButton("Ativar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//               if(alreadyExist)
//               {
//                   c.setAtivado(true);
//                   cuidadorDAO.alterar(c);
//                   cuidador = c;
//               }
//               else
//               {
//                   c.setAtivado(true);
//                   cuidadorDAO.salvar(c);
//                   cuidador = c;
//               }
//            }
//        });
//
//        builder.setNegativeButton("Nao Ativar", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//
//        return cuidador;
//    }
//
//
//    private void buscarCuidador(String id)
//    {
//        DatabaseReference usuariosRef = firebaseRef.child("cuidadores");
//        DatabaseReference usuarioCuidadorRef = usuariosRef.child(id);
//        usuarioCuidadorRef.addListenerForSingleValueEvent(
//                new ValueEventListener() {
//                    @RequiresApi(api = Build.VERSION_CODES.N)
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        cuidador = dataSnapshot.getValue(Cuidador.class);
//                        Log.w("TAG", cuidador.toString());
////                            Usuario usuario = usuarioDAO.buscar(id).get();
////                            cuidador.setUsuario(usuario);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.w("TAG", databaseError.toException());
//                    }
//                }
//        );
//    }
}