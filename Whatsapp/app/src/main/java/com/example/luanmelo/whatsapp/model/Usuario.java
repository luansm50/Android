package com.example.luanmelo.whatsapp.model;

import com.example.luanmelo.whatsapp.config.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private String idUsuario;
    private String email;
    private String nome;
    private String senha;
    private String foto;

    public Usuario() {
    }


    public void salvar(){
        ConfiguracaoFirebase.getDatabaseReference().child("usuarios")
                .child(idUsuario)
                .setValue(this);
    }

    public void atualizar(){


        ConfiguracaoFirebase.getDatabaseReference()
                .child("usuarios")
                .child(idUsuario)
                .updateChildren(converterParaMap());
    }

    public Map<String, Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("idUsuario", getIdUsuario());
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("foto", getFoto());

        return usuarioMap;
    }


    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
