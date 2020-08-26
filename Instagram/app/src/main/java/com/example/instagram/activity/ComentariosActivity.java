package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.instagram.R;
import com.example.instagram.adapter.AdapterComentarios;
import com.example.instagram.helper.ConfiguracaoFirabese;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Comentario;
import com.example.instagram.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ComentariosActivity extends AppCompatActivity {

    private EditText editComentario;
    private RecyclerView recyclerComentarios;

    private AdapterComentarios adapterComentarios;

    private DatabaseReference firebaseRef;
    private DatabaseReference comentariosRef;

    private ValueEventListener valueEventListenerComentarios;

    private String idPostagem;
    private Usuario usuarioLogado;
    private List<Comentario> listaComentarios = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Comentarios");
        setSupportActionBar(toolbar);

        editComentario = findViewById(R.id.editComentario);
        recyclerComentarios = findViewById(R.id.recyclerComentarios);

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        firebaseRef = ConfiguracaoFirabese.getReferenceFirabese();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            idPostagem = bundle.getString("idPostagem");
        }
        adapterComentarios = new AdapterComentarios(listaComentarios, getApplicationContext());
        recyclerComentarios.setHasFixedSize(true);
        recyclerComentarios.setLayoutManager(new LinearLayoutManager(this));
        recyclerComentarios.setAdapter(adapterComentarios);
    }

    public void salvarComentario(View view)
    {
        String textoComentario = editComentario.getText().toString();
        if(textoComentario != null && !textoComentario.isEmpty())
        {
            Comentario comentario = new Comentario();
            comentario.setIdPostagem(idPostagem);
            comentario.setIdUsuario(usuarioLogado.getId());
            comentario.setNomeUsuario(usuarioLogado.getNome());
            comentario.setCaminhoFoto(usuarioLogado.getCaminhoFoto());
            comentario.setComentario(textoComentario);
            if(comentario.salvar())
            {
                Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Insira um texto", Toast.LENGTH_SHORT).show();
        }
        //
    }

    private void recuperarComentarios()
    {
        comentariosRef = firebaseRef.child("comentarios")
                .child(idPostagem);
        valueEventListenerComentarios = comentariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listaComentarios.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    listaComentarios.add(ds.getValue(Comentario.class));
                }
                adapterComentarios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarComentarios();
    }

    @Override
    protected void onStop() {
        super.onStop();
        comentariosRef.removeEventListener(valueEventListenerComentarios);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}