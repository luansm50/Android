package com.example.instagram.model;

import com.example.instagram.helper.ConfiguracaoFirabese;
import com.example.instagram.helper.UsuarioFirebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Postagem implements Serializable
{
    private String id;
    private String idUsuario;
    private String descricao;
    private String caminhoFoto;

    public Postagem()
    {
        DatabaseReference firebaseRef = ConfiguracaoFirabese.getReferenceFirabese();
        DatabaseReference postagemRef = firebaseRef.child("postagens");
        this.id = postagemRef.push().getKey();
    }

    public boolean salvar(DataSnapshot seguidoresSnapshot)
    {
        Map objeto = new HashMap();
        Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();


        DatabaseReference firebaseRef = ConfiguracaoFirabese.getReferenceFirabese();
        String combinacaoId = "/" + getIdUsuario() + "/" + getId();
        objeto.put("/postagens" + combinacaoId, this);

        //Referencia para postagem
        for(DataSnapshot seguidores : seguidoresSnapshot.getChildren())
        {
            String idSeguidor = seguidores.getKey();
            //Monta objeto
            HashMap<String, Object> dadosSeguidor = new HashMap<>();
            dadosSeguidor.put("fotoPostagem", getCaminhoFoto());
            dadosSeguidor.put("descricao",    getDescricao());
            dadosSeguidor.put("id",           getId());
            dadosSeguidor.put("nomeUsuario",  usuarioLogado.getNome());
            dadosSeguidor.put("fotoUsuario",  usuarioLogado.getCaminhoFoto());

            String idsAtualizacao = "/" + idSeguidor + "/" + getId();
            objeto.put("/feed" + idsAtualizacao, dadosSeguidor);
        }



        objeto.put("/feed" + combinacaoId, this);
        objeto.put("/destaques" + combinacaoId, this);


        firebaseRef.updateChildren(objeto);









//        DatabaseReference postagensRef = firebaseRef
//                .child("postagens")
//                .child(getIdUsuario())
//                .child(id);
//        postagensRef.setValue(this);

        return true;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }
}
