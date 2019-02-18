package com.example.luanmelo.organizze.DAO;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.luanmelo.organizze.config.ConfiguracaoFirebase;
import com.example.luanmelo.organizze.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class UsuarioDAO {

    public Usuario buscar(){
        DatabaseReference usuarioRef  = ConfiguracaoFirebase.getFirebaseDatabase()
                .child(ConfiguracaoFirebase.getUsuarioIdAutenticado());
        Usuario u = new Usuario();
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);

                for(DataSnapshot data : dataSnapshot.getChildren()){
                    Log.i("Teste", data.getValue().toString() +"t");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return u;
    }
}
