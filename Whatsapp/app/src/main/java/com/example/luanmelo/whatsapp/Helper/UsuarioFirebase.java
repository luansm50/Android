package com.example.luanmelo.whatsapp.helper;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.luanmelo.whatsapp.config.ConfiguracaoFirebase;
import com.example.luanmelo.whatsapp.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFirebase {

    public static String getIdUsuario() {
        FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        String idUsuario = Base64Custom.codificarBase64(firebaseAuth.getCurrentUser().getEmail());
        return idUsuario;
    }

    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth firebaseAuth = ConfiguracaoFirebase.getFirebaseAuth();
        return firebaseAuth.getCurrentUser();
    }

    public static boolean atualizarFotoUsuario(Uri url) {
        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profle = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url).build();
            user.updateProfile(profle).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar foto do usuário");
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean atualizarNomeUsuario(String nome) {
        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profle = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome).build();
            user.updateProfile(profle).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar nome de usuário");
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Usuario getDadosUsuarioLogado(){
        Usuario usuario = new Usuario();
        usuario.setEmail(getUsuarioAtual().getEmail());
        usuario.setNome(getUsuarioAtual().getDisplayName());

        if(getUsuarioAtual().getPhotoUrl() == null){
            usuario.setFoto("");
        }else{
            usuario.setFoto(getUsuarioAtual().getPhotoUrl().toString());
        }
        return usuario;
    }
}
