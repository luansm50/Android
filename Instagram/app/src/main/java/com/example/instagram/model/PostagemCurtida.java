package com.example.instagram.model;

import com.example.instagram.helper.ConfiguracaoFirabese;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;

public class PostagemCurtida
{
    public Feed feed;
    public Usuario usuario;
    public int qtdCurtida = 0;

    public PostagemCurtida() {
    }

    public void salvar()
    {
        DatabaseReference firebaseRef = ConfiguracaoFirabese.getReferenceFirabese();

        DatabaseReference pCurtidasRef = firebaseRef
                .child("postagens-curtida")
                .child(feed.getId())
                .child(usuario.getId());

        pCurtidasRef.removeValue();

        atualizarQtd(-1);
    }
    public void remover()
    {
        DatabaseReference firebaseRef = ConfiguracaoFirabese.getReferenceFirabese();

        //Objeto usuario
        HashMap<String, Object> dadosUsuario = new HashMap<>();
        dadosUsuario.put("nomeUsuario", usuario.getNome());
        dadosUsuario.put("caminhoFoto", usuario.getCaminhoFoto());

        DatabaseReference pCurtidasRef = firebaseRef
                .child("postagens-curtida")
                .child(feed.getId())
                .child(usuario.getId());

        pCurtidasRef.setValue(dadosUsuario);

        atualizarQtd(1);
    }

    public void atualizarQtd(int valor)
    {
        qtdCurtida = qtdCurtida + valor;
        DatabaseReference firebaseRef = ConfiguracaoFirabese.getReferenceFirabese();
        DatabaseReference pCurtidasRef = firebaseRef
            .child("postagens-curtida")
            .child(feed.getId())
            .child("qtdCurtidas");
        setQtdCurtida(getQtdCurtida());
        pCurtidasRef.setValue(getQtdCurtida());

    }



    public int getQtdCurtida() {
        return qtdCurtida;
    }

    public void setQtdCurtida(int qtdCurtida) {
        this.qtdCurtida = qtdCurtida;
    }

    public Feed getFeed() {
        return feed;
    }

    public void setFeed(Feed feed) {
        this.feed = feed;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
