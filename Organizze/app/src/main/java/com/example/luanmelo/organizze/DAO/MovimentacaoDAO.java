package com.example.luanmelo.organizze.DAO;

import com.example.luanmelo.organizze.config.ConfiguracaoFirebase;
import com.example.luanmelo.organizze.helper.Base64Custom;
import com.example.luanmelo.organizze.helper.DateCustom;
import com.example.luanmelo.organizze.helper.GenericDAO;
import com.example.luanmelo.organizze.model.Movimentacao;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class MovimentacaoDAO implements GenericDAO<Movimentacao> {
    @Override
    public void salvar(Movimentacao movimentacao) {

        DatabaseReference firebaseDatabase = ConfiguracaoFirebase.getFirebaseDatabase();
        firebaseDatabase.child("movimentacao")
                .child(new UsuarioDAO().buscar().getIdUsuario())
                .child(DateCustom.mesAnoDataEscolhida(movimentacao.getData()))
                .push()
                .setValue(movimentacao);
    }

    @Override
    public void deletar(Movimentacao movimentacao) {

    }

    @Override
    public void alterar(Movimentacao movimentacao) {

    }

    @Override
    public List<Movimentacao> listarTodos() {
        return null;
    }


    public Movimentacao buscar(Movimentacao movimentacao) {
        return null;
    }
}
