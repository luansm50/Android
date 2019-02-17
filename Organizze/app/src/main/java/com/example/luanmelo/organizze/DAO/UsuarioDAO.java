package com.example.luanmelo.organizze.DAO;

import android.support.annotation.NonNull;

import com.example.luanmelo.organizze.config.ConfiguracaoFirebase;
import com.example.luanmelo.organizze.helper.Base64Custom;
import com.example.luanmelo.organizze.helper.GenericDAO;
import com.example.luanmelo.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UsuarioDAO implements GenericDAO<Usuario> {

    private DatabaseReference firebaseRef;

    public UsuarioDAO() {
        firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    }

    @Override
    public void salvar(Usuario usuario) {
        firebaseRef.child("usuarios")
                .child(usuario.getIdUsuario())
                .setValue(usuario);

    }

    @Override
    public void deletar(Usuario usuario) {

    }

    @Override
    public void alterar(Usuario usuario) {
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(usuario.getIdUsuario());
        /*usuarioRef.child("despesaTotal").setValue(usuario.getDespesaTotal());
        usuarioRef.child("receitaTotal").setValue(usuario.getReceitaTotal());*/
        usuarioRef.setValue(usuario);
    }

    @Override
    public List<Usuario> listarTodos() {
        return null;
    }


    public Usuario buscar() {
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        String idUsuario = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail());

        DatabaseReference usuarioRef = firebaseRef.child("usuarios")
                .child(idUsuario);
        final Usuario usuario = new Usuario();
        usuario.setIdUsuario(idUsuario);
        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario u = dataSnapshot.getValue(Usuario.class);
                usuario.setNome(u.getNome());
                usuario.setEmail(u.getEmail());
                usuario.setDespesaTotal(u.getDespesaTotal());
                usuario.setReceitaTotal(u.getReceitaTotal());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return usuario;
    }
}
