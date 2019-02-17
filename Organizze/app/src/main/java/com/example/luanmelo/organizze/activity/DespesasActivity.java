package com.example.luanmelo.organizze.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luanmelo.organizze.DAO.MovimentacaoDAO;
import com.example.luanmelo.organizze.DAO.UsuarioDAO;
import com.example.luanmelo.organizze.R;
import com.example.luanmelo.organizze.config.ConfiguracaoFirebase;
import com.example.luanmelo.organizze.helper.Base64Custom;
import com.example.luanmelo.organizze.helper.DateCustom;
import com.example.luanmelo.organizze.model.Movimentacao;
import com.example.luanmelo.organizze.model.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class DespesasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;

    private Movimentacao movimentacao;
    private Usuario usuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_despesas);

        usuario = new UsuarioDAO().buscar();

        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        //Preenche o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());
    }

    public void salvarDespesa(View view) {
        String textoValor = campoValor.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();
        String textoData = campoData.getText().toString();
        if (validaCamposDespesa(textoValor, textoCategoria, textoDescricao, textoData)) {
            Double despesaGerada = Double.parseDouble(textoValor);
            movimentacao = new Movimentacao();
            movimentacao.setValor(despesaGerada);
            movimentacao.setCategoria(textoCategoria);
            movimentacao.setDescricao(textoDescricao);
            movimentacao.setData(textoData);
            movimentacao.setTipo("d");

            Double despesaTotal = usuario.getDespesaTotal();
            Double despesaAtualizada = despesaTotal + despesaGerada;
            usuario.setDespesaTotal(despesaAtualizada);

            UsuarioDAO usuarioDAO = new UsuarioDAO();
            usuarioDAO.alterar(usuario);

            MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
            movimentacaoDAO.salvar(movimentacao);
        }
    }

    public boolean validaCamposDespesa(String textoValor, String textoCategoria,
                                       String textoDescricao, String textoData) {

        if (!textoValor.isEmpty()) {
            if (!textoCategoria.isEmpty()) {
                if (!textoDescricao.isEmpty()) {
                    if (!textoData.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(DespesasActivity.this,
                                "Data não foi preenchido",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(DespesasActivity.this,
                            "Descrição não foi preenchido",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(DespesasActivity.this,
                        "Categoria não foi preenchido",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(DespesasActivity.this,
                    "Valor não foi preenchido",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }



    public void atualizarDespesa() {

    }
}
