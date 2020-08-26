package com.example.instagram.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.model.Postagem;
import com.example.instagram.model.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class VisualizarPostagemActivity extends AppCompatActivity {

    private TextView textPerfilPostagem;
    private TextView textQtCurtidas;
    private TextView textDescricaoPostagem;
    private TextView textVisualizarComentariosPostagem;
    private ImageView imagemPostagemSelecionada;
    private CircleImageView imagePerfilPostagem;

    private Postagem postagem;
    private Usuario usuarioSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_postagem);

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Visualizar Postagem");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24);

        inicializarComponentes();

        Bundle bundle = getIntent().getExtras();
        if(bundle != null)
        {
            postagem = (Postagem) bundle.getSerializable("postagem");
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            //Exibir dados do usuario
            Uri uri = Uri.parse(usuarioSelecionado.getCaminhoFoto());
            Glide.with(VisualizarPostagemActivity.this)
                    .load(uri)
                    .into(imagePerfilPostagem);
            textPerfilPostagem.setText(usuarioSelecionado.getNome());

            //Exibir dados da pastagem
            Uri uriPostagem = Uri.parse(postagem.getCaminhoFoto());
            Glide.with(VisualizarPostagemActivity.this)
                    .load(uri)
                    .into(imagemPostagemSelecionada);
            textDescricaoPostagem.setText(postagem.getDescricao());

        }
    }

    private void inicializarComponentes()
    {
        textPerfilPostagem                  = findViewById(R.id.textPerfilPostagem);
        textQtCurtidas                      = findViewById(R.id.textQtCurtidas);
        textDescricaoPostagem               = findViewById(R.id.textDescricaoPostagem);
//        textVisualizarComentariosPostagem   = findViewById(R.id.textVisualizarComentariosPostagem);
        imagemPostagemSelecionada           = findViewById(R.id.imagemPostagemSelecionada);
        imagePerfilPostagem                 = findViewById(R.id.imagePerfilPostagem);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}