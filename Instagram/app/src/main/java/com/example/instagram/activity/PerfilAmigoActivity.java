package com.example.instagram.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.adapter.AdapterGrid;
import com.example.instagram.helper.ConfiguracaoFirabese;
import com.example.instagram.helper.UsuarioFirebase;
import com.example.instagram.model.Postagem;
import com.example.instagram.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilAmigoActivity extends AppCompatActivity {

    private Usuario usuarioSelecionado;
    private Usuario usuarioLogado;

    private ProgressBar progressBar;
    private CircleImageView imagePerfil;
    public GridView gridViewPerfil;
    private TextView textPublicacoes, textSeguidores, textSeguindo;
    private Button buttonAcaoPerfil;

    DatabaseReference firebaseRef;
    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioAmigoRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference seguidoresRef;
    private ValueEventListener valueEventListenerPerfilAmigo;
    private DatabaseReference postagensUsuarioRef;

    private AdapterGrid adapterGrid;

    private String idUsuarioLogado;

    private List<Postagem> postagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_amigo);

        //Configuraçoes dos componentes;
        gridViewPerfil = findViewById(R.id.gridViewPerfil);
        progressBar = findViewById(R.id.progressBarPerfil);
        imagePerfil = findViewById(R.id.imagePerfilPostagem);
        textPublicacoes = findViewById(R.id.textPublicacoes);
        textSeguidores = findViewById(R.id.textSeguidores);
        textSeguindo = findViewById(R.id.textSeguindo);
        buttonAcaoPerfil = findViewById(R.id.buttonAcaoPerfil);

        //Configura texto inicial botao
        buttonAcaoPerfil.setText("Carregando...");

        //Configuracoes iniciais
        firebaseRef = ConfiguracaoFirabese.getReferenceFirabese();
        usuariosRef = firebaseRef.child("usuarios");
        seguidoresRef = firebaseRef.child("seguidores");

        //obterUsuarioLogado;
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        postagens = new ArrayList<>();

        //Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24);

        //recuperar usuarioSelecionado
        Bundle bundle = getIntent().getExtras();

        if(bundle != null)
        {
            usuarioSelecionado = (Usuario) bundle.getSerializable("usuarioSelecionado");

            //configurar referencia postagens usuario
            postagensUsuarioRef = ConfiguracaoFirabese.getReferenceFirabese()
                    .child("postagens")
                    .child(usuarioSelecionado.getId());


            getSupportActionBar().setTitle(usuarioSelecionado.getNome());

            String caminhoFoto = usuarioSelecionado.getCaminhoFoto();
            if (caminhoFoto == null || caminhoFoto.isEmpty())
            {
                imagePerfil.setImageResource(R.drawable.avatar);
            }
            else
            {
                Uri url = Uri.parse(caminhoFoto);
                Glide.with(PerfilAmigoActivity.this)
                        .load(url)
                        .into(imagePerfil);
            }
        }
        inicializarImageLoader();
        carregarFotosPostagem();

        gridViewPerfil.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Postagem postagem = postagens.get(position);
                Intent i = new Intent(getApplicationContext(), VisualizarPostagemActivity.class);
                i.putExtra("postagem", postagem);
                i.putExtra("usuarioSelecionado", usuarioSelecionado);
                startActivity(i);
            }
        });
    }
    public void inicializarImageLoader()
    {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(this)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);
    }


    public void carregarFotosPostagem()
    {
        postagens.clear();
        postagensUsuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int tamanhoGrid = getResources().getDisplayMetrics().widthPixels;
                int tamanhoImagem = tamanhoGrid/3;
                gridViewPerfil.setColumnWidth(tamanhoImagem);

                List<String> urlFotos = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Postagem postagem = dataSnapshot.getValue(Postagem.class);
                    postagens.add(postagem);
                    urlFotos.add(postagem.getCaminhoFoto());
                }
//                int qtdPostagem = urlFotos.size();
//                textPublicacoes.setText(String.valueOf(qtdPostagem));

                //Configurar Adapter
                adapterGrid = new AdapterGrid(getApplicationContext(), R.layout.grid_postagem, urlFotos);
                gridViewPerfil.setAdapter(adapterGrid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void recuperarDadosUsuarioLogado()
    {
        usuarioLogadoRef = usuariosRef.child(idUsuarioLogado);
        usuarioLogadoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        usuarioLogado = dataSnapshot.getValue(Usuario.class);
                        verificaSegueUsuarioAmigo();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void verificaSegueUsuarioAmigo()
    {
        DatabaseReference seguidorRef = seguidoresRef
                .child(usuarioSelecionado.getId())
                .child(idUsuarioLogado);

        seguidorRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists()) {
                            habilitarBotaoSeguir(true);
                        }else
                        {
                            habilitarBotaoSeguir(false);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void habilitarBotaoSeguir(boolean segueUsuario)
    {
        if(!segueUsuario)
        {
            buttonAcaoPerfil.setText("Seguir");
            buttonAcaoPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    salvarSeguidor(usuarioLogado, usuarioSelecionado);
                }
            });
        }
        else
        {
            buttonAcaoPerfil.setText("Seguindo");
        }
    }

    private void salvarSeguidor(Usuario usuarioLogado, Usuario usuarioAmigo)
    {
        HashMap<String, Object> dadosAmigo = new HashMap<String, Object>();
        dadosAmigo.put("nome", usuarioAmigo.getNome());
        dadosAmigo.put("caminhoFoto", usuarioAmigo.getCaminhoFoto());

        DatabaseReference seguidorRef = seguidoresRef
                .child(usuarioAmigo.getId())
                .child(usuarioLogado.getId());

        seguidorRef.setValue(dadosAmigo);
        buttonAcaoPerfil.setText("Seguindo");
        buttonAcaoPerfil.setOnClickListener(null);

        //Incrementar seguindo do usuario logado
        int seguindo = usuarioLogado.getSeguindo() +  1;

        HashMap<String, Object> dadosSeguindo = new HashMap<String, Object>();
        dadosSeguindo.put("seguindo", seguindo);

        DatabaseReference usuarioSeguindo = usuariosRef
                .child(usuarioLogado.getId());
        usuarioSeguindo.updateChildren(dadosSeguindo);

        //Incrementar seguidores do usuario amigo
        int seguidores = usuarioAmigo.getSeguidores() + 1;

        HashMap<String, Object> dadosSeguidores = new HashMap<String, Object>();
        dadosSeguidores.put("seguidores", seguidores);

        DatabaseReference usuarioSeguidores = usuariosRef
                .child(usuarioAmigo.getId());
        usuarioSeguidores.updateChildren(dadosSeguidores);

    }


    @Override
    protected void onStart() {
        super.onStart();
        recuperarDadosPerfilAmigo();
        recuperarDadosUsuarioLogado();
    }

    @Override
    protected void onStop() {
        super.onStop();
        usuarioAmigoRef.removeEventListener(valueEventListenerPerfilAmigo);
    }

    public void recuperarDadosPerfilAmigo()
    {
        usuarioAmigoRef = usuariosRef.child(usuarioSelecionado.getId());
        valueEventListenerPerfilAmigo = usuarioAmigoRef.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        String postagens = String.valueOf(usuario.getPostagens());
                        String seguindo  = String.valueOf(usuario.getSeguindo());
                        String seguidores= String.valueOf(usuario.getSeguidores());

                        textPublicacoes.setText(postagens);
                        textSeguidores.setText(seguidores);
                        textSeguindo.setText(seguindo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return false;
    }
}