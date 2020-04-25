package com.example.luanmelo.whatsapp.model;

import com.example.luanmelo.whatsapp.config.ConfiguracaoFirebase;
import com.example.luanmelo.whatsapp.helper.Base64Custom;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable
{
    private String id;
    private String name;
    private String foto;
    private List<Usuario> membros;

    public Grupo() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference grupoRef = databaseReference.child("grupos");

        String idGrupoFirabese = grupoRef.push().getKey();
        this.id = idGrupoFirabese;
    }

    public void salvar()
    {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getDatabaseReference();
        DatabaseReference grupoRef = databaseReference.child("grupos");

        grupoRef.child(id).setValue(this);

        //SalvarConversa para membros do grupo;
        for(Usuario membro : getMembros())
        {
            String idRemetente = Base64Custom.codificarBase64(membro.getEmail());

            Conversa conversa = new Conversa();
            conversa.setIdRemetente(idRemetente);
            conversa.setIdDestinatario(getId());
            conversa.setUltimaMensagem("");
            conversa.setIsGroup("true");
            conversa.setGrupo(this);

            conversa.salvar();

        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }
}
