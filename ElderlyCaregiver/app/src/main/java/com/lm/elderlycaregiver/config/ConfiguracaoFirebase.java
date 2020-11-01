package com.lm.elderlycaregiver.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.lm.elderlycaregiver.helper.Base64Custom;

public class ConfiguracaoFirebase {

    private static FirebaseAuth autenticacao;
    private static DatabaseReference firebaseDatabase;
    //retorna a instancia do FirabeseDatabase
    public static DatabaseReference getFirebaseDatabase(){
        if(firebaseDatabase == null){
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        }
        return firebaseDatabase;
    }

    //retorna a instancia do FirebaseAuth
    public static FirebaseAuth getFirebaseAutenticacao(){
        if(autenticacao == null){
            autenticacao = FirebaseAuth.getInstance();
        }
        return autenticacao;
    }

    public static String getUsuarioIdAutenticado(){
//        return Base64Custom.codificarBase64(
//                ConfiguracaoFirebase
//                        .getFirebaseAutenticacao()
////                        .getCurrentUser()
////                        .getEmail()
//        );
        return "";
    }
}
