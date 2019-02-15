package com.example.user.myapplication.activity;


import android.annotation.SuppressLint;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.user.myapplication.DAO.TarefaDAO;
import com.example.user.myapplication.R;
import com.example.user.myapplication.model.Tarefa;

public class AdicionarTarefaActivity extends AppCompatActivity {

    private TextInputEditText textTarefa;
    private Tarefa tarefaAtual;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_tarefa);

        textTarefa = findViewById(R.id.textTarefa);

        //Recuperar tarefa,
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");

        //configurar tarefa na caixa de texto
        if(tarefaAtual != null){
            textTarefa.setText(tarefaAtual.getNomeTarefa());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_tarefa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.itemSalvar:
                if(tarefaAtual == null) {
                    //Executar ação para o item salvar
                    String t = textTarefa.getText().toString();
                    if (!t.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(t);
                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                        if(tarefaDAO.salvar(tarefa)){
                            Toast.makeText(getApplicationContext(), "Sucesso ao salvar tarefa", Toast.LENGTH_SHORT);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Erro ao salvar tarefa", Toast.LENGTH_SHORT);
                        }
                    }
                }else{
                    String t = textTarefa.getText().toString();
                    if (!t.isEmpty()) {
                        Tarefa tarefa = new Tarefa();
                        tarefa.setNomeTarefa(t);
                        tarefa.setId(tarefaAtual.getId());
                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
                        if(tarefaDAO.atualizar(tarefa)){
                            Toast.makeText(getApplicationContext(), "Sucesso ao atualizar tarefa", Toast.LENGTH_SHORT);
                            finish();
                        }else{
                            Toast.makeText(getApplicationContext(), "Erro ao atualizar tarefa", Toast.LENGTH_SHORT);
                        }
                    }
                }

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
