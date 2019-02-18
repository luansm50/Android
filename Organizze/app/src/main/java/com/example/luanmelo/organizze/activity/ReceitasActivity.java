package com.example.luanmelo.organizze.activity;

import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.luanmelo.organizze.R;
import com.example.luanmelo.organizze.config.ConfiguracaoFirebase;
import com.example.luanmelo.organizze.helper.DateCustom;
import com.example.luanmelo.organizze.model.Movimentacao;
import com.example.luanmelo.organizze.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;

    private Movimentacao movimentacao;
    private String usuarioID = ConfiguracaoFirebase.getUsuarioIdAutenticado();
    private Double receitaTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);


        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        //Preenche o campo data com a data atual
        campoData.setText(DateCustom.dataAtual());
        recuperarReceitaTotal();
    }

    public void salvarReceita(View view) {
        String textoValor = campoValor.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();
        String textoData = campoData.getText().toString();
        if (validaCamposDespesa(textoValor, textoCategoria, textoDescricao, textoData)) {
            Double receitaGerada = Double.parseDouble(textoValor);
            movimentacao = new Movimentacao();
            movimentacao.setValor(receitaGerada);
            movimentacao.setCategoria(textoCategoria);
            movimentacao.setDescricao(textoDescricao);
            movimentacao.setData(textoData);
            movimentacao.setTipo("r");

            Double receitaAtualizada = receitaTotal + receitaGerada;

            atualizarUsuarioReceita(receitaAtualizada);

            movimentacao.salvar();
            finish();
        }
    }

    public void recuperarReceitaTotal() {
        DatabaseReference usuarioRef = ConfiguracaoFirebase.getFirebaseDatabase()
                .child("usuarios").child(usuarioID);

        usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal = usuario.getReceitaTotal();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void atualizarUsuarioReceita(final Double receitaTotal) {
        DatabaseReference usuarioRef = ConfiguracaoFirebase.getFirebaseDatabase().child("usuarios")
                .child(usuarioID);
        usuarioRef.child("receitaTotal").setValue(receitaTotal);
    }

    public boolean validaCamposDespesa(String textoValor, String textoCategoria,
                                       String textoDescricao, String textoData) {

        if (!textoValor.isEmpty()) {
            if (!textoCategoria.isEmpty()) {
                if (!textoDescricao.isEmpty()) {
                    if (!textoData.isEmpty()) {
                        return true;
                    } else {
                        Toast.makeText(ReceitasActivity.this,
                                "Data não foi preenchido",
                                Toast.LENGTH_SHORT).show();
                        return false;
                    }
                } else {
                    Toast.makeText(ReceitasActivity.this,
                            "Descrição não foi preenchido",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            } else {
                Toast.makeText(ReceitasActivity.this,
                        "Categoria não foi preenchido",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {
            Toast.makeText(ReceitasActivity.this,
                    "Valor não foi preenchido",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

    }
}
