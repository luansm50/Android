package com.lm.elderlycaregiver.dao;

import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.lm.elderlycaregiver.config.ConfiguracaoFirebase;
import com.lm.elderlycaregiver.model.Cuidador;
import com.lm.elderlycaregiver.model.Usuario;

import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements Dao<Usuario>
{
    private DatabaseReference firebaseRef;

    private Usuario usuarioLogado;

    public UsuarioDAO()
    {
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public Optional<Usuario> buscar(final String id) {


        DatabaseReference usuariosRef = firebaseRef.child("usuarios");
        DatabaseReference usuarioCuidadorRef = usuariosRef.child(id);
        usuarioCuidadorRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usuarioLogado = dataSnapshot.getValue(Usuario.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.w("TAG", databaseError.toException());
                    }
                }
        );

        if(usuarioLogado != null)
        {
            return Optional.of(usuarioLogado);
        }
        return Optional.empty();
    }

    @Override
    public List<Usuario> listarTodos() {
        return null;
    }

    @Override
    public Boolean salvar(Usuario usuario)
    {
//        usuario.setSenha("");

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(usuario.getId());
        usuariosRef.setValue(usuario);
        return true;
    }



    @Override
    public Boolean alterar(Usuario usuario) {
        return null;
    }

    @Override
    public Boolean deletar(Usuario usuario) {
        return null;
    }

    public static FirebaseUser getUsuarioAtual()
    {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }
}
